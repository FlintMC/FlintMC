package net.flintmc.mcapi.items.inventory.event;

import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.items.inventory.Inventory;

/**
 * The base event for every action that happens in an inventory.
 *
 * @see Subscribe
 */
public interface InventoryEvent {

  /**
   * Retrieves the inventory where this event has happened.
   *
   * @return The non-null inventory
   */
  Inventory getInventory();

  /** Factory for the {@link InventoryEvent}. */
  @AssistedFactory(InventoryEvent.class)
  interface Factory {

    /**
     * Creates a new {@link InventoryEvent} with the given inventory.
     *
     * @param inventory The non-null inventory where the event has happened
     * @return The new event
     */
    InventoryEvent create(@Assisted("inventory") Inventory inventory);
  }
}
