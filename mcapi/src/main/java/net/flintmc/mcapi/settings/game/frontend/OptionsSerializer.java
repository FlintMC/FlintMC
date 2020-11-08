package net.flintmc.mcapi.settings.game.frontend;

import com.google.gson.JsonObject;

import java.util.Map;

/**
 * Represents a serializer for the options.
 */
public interface OptionsSerializer {

  /**
   * Serializes the given key value system which represents the `options.txt` file into a {@link JsonObject}.
   *
   * @param configurations The `options.txt` file as a key-value system.
   * @return The parsed json.
   */
  JsonObject serialize(Map<String, String> configurations);

  /**
   * Deserializes the given {@link JsonObject} to a key-value system which represents the `options.txt` file.
   *
   * @param object The `options.txt` file as a {@link JsonObject}.
   * @return The parsed key-value system.
   */
  Map<String, String> deserialize(JsonObject object);

  /**
   * Retrieves the Minecraft `options.txt` as a {@link JsonObject}.
   *
   * @return The Minecraft `options.txt` as a {@link JsonObject}.
   */
  JsonObject getConfigurationObject();

  /**
   * Updates and save this configuration to the options.txt file.
   *
   * @param object The new configuration.
   */
  void updateConfiguration(JsonObject object);

}
