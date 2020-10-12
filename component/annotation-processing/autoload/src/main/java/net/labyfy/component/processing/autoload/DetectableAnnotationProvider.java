package net.labyfy.component.processing.autoload;

import java.util.List;

/**
 * Interface to create auto generated classes from to discover {@link DetectableAnnotation}.
 */
public interface DetectableAnnotationProvider {
  default void register(List<AnnotationMeta> consumer) {
  }

}
