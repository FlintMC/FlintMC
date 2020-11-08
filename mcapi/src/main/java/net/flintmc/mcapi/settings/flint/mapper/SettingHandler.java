package net.flintmc.mcapi.settings.flint.mapper;

import com.google.gson.JsonObject;
import net.flintmc.framework.config.generator.method.ConfigObjectReference;
import net.flintmc.mcapi.settings.flint.annotation.ApplicableSetting;
import net.flintmc.mcapi.settings.flint.registered.RegisteredSetting;

import java.lang.annotation.Annotation;

/**
 * Handler for {@link ApplicableSetting}s.
 *
 * @param <A> The annotation that can be handled by this handler, should be annotated with {@link ApplicableSetting}
 */
public interface SettingHandler<A extends Annotation> {

  /**
   * Serializes the given value with all necessary information (can be used from the annotation and setting) into a json
   * object.
   *
   * @param annotation   The non-null annotation from the setting ({@link RegisteredSetting#getAnnotation()}
   * @param setting      The non-null setting that contains the given value
   * @param currentValue The non-null value to be serialized
   * @return The new non-null json object with the serialized data
   */
  JsonObject serialize(A annotation, RegisteredSetting setting, Object currentValue);

  /**
   * Checks whether the given {@code input} is valid to set for the given annotation.
   *
   * @param input      The nullable input to check for
   * @param reference  The non-null reference where the given setting is attached to
   * @param annotation The non-null annotation to get information from
   * @return {@code true} if the given input is valid for the given annotation, {@code false} otherwise
   */
  boolean isValidInput(Object input, ConfigObjectReference reference, A annotation);

}
