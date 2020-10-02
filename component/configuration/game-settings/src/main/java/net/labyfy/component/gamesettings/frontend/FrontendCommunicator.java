package net.labyfy.component.gamesettings.frontend;

import com.google.gson.JsonObject;

import java.util.Map;

public interface FrontendCommunicator {

  JsonObject parseOption(Map<String, String> configurations);

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
