package net.flintmc.mcapi.items.inventory.event;

import net.flintmc.framework.eventbus.event.Event;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.items.inventory.Inventory;
import net.flintmc.mcapi.items.inventory.player.PlayerInventory;

/**
 * This event will be fired whenever an inventory is opened in the client. Tt will only be fired in
 * the POST phase.
 *
 * <p>The client itself can only open a {@link PlayerInventory}, other inventories are all sent by
 * the server.
 *
 * @see Subscribe
 */
public interface InventoryOpenEvent extends Event, InventoryEvent {

  /** Factory for the {@link InventoryOpenEvent}. */
  @AssistedFactory(InventoryOpenEvent.class)
  interface Factory {

    /**
     * Creates a new {@link InventoryOpenEvent} with the given inventory and direction.
     *
     * @param inventory The non-null inventory where the event has happened
     * @return The new event
     */
    InventoryOpenEvent create(@Assisted("inventory") Inventory inventory);
  }
}
