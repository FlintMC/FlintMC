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

/**
 * The base event for every action that happens in an inventory.
 *
 * @see Subscribe
 */
public interface InventoryEvent extends Event {

  /**
   * Retrieves the inventory where this event has happened.
   *
   * @return The non-null inventory
   */
  Inventory getInventory();

  /**
   * Factory for the {@link InventoryEvent}.
   */
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
