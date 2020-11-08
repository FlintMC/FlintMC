package net.flintmc.framework.config.modifier;

import net.flintmc.framework.config.annotation.Config;

import java.lang.annotation.Annotation;

/**
 * Handler for the modification of annotations in a {@link Config}. To register one, {@link AnnotationModifier} may be
 * used.
 * <p>
 * This can be used to dynamically set values on an annotation of a config value, especially useful for the Settings
 * module.
 *
 * @see AnnotationModifier
 */
public interface ConfigModificationHandler {

  /**
   * Modifies the given annotation, the exact process depends on the implementation.
   *
   * @param annotation The non-null annotation to be modified
   * @return The modified/new annotation, {@code null} if no modification should be applied
   */
  Annotation modify(Annotation annotation);

}
