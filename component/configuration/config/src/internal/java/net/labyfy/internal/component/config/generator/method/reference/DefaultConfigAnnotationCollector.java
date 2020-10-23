package net.labyfy.internal.component.config.generator.method.reference;

import com.google.inject.Singleton;
import net.labyfy.component.config.generator.ConfigAnnotationCollector;
import net.labyfy.component.inject.implement.Implement;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;

@Singleton
@Implement(ConfigAnnotationCollector.class)
public class DefaultConfigAnnotationCollector implements ConfigAnnotationCollector {

  @Override
  public <A extends Annotation> A findLastAnnotation(Method[] methods, Class<? extends A> annotationType) {
    // methods have a higher priority than classes
    for (Method method : methods) {
      if (method.isAnnotationPresent(annotationType)) {
        return method.getAnnotation(annotationType);
      }
    }

    for (int i = methods.length - 1; i >= 0; i--) {
      Method method = methods[i];

      Collection<Class<?>> subTypes = new HashSet<>();
      this.collectSuperclasses(method.getDeclaringClass(), subTypes);
      for (Class<?> subType : subTypes) {
        try {
          // check for methods in the superclass/interface
          Method subMethod = subType.getDeclaredMethod(method.getName(), method.getParameterTypes());
          if (subMethod.isAnnotationPresent(annotationType)) {
            return subMethod.getAnnotation(annotationType);
          }
        } catch (NoSuchMethodException ignored) {
        }
      }

      for (Class<?> subType : subTypes) {
        if (subType.isAnnotationPresent(annotationType)) {
          return subType.getAnnotation(annotationType);
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
