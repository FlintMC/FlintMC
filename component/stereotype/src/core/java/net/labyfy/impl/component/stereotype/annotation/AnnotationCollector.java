package net.labyfy.impl.component.stereotype.annotation;

import com.google.common.collect.Sets;
import net.labyfy.component.stereotype.annotation.Transitive;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Collection;

/**
 * Util class to collect annotations ant their properties.
 */
public class AnnotationCollector {

  private AnnotationCollector() {}

  /**
   * Collects all transitive annotations.
   * That means, every annotation that is annotated to the annotation on the input class
   * will be recursively collected, as long as they have a {@link Transitive} annotation in the recursive order.
   *
   * @param clazz the input class
   * @return all transitive annotations
   */
  public static Collection<Annotation> getTransitiveAnnotations(Class<?> clazz) {
    Collection<Annotation> annotations = Sets.newHashSet();
    for (Annotation declaredAnnotation : clazz.getDeclaredAnnotations()) {
      if (getRealAnnotationClass(declaredAnnotation).isAnnotationPresent(Transitive.class))
        annotations.add(declaredAnnotation);
    }
    return annotations;
  }

  /**
   * Collects all transitive annotations.
   * That means, every annotation that is annotated to the annotation on the input class
   * will be recursively collected, as long as they have a {@link Transitive} annotation in the recursive order.
   *
   * @param clazz the input class
   * @return all transitive annotations
   */
  public static Collection<Annotation> getTransitiveAnnotations(Method method) {
    Collection<Annotation> annotations = Sets.newHashSet();
    for (Annotation declaredAnnotation : method.getDeclaredAnnotations()) {
      if (getRealAnnotationClass(declaredAnnotation).isAnnotationPresent(Transitive.class))
        annotations.add(declaredAnnotation);
    }
    return annotations;
  }

  /**
   * Finds the real annotation type of an annotation.
   * Ff getClass() is called on a proxied annotation, the result will be {@link Proxy} and not the real type.
   *
   * @param annotation input annotation
   * @return the real annotation type
   */
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
