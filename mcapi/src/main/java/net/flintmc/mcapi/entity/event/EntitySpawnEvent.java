package net.flintmc.mcapi.entity.event;

import net.flintmc.framework.eventbus.event.Event;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.entity.Entity;

/**
 * This event will be fired when an entity is spawned in the world the player is currently in.
 *
 * <p>When joining a world or server (Singleplayer or Multiplayer), this event will be fired for
 * every entity in the world and the player himself.
 *
 * <p>It will only be fired in the {@link Subscribe.Phase#POST} phase.
 *
 * @see Subscribe
 */
public interface EntitySpawnEvent extends Event {

  /**
   * Retrieves the entity that has been spawned in this event.
   *
   * @return The non-null entity that has been spawned
   */
  Entity getEntity();

  /**
   * Factory for the {@link EntitySpawnEvent}.
   */
  @AssistedFactory(EntitySpawnEvent.class)
  interface Factory {

    /**
     * Creates a new {@link EntitySpawnEvent} for the given entity.
     *
     * @param entity The non-null entity that has been spawned
     * @return The new non-null {@link EntitySpawnEvent}
     */
    EntitySpawnEvent create(@Assisted Entity entity);
  }
}
