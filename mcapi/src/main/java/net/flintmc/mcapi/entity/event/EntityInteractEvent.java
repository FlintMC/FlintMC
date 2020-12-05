package net.flintmc.mcapi.entity.event;

import net.flintmc.framework.eventbus.event.Event;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.entity.Entity;
import net.flintmc.mcapi.items.inventory.player.PlayerHand;

/**
 * This event will be fired when the user interacts with (right click) another entity. This entity
 * can be anything, for example another player, zombie, ... The gamemode of the client doesn't
 * matter, it will also be fired in spetator mode. It will only be fired in the {@link
 * Subscribe.Phase#PRE} phase. This event may also be fired twice per right click when interacting
 * with both {@link PlayerHand}s.
 *
 * @see Subscribe
 */
public interface EntityInteractEvent extends Event {

  /**
   * Retrieves the entity that has been interacted with.
   *
   * @return The non-null entity that has been interacted with
   */
  Entity getInteracted();

  /**
   * Retrieves the hand that has been used for the interaction.
   *
   * @return The non-null hand that has been used for the interaction
   */
  PlayerHand getHand();

  /** Factory for the {@link EntityInteractEvent}. */
  @AssistedFactory(EntityInteractEvent.class)
  interface Factory {

    /**
     * Creates a new {@link EntityInteractEvent} with the given entity as the interacted one and the
     * given hand.
     *
     * @param interacted The non-null entity that has been interacted with
     * @param hand The non-null hand that has been used for the interaction
     * @return The new non-null {@link EntityInteractEvent}
     */
    EntityInteractEvent create(@Assisted Entity interacted, @Assisted PlayerHand hand);
  }
}
