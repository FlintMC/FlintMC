package net.labyfy.component.processing.autoload;

import java.lang.annotation.*;

/**
 * Marks an annotation that can be discovered at runtime.
 * Annotations that are annotated with this will be parsed at compile time and written to a {@link DetectableAnnotationProvider} by the autoload module.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface DetectableAnnotation {

  /**
   * Defines the meta data types that can be attached to the target annotation.
   * In order to be recognized every metadata annotation must be annotated with {@link DetectableAnnotation} too.
   */
  Class<? extends Annotation>[] metaData() default {};

  boolean requiresParent() default false;

}
