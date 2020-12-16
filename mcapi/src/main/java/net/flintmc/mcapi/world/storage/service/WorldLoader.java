package net.flintmc.mcapi.world.storage.service;

import java.util.List;
import net.flintmc.mcapi.world.storage.WorldOverview;
import net.flintmc.mcapi.world.storage.service.exception.WorldLoadException;

/**
 * Represents a loader to load worlds.
 */
public interface WorldLoader {

  /**
   * Loads all saved worlds.
   */
  void loadWorlds();

  /**
   * Retrieves a collection with all loaded worlds.
   *
   * @return A collection with all loaded worlds.
   * @throws WorldLoadException Will be thrown if the worlds are not loaded.
   */
  List<WorldOverview> getWorlds();

  /**
   * Whether the world can be loaded.
   *
   * @param fileName The file name of the world to be loaded.
   * @return {@code true} if the world can be loaded, otherwise {@code false}.
   */
  boolean canLoadWorld(String fileName);

  /**
   * Whether the world loader has loaded the worlds.
   *
   * @return {@code true} if the world loader has loaded the worlds, otherwise {@code false}.
   */
  boolean isLoaded();

}
