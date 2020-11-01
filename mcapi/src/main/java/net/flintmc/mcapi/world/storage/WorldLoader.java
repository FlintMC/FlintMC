package net.flintmc.mcapi.world.storage;

import java.util.Collection;

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
   */
  Collection<WorldOverview> getWorlds();

  /**
   * Whether the world can be loaded.
   *
   * @param fileName The file name of the world to be loaded.
   * @return {@code true} if the world can be loaded, otherwise {@code false}.
   */
  boolean canLoadWorld(String fileName);

}
