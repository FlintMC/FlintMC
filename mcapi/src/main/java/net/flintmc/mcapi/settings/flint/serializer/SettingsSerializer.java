package net.flintmc.mcapi.settings.flint.serializer;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import net.flintmc.mcapi.settings.flint.annotation.ui.DisplayName;
import net.flintmc.processing.autoload.DetectableAnnotation;

/**
 * Marks a {@link SettingsSerializationHandler} to be registered in the {@link
 * JsonSettingsSerializer}. The annotated class has to implement {@link
 * SettingsSerializationHandler}.
 *
 * @see SettingsSerializationHandler
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@DetectableAnnotation
public @interface SettingsSerializer {

  /**
   * Retrieves the annotation that can be serialized into a json element by the annotated
   * serialization handler. For example {@link DisplayName}.
   *
   * @return The annotation that can be serialized
   */
  Class<? extends Annotation> value();
}
