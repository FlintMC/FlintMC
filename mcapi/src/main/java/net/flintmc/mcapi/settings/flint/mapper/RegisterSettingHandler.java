package net.flintmc.mcapi.settings.flint.mapper;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import net.flintmc.mcapi.settings.flint.options.text.StringSetting;
import net.flintmc.processing.autoload.DetectableAnnotation;

/**
 * Registers a {@link SettingHandler}. The annotated class needs to implement {@link
 * SettingHandler}.
 *
 * @see SettingHandler
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@DetectableAnnotation
public @interface RegisterSettingHandler {

  /**
   * Retrieves the annotation can be handled by the annotated handler, for example {@link
   * StringSetting}.
   *
   * @return The annotation that can be handled by the handler
   */
  Class<? extends Annotation> value();
}
