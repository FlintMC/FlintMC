package net.flintmc.mcapi.settings.flint.serializer;

import com.google.gson.JsonObject;
import net.flintmc.mcapi.settings.flint.annotation.ApplicableSetting;
import net.flintmc.mcapi.settings.flint.options.text.StringSetting;
import net.flintmc.mcapi.settings.flint.registered.RegisteredSetting;

import java.lang.annotation.Annotation;

/**
 * Handler for the serialization of annotations that can be applied on a method that is annotated with any {@link
 * ApplicableSetting} (e.g. {@link StringSetting}).
 *
 * @param <A> The type of annotation that can be serialized by this handler
 */
public interface SettingsSerializationHandler<A extends Annotation> {

  /**
   * Serializes the given {@code annotation} into the given json object.
   * <p>
   * This method should not add the following values to the json object because they are already added by the {@link
   * JsonSettingsSerializer} by default: type, name, enabled, category, native, subSettings, subCategory
   * <p>
   * The new contents of the json object depend on the implementation.
   *
   * @param result     The non-null json object to serialize the given annotation into
   * @param setting    The non-null setting where the given annotation has been found
   * @param annotation The annotation that has been found matching the type that is supported by this handler, may be
   *                   {@code null} if no annotation with the given type was found
   */
  void append(JsonObject result, RegisteredSetting setting, A annotation);

}
