package net.flintmc.framework.config.serialization;

import com.google.gson.JsonElement;
import net.flintmc.framework.config.annotation.Config;

/**
 * Handler for the serialization of values in a {@link Config}. To register one, {@link ConfigSerializer} may be used.
 *
 * @param <T> The type that can be serialized by this handler
 * @see ConfigSerializer
 */
public interface ConfigSerializationHandler<T> {

  /**
   * Serializes the given object into a {@link JsonElement}.
   *
   * @param t The non-null value to be serialized
   * @return The new non-null json element containing the serialized object
   */
  JsonElement serialize(T t);

  /**
   * Deserializes the given {@link JsonElement} into an object.
   *
   * @param element The non-null json element to be deserialized
   * @return The new non-null object from the given json element
   */
  T deserialize(JsonElement element);

}
