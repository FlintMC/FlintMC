package net.labyfy.component.settings.mapper;

import net.labyfy.component.processing.autoload.DetectableAnnotation;
import net.labyfy.component.settings.options.text.StringSetting;

import java.lang.annotation.*;

/**
 * Registers a {@link SettingHandler}. The annotated class needs to implement {@link SettingHandler}.
 *
 * @see SettingHandler
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@DetectableAnnotation
public @interface RegisterSettingHandler {

  /**
   * Retrieves the annotation can be handled by the annotated handler, for example {@link StringSetting}.
   *
   * @return The annotation that can be handled by the handler
   */
  Class<? extends Annotation> value();

}
