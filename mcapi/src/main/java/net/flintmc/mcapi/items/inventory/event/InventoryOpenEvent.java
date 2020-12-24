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

  /**
   * Factory for the {@link InventoryOpenEvent}.
   */
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
