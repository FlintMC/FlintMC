package net.flintmc.mcapi.world.storage.service.exception;

import net.flintmc.mcapi.world.storage.service.WorldLoader;

/**
 * The exception is thrown if the worlds have not yet been loaded.
 *
 * @see WorldLoader#getWorlds()
 */
public class WorldLoadException extends RuntimeException {

  /**
   * {@inheritDoc}
   */
  public WorldLoadException(String message) {
    super(message);
  }

  /**
   * {@inheritDoc}
   */
  public WorldLoadException(String message, Throwable cause) {
    super(message, cause);
  }
}
