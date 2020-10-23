package net.labyfy.component.config.generator;

import net.labyfy.component.config.generator.method.ConfigObjectReference;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * This class collects annotations on specific methods and classes.
 */
public interface ConfigAnnotationCollector {

  /**
   * Finds the last annotation in any of the given methods.
   *
   * @param methods        The non-null array of methods to search for
   * @param annotationType The non-null type of the annotation
   * @param <A>            The annotation which should be searched
   * @return The discovered annotation or {@code null} if no method/class associated with this reference is annotated
   * with it.
   * @see ConfigObjectReference#findLastAnnotation(Class)
   */
  <A extends Annotation> A findLastAnnotation(Method[] methods, Class<? extends A> annotationType);

}
