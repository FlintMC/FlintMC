package net.flintmc.mcapi.world.event;

import net.flintmc.framework.eventbus.event.Event;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;

/**
 * This event will be fired when a chunk is loaded in either a Singleplayer or Multiplayer world. It
 * will also be fired for each chunk when joining a world or server. It will be fired in both the
 * {@link Subscribe.Phase#PRE} and {@link Subscribe.Phase#POST} phases.
 *
 * @see Subscribe
 */
public interface ChunkLoadEvent extends Event {

  /**
   * Retrieves the X coordinate of the chunk that has been loaded.
   *
   * @return The X coordinate of the chunk
   */
  int getX();

  /**
   * Retrieves the Y coordinate of the chunk that has been loaded.
   *
   * @return The Y coordinate of the chunk
   */
  int getZ();

  /**
   * Factory for the {@link ChunkLoadEvent}.
   */
  @AssistedFactory(ChunkLoadEvent.class)
  interface Factory {

    /**
     * Creates a new {@link ChunkLoadEvent} with the given chunk coordinates.
     *
     * @param x The X coordinate of the chunk
     * @param z The Z coordinate of the chunk
     * @return The new non-null {@link ChunkLoadEvent}
     */
    ChunkLoadEvent create(@Assisted("x") int x, @Assisted("z") int z);
  }
}
