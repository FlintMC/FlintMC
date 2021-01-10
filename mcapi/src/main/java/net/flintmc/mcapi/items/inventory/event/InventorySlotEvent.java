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

import com.google.inject.name.Named;
import net.flintmc.framework.eventbus.event.Event;
import net.flintmc.framework.eventbus.event.subscribe.Subscribable;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe.Phase;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.items.inventory.Inventory;

/**
 * The base event for everything that happens inside an inventory on a specified slot.
 *
 * @see Subscribe
 */
@Subscribable({Phase.PRE, Phase.POST})
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

  /**
   * Factory for the {@link InventorySlotEvent}.
   */
  @AssistedFactory(InventorySlotEvent.class)
  interface Factory {

    /**
     * Creates a new {@link InventorySlotEvent} with the given inventory and slot.
     *
     * @param inventory The non-null inventory where the event has happened
     * @param slot      The slot where this event has happened or {@code -1} if it happened outside
     *                  of any slot. If the slot isn't {@code -1}, {@link Inventory#getItem(int)}
     *                  with the given inventory and slot has to work.
     * @return The new event
     */
    InventorySlotEvent create(
        @Assisted("inventory") Inventory inventory, @Assisted("slot") int slot);
  }
}
