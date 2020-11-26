package net.flintmc.mcapi.items.inventory.event;

import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.event.DirectionalEvent;
import net.flintmc.mcapi.items.inventory.Inventory;
import net.flintmc.mcapi.items.inventory.player.PlayerInventory;

/**
 * This event will be fired whenever an inventory is opened in the client. It can either be opened
 * by the server ({@link DirectionalEvent#getDirection()} = {@link Direction#RECEIVE}), or by the
 * client ({@link DirectionalEvent#getDirection()} = {@link Direction#SEND}). In both cases it will
 * be fired in the PRE and POST phases, but cancellation will only have an effect in the PRE phase
 * with the direction {@link Direction#SEND}. If it has been cancelled in this state, the inventory
 * will not be opened and the input will be ignored.
 *
 * <p>Note that the client can only open a {@link PlayerInventory}, other inventories are all sent
 * by the server and therefore cancellation won't work for them.
 *
 * @see Subscribe
 */
public interface InventoryOpenEvent extends InventoryEvent, DirectionalEvent {

  /** Factory for the {@link InventoryOpenEvent}. */
  @AssistedFactory(InventoryOpenEvent.class)
  interface Factory {

    /**
     * Creates a new {@link InventoryOpenEvent} with the given inventory and direction.
     *
     * @param inventory The non-null inventory where the event has happened
     * @param direction How the event has been invoked, {@link Direction#SEND} means that the client
     *     has opened the inventory (only possible for the {@link PlayerInventory}) and {@link
     *     Direction#RECEIVE} that the server has opened it
     * @return The new event
     */
    InventoryOpenEvent create(
        @Assisted("inventory") Inventory inventory, @Assisted("direction") Direction direction);
  }
}
