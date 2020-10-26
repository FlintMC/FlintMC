package net.flintmc.processing.autoload;

import java.util.List;

/**
 * Interface to create auto generated classes from to discover {@link DetectableAnnotation}.
 */
public interface DetectableAnnotationProvider {

  /**
   * Adds representations of {@link DetectableAnnotation} as {@link AnnotationMeta} to a list.
   *
   * @param list List to add the detected annotation metadata to
   */
  default void register(List<AnnotationMeta> list) {
  }
}
