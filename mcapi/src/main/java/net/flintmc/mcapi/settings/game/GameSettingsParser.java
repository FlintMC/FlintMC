package net.flintmc.mcapi.settings.game;

import java.io.File;
import java.util.Map;

/**
 * An interface to parse the Minecraft game settings.
 */
public interface GameSettingsParser {

  /**
   * Retrieves the options file of Minecraft.
   *
   * @return The `options.txt` file.
   */
  File getOptionsFile();

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

}
