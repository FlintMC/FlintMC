package net.labyfy.base.structure.annotation;

import com.google.common.collect.Sets;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Collection;

public class AnnotationCollector {

  private AnnotationCollector() {}

  public static Collection<Annotation> getTransitiveAnnotations(Class<?> clazz) {
    Collection<Annotation> annotations = Sets.newHashSet();
    for (Annotation declaredAnnotation : clazz.getDeclaredAnnotations()) {
      if (getRealAnnotationClass(declaredAnnotation).isAnnotationPresent(Transitive.class))
        annotations.add(declaredAnnotation);
    }
    return annotations;
  }

  public static Collection<Annotation> getTransitiveAnnotations(Method method) {
    Collection<Annotation> annotations = Sets.newHashSet();
    for (Annotation declaredAnnotation : method.getDeclaredAnnotations()) {
      if (getRealAnnotationClass(declaredAnnotation).isAnnotationPresent(Transitive.class))
        annotations.add(declaredAnnotation);
    }
    return annotations;
  }

  public static Class<? extends Annotation> getRealAnnotationClass(Annotation annotation) {
    Class<? extends Annotation> annotationClass;
    if (annotation instanceof Proxy) {
      annotationClass = (Class<? extends Annotation>) annotation.getClass().getInterfaces()[0];
    } else {
      annotationClass = annotation.getClass();
    }
    return annotationClass;
  }
}
