package net.flintmc.mcapi.items.inventory.event;

import net.flintmc.framework.eventbus.event.Cancellable;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.items.ItemStack;
import net.flintmc.mcapi.items.inventory.Inventory;
import net.flintmc.mcapi.items.inventory.InventoryClick;
import net.flintmc.mcapi.items.inventory.InventoryController;

/**
 * This event will be fired whenever the player clicks into the inventory. It will also be fired by
 * {@link InventoryController#performClick(InventoryClick, int)} and in both the PRE and POST
 * phases, but cancellation will only have an effect in the PRE phase.
 *
 * @see Subscribe
 */
public interface InventoryClickEvent extends InventoryEvent, InventorySlotEvent, Cancellable {

  /**
   * Retrieves the item that has been clicked by the player.
   *
   * <p>In the POST phase, this will be the item after the action, so if the type was {@link
   * InventoryClick#DROP_ALL}, it would be an air stack.
   *
   * <p>For example for {@link InventoryClick#PICKUP_ALL} it would be the item that has been picked
   * up in the PRE phase and air in the POST phase or if an item has been placed the item in the
   * POST phase and air in the PRE phase.
   *
   * @return The clicked item or {@code null} if no slot has been clicked (e.g. outside of the
   *     inventory when {@link #getClickType()} == {@link InventoryClick#DROP}
   */
  ItemStack getClickedItem();

  /**
   * Retrieves the type of click that has been performed by the client.
   *
   * @return The non-null type of click
   */
  InventoryClick getClickType();

  /** Factory for the {@link InventoryClickEvent}. */
  @AssistedFactory(InventoryClickEvent.class)
  interface Factory {

    /**
     * Creates a new {@link InventoryClickEvent} with the given values.
     *
     * @param inventory The non-null inventory where the click has happened
     * @param slot The slot in the inventory where the click has happened or {@code -1} if no slot
     *     has been clicked (e.g. outside of the inventory when the clickType is {@link
     *     InventoryClick#DROP})
     * @param clickType The non-null type of click performed by the player
     * @return The new non-null event
     */
    InventoryClickEvent create(
        @Assisted("inventory") Inventory inventory,
        @Assisted("slot") int slot,
        @Assisted("clickType") InventoryClick clickType);
  }
}
