package net.labyfy.component.settings.serializer;

import net.labyfy.component.processing.autoload.DetectableAnnotation;
import net.labyfy.component.settings.annotation.ui.DisplayName;

import java.lang.annotation.*;

/**
 * Marks a {@link SettingsSerializationHandler} to be registered in the {@link JsonSettingsSerializer}. The annotated
 * class has to implement {@link SettingsSerializationHandler}.
 *
 * @see SettingsSerializationHandler
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@DetectableAnnotation
public @interface SettingsSerializer {

  /**
   * Retrieves the annotation that can be serialized into a json element by the annotated serialization handler. For
   * example {@link DisplayName}.
   *
   * @return The annotation that can be serialized
   */
  Class<? extends Annotation> value();

}
