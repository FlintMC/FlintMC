package net.flintmc.mcapi.items.inventory.event;

import net.flintmc.framework.eventbus.event.Cancellable;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.event.DirectionalEvent;
import net.flintmc.mcapi.items.inventory.Inventory;

/**
 * This event will be fired whenever the inventory opened in the client is being closed. It can
 * either be closed by the server ({@link DirectionalEvent#getDirection()} = {@link
 * DirectionalEvent.Direction#RECEIVE}), or by the client ({@link DirectionalEvent#getDirection()} =
 * {@link DirectionalEvent.Direction#SEND}). In both cases it will only be fired in the PRE phase,
 * but cancellation will only have an effect in the direction {@link
 * DirectionalEvent.Direction#SEND}. If it has been cancelled in this state, the inventory will not
 * be closed and the input will be ignored.
 *
 * @see Subscribe
 */
public interface InventoryCloseEvent extends InventoryEvent, DirectionalEvent, Cancellable {

  @AssistedFactory(InventoryCloseEvent.class)
  interface Factory {

    /**
     * Creates a new {@link InventoryCloseEvent} with the given inventory.
     *
     * @param inventory The non-null inventory that has been closed
     * @param direction How the event has been invoked, {@link Direction#SEND} means that the client
     *     has closed the inventory and {@link Direction#RECEIVE} that the server has closed it
     * @return The new event
     */
    InventoryCloseEvent create(
        @Assisted("inventory") Inventory inventory, @Assisted("direction") Direction direction);
  }
}
