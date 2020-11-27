package net.flintmc.mcapi.items.inventory.event;

import net.flintmc.framework.eventbus.event.Event;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.items.ItemStack;
import net.flintmc.mcapi.items.inventory.Inventory;

/**
 * This event will be fired when an item in the inventory gets updated. For client-side changes , it
 * will only be fired for inventories that are not the player inventory (not including the armor and
 * crafting slots of the player) in the survival gamemode, for those see the {@link
 * InventoryClickEvent} and {@link InventoryHotkeyPressEvent} and only in the {@link
 * Subscribe.Phase#PRE} phase. A client-side change means a click with the mouse or hotkey press
 * inside of an inventory.
 *
 * @see Subscribe
 */
public interface InventoryUpdateSlotEvent extends Event, InventorySlotEvent, InventoryEvent {

  /**
   * Retrieves the item that was set before the update on the slot that has been updated.
   *
   * @return The non-null item from before the update
   */
  ItemStack getPreviousItem();

  /**
   * Retrieves the new item that is now set on the slot that has been updated.
   *
   * @return The non-null item from the update
   */
  ItemStack getNewItem();

  /** Factory for the {@link InventoryUpdateSlotEvent}. */
  @AssistedFactory(InventoryUpdateSlotEvent.class)
  interface Factory {

    /**
     * Creates a new {@link InventoryUpdateSlotEvent}.
     *
     * @param inventory The non-null inventory in which the slot has been updated
     * @param slot The slot that has been updated
     * @param previousItem The non-null item that was set in the given slot before the update
     * @param newItem The non-null item that is now set after the update
     * @return The new non-null {@link InventoryUpdateSlotEvent}.
     */
    InventoryUpdateSlotEvent create(
        @Assisted("inventory") Inventory inventory,
        @Assisted("slot") int slot,
        @Assisted("previousItem") ItemStack previousItem,
        @Assisted("newItem") ItemStack newItem);
  }
}
