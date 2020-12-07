package net.flintmc.mcapi.entity.event;

import net.flintmc.framework.eventbus.event.Event;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.entity.Entity;

/**
 * This event will be fired when an entity is despawned in the world the player is currently in. If
 * the player switches the world (e.g. through a nether portal), this event won't be fired for each
 * entity in the previous world.
 *
 * <p>When leaving a world in Singleplayer (not in Multiplayer), this event will also be fired for
 * the player himself, but not for other entities in the world.
 *
 * <p>It will only be fired in the {@link Subscribe.Phase#POST} phase.
 *
 * @see Subscribe
 */
public interface EntityDespawnEvent extends Event {

  /**
   * Retrieves the entity that has been despawned in this event.
   *
   * @return The non-null entity that has been despawned
   */
  Entity getEntity();

  /** Factory for the {@link EntityDespawnEvent}. */
  @AssistedFactory(EntityDespawnEvent.class)
  interface Factory {

    /**
     * Creates a new {@link EntityDespawnEvent} for the given entity.
     *
     * @param entity The non-null entity that has been despawned
     * @return The new non-null {@link EntityDespawnEvent}
     */
    EntityDespawnEvent create(@Assisted Entity entity);
  }
}
