package net.flintmc.framework.config.modifier;

import net.flintmc.processing.autoload.DetectableAnnotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a {@link ConfigModificationHandler} to be registered in the {@link ConfigModifierRegistry}.
 *
 * @see ConfigModificationHandler
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@DetectableAnnotation
public @interface AnnotationModifier {

  /**
   * The class where the annotation to modify is set.
   *
   * @return The class where the annotation to modify is set
   */
  Class<?> value();

  /**
   * The method in the {@link #value() class} where the annotation to modify is set
   *
   * @return The method where the annotation to modify is set
   */
  String method();

}
