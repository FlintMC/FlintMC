package net.labyfy.component.gamesettings;

import java.io.File;
import java.util.Map;

public interface GameSettingInterceptor {

  /**
   * Makes qualified key bindings for the specific version.
   */
  void makeQualifiedKeyBinds(File optionsFile, Map<String, String> configurations);

  /**
   * Reads all configuration from the "options.txt" file and puts it into the a key-value system.
   *
   * @return A key-value system with the configurations from the options.txt.
   */
  Map<String, String> readOptions(File optionsFile);

  /**
   * Saves the given key-value system to the "options.txt" file.
   *
   * @param configurations The key-value system to be stored.
   */
  void saveOptions(File optionsFile, Map<String, String> configurations);

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
