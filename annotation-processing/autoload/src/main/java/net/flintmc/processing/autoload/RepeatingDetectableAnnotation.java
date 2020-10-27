package net.flintmc.processing.autoload;

import java.lang.annotation.*;

/**
 * Marks a {@link DetectableAnnotation} that is repeatable. The repeated annotation must be
 * annotated with @{@link Repeatable}. The repeating annotation must be annotated with {@link
 * RepeatingDetectableAnnotation}
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface RepeatingDetectableAnnotation {

  /** @return The repeating type of this annotation */
  Class<? extends Annotation> value() default Annotation.class;
}
