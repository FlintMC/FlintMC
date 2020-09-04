package net.labyfy.chat.serializer;

import com.google.gson.Gson;

/**
 * Serializer for components which uses {@link Gson} for serialization and deserialization.
 */
public interface GsonComponentSerializer extends ComponentSerializer {

  /**
   * Retrieves the {@link Gson} instance which is used to serialize/deserialize components in this serializer.
   *
   * @return The non-null gson instance for serialization
   */
  Gson getGson();

}
