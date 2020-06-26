package net.labyfy.component.commons.resolve;

import java.lang.annotation.Annotation;

/**
 * Generic interface to resolve annotations to values.
 *
 * @param <A> The type of the annotation to resolve
 * @param <R> The resolved type
 */
@FunctionalInterface
public interface AnnotationResolver<A extends Annotation, R> extends Resolver<A, R> {
  /**
   * Resolves the given annotation to a value.
   *
   * @param a The annotation to resolve
   * @return The resolved value
   */
  R resolve(A a);
}
