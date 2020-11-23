package net.flintmc.framework.config.internal.generator.method.reference;

import com.google.inject.Singleton;
import net.flintmc.framework.config.generator.ConfigAnnotationCollector;
import net.flintmc.framework.inject.implement.Implement;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.function.Predicate;

@Singleton
@Implement(ConfigAnnotationCollector.class)
public class DefaultConfigAnnotationCollector implements ConfigAnnotationCollector {

  @Override
  public <A extends Annotation> A findLastAnnotation(
      Method[] methods, Class<? extends A> annotationType) {
    return this.forEachAnnotations(methods, annotationType, a -> true);
  }

  @Override
  public Collection<Annotation> findAllAnnotations(Method[] methods) {
    Collection<Annotation> annotations = new ArrayList<>();

    this.forEachAnnotations(
        methods,
        null,
        annotation -> {
          annotations.add(annotation);
          return false;
        });

    return annotations;
  }

  @Override
  public <A extends Annotation> Collection<A> getAllAnnotations(Class<?> clazz, Class<A> annotationType) {
    Collection<A> result = new ArrayList<>();

    this.addAllAnnotations(clazz, annotationType, result);

    return result;
  }

  private <A extends Annotation> void addAllAnnotations(
      Class<?> clazz, Class<A> annotationType, Collection<A> result) {
    for (Class<?> ifc : clazz.getInterfaces()) {
      this.addAllAnnotations(ifc, annotationType, result);
    }

    Class<?> superClass = clazz.getSuperclass();
    if (superClass != null && !Object.class.equals(superClass)) {
      this.addAllAnnotations(superClass, annotationType, result);
    }

    A annotation = clazz.getAnnotation(annotationType);
    if (annotation != null) {
      result.add(annotation);
    }
  }

  private <A extends Annotation> A forEachAnnotations(
      Method[] methods, Class<A> annotationType, Predicate<A> handler) {
    // methods have a higher priority than classes
    for (Method method : methods) {
      A a = this.testAnnotations(annotationType, method.getAnnotations(), handler);
      if (a != null) {
        return a;
      }
    }

    for (int i = methods.length - 1; i >= 0; i--) {
      Method method = methods[i];

      Collection<Class<?>> subTypes = new HashSet<>();
      subTypes.add(method.getDeclaringClass());
      this.collectSuperclasses(method.getDeclaringClass(), subTypes);

      for (Class<?> subType : subTypes) {
        try {
          // check for methods in the superclass/interface
          Method subMethod =
              subType.getDeclaredMethod(method.getName(), method.getParameterTypes());
          A a = this.testAnnotations(annotationType, subMethod.getAnnotations(), handler);
          if (a != null) {
            return a;
          }
        } catch (NoSuchMethodException ignored) {
        }
      }

      for (Class<?> subType : subTypes) {
        A a = this.testAnnotations(annotationType, subType.getAnnotations(), handler);
        if (a != null) {
          return a;
        }
      }
    }

    return null;
  }

  @SuppressWarnings("unchecked")
  private <A extends Annotation> A testAnnotations(
      Class<A> annotationType, Annotation[] annotations, Predicate<A> handler) {
    for (Annotation annotation : annotations) {
      if (annotationType == null || annotationType.equals(annotation.annotationType())) {
        A a = (A) annotation;
        if (handler.test(a)) {
          return a;
        }
      }
    }

    return null;
  }

  private void collectSuperclasses(Class<?> type, Collection<Class<?>> target) {
    if (type.getSuperclass() != null && type.getSuperclass() != Object.class) {
      target.add(type.getSuperclass());
      this.collectSuperclasses(type.getSuperclass(), target);
    }

    for (Class<?> subType : type.getInterfaces()) {
      target.add(subType);
      this.collectSuperclasses(subType, target);
    }
  }
}
