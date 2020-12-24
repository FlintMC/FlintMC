/*
 * FlintMC
 * Copyright (C) 2020-2021 LabyMedia GmbH and contributors
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package net.flintmc.mcapi.items.inventory.event;

import net.flintmc.framework.eventbus.event.Cancellable;
import net.flintmc.framework.eventbus.event.Event;
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
public interface InventoryCloseEvent extends Event, InventoryEvent, DirectionalEvent, Cancellable {

  /**
   * Factory for the {@link InventoryCloseEvent}.
   */
  @AssistedFactory(InventoryCloseEvent.class)
  interface Factory {

    /**
     * Creates a new {@link InventoryCloseEvent} with the given inventory.
     *
     * @param inventory The non-null inventory that has been closed
     * @param direction How the event has been invoked, {@link Direction#SEND} means that the client
     *                  has closed the inventory and {@link Direction#RECEIVE} that the server has
     *                  closed it
     * @return The new event
     */
    InventoryCloseEvent create(
        @Assisted("inventory") Inventory inventory, @Assisted("direction") Direction direction);
  }
}
