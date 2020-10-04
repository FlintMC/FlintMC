package net.labyfy.component.gamesettings.frontend;

import com.google.gson.JsonObject;

import java.util.Map;

/**
 * Represents the communicator for the frontend.
 */
public interface FrontendCommunicator {

  /**
   * Parses the given key value system which represents the `options.txt` file into a {@link JsonObject}.
   *
   * @param configurations The `options.txt` file as a key-value system.
   * @return The parsed json.
   */
  JsonObject parseOption(Map<String, String> configurations);

  /**
   * Parses the given {@link JsonObject} to a key-value system which represents the `options.txt` file.
   *
   * @param object The `options.txt` file as a {@link JsonObject}.
   * @return The parsed key-value system.
   */
  Map<String, String> parseJson(JsonObject object);

  /**
   * Retrieves the minor version of Minecraft.
   *
   * @param version The current Minecraft version.
   * @return The minor version of Minecraft.
   * @throws NumberFormatException if the string does not contain a
   *                               parsable integer.
   */
  default int getMinorVersion(String version) {
    String[] elements = version.split("\\.");

    try {
      return Integer.parseInt(elements[1]);
    } catch (NumberFormatException exception) {
      return -1;
    }
  }

}
