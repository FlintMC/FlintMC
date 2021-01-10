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
import net.flintmc.framework.eventbus.event.subscribe.Subscribable;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe.Phase;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.items.ItemStack;
import net.flintmc.mcapi.items.inventory.Inventory;

/**
 * This event will be fired when an item in the inventory gets updated. It will only be fired in
 * both the {@link Phase#PRE} and {@link Phase#POST} phases.
 *
 * @see Subscribe
 */
@Subscribable({Phase.PRE, Phase.POST})
public interface InventoryUpdateSlotEvent extends Event, InventorySlotEvent, InventoryEvent {

  /**
   * Retrieves the new item that is now set on the slot that has been updated.
   *
   * @return The non-null item from the update
   */
  ItemStack getNewItem();

  /**
   * Factory for the {@link InventoryUpdateSlotEvent}.
   */
  @AssistedFactory(InventoryUpdateSlotEvent.class)
  interface Factory {

    /**
     * Creates a new {@link InventoryUpdateSlotEvent}.
     *
     * @param inventory The non-null inventory in which the slot has been updated
     * @param slot      The slot that has been updated
     * @param newItem   The non-null item that is now set after the update
     * @return The new non-null {@link InventoryUpdateSlotEvent}.
     */
    InventoryUpdateSlotEvent create(
        @Assisted("inventory") Inventory inventory,
        @Assisted("slot") int slot,
        @Assisted("newItem") ItemStack newItem);
  }
}
