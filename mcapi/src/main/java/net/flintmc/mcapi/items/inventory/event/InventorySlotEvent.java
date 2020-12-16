package net.flintmc.mcapi.items.inventory.event;

import com.google.inject.name.Named;
import net.flintmc.framework.eventbus.event.Event;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.items.inventory.Inventory;

/**
 * The base event for everything that happens inside an inventory on a specified slot.
 *
 * @see Subscribe
 */
public interface InventorySlotEvent extends Event, InventoryEvent {

  /**
   * Retrieves the slot where this event has happened or {@code -1} if it didn't happen on any slot
   * (e.g. outside of the Inventory)
   *
   * <p>If the slot isn't {@code -1}, {@link Inventory#getItem(int)} will work with the given
   * inventory from {@link #getInventory()} and this slot.
   *
   * @return The slot of this event
   */
  @Named("slot")
  int getSlot();

  /** Factory for the {@link InventorySlotEvent}. */
  @AssistedFactory(InventorySlotEvent.class)
  interface Factory {

    /**
     * Creates a new {@link InventorySlotEvent} with the given inventory and slot.
     *
     * @param inventory The non-null inventory where the event has happened
     * @param slot The slot where this event has happened or {@code -1} if it happened outside of
     *     any slot. If the slot isn't {@code -1}, {@link Inventory#getItem(int)} with the given
     *     inventory and slot has to work.
     * @return The new event
     */
    InventorySlotEvent create(
        @Assisted("inventory") Inventory inventory, @Assisted("slot") int slot);
  }
}
