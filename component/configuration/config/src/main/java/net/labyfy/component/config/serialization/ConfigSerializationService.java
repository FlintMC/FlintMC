package net.labyfy.component.config.serialization;

import com.google.gson.JsonElement;
import javassist.CtClass;
import net.labyfy.component.config.annotation.Config;

/**
 * Registry containing all {@link ConfigSerializationHandler}s for the serialization in a {@link Config}.
 *
 * @see ConfigSerializationHandler
 */
public interface ConfigSerializationService {

  /**
   * Retrieves whether a serializer that is able to serialize objects of the given type exists. The types don't have to
   * be the same, it can also be any super class/interface.
   *
   * @param interfaceType The non-null type of the interface that should be serialized
   * @return {@code true} if a serializer for the given type exists, {@code false} otherwise
   */
  boolean hasSerializer(Class<?> interfaceType);

  /**
   * Retrieves whether a serializer that is able to serialize objects of the given type exists. The types don't have to
   * be the same, it can also be any super class/interface.
   *
   * @param interfaceType The non-null type of the interface that should be serialized
   * @return {@code true} if a serializer for the given type exists, {@code false} otherwise
   */
  boolean hasSerializer(CtClass interfaceType);

  /**
   * Retrieves a serializer that is able to serialize the given type. The types don't have to be the same, it can also
   * be any super class/interface.
   *
   * @param interfaceType The non-null type of the interface that should be serialized
   * @param <T>           The type of the interface that should be serialized
   * @return The handler for the given type or {@code null} if there is none
   */
  <T> ConfigSerializationHandler<T> getSerializer(Class<T> interfaceType);

  /**
   * Registers a new serializer in this registry.
   *
   * @param interfaceType The non-null type of interfaces that can be serialized by the given handler
   * @param handler       The non-null handler for serialization of interfaces of the given type
   * @param <T>           The type of the interface that can be serialized by the given handler
   */
  <T> void registerSerializer(Class<T> interfaceType, ConfigSerializationHandler<T> handler);

  /**
   * Serializes the given object into a {@link JsonElement}.
   *
   * @param interfaceType The non-null type of the interface of the value to get the handler for
   * @param value         The non-null value to be serialized
   * @param <T>           The type of the value
   * @return The new json element containing the serialized object or {@code null} if there is no handler for the given
   * type
   */
  <T> JsonElement serialize(Class<T> interfaceType, T value);

  /**
   * Deserializes the given {@link JsonElement} into an object.
   *
   * @param interfaceType The non-null type of the interface of the value to get the handler for
   * @param value         The non-null json element to be deserialized
   * @param <T>           The type of the value
   * @return The new object from the given json element or {@code null} if there is no handler for the given type
   */
  <T> T deserialize(Class<T> interfaceType, JsonElement value);

}
