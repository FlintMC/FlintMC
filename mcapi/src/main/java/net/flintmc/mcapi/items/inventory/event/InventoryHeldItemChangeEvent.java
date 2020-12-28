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
import net.flintmc.framework.eventbus.event.subscribe.Subscribable;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.items.ItemStack;
import net.flintmc.mcapi.items.inventory.player.PlayerInventory;

import static net.flintmc.framework.eventbus.event.subscribe.Subscribe.Phase;

/**
 * This event will be fired when the player changes the selected slot in their hotbar. It will also
 * be fired when changing the slot via {@link PlayerInventory#setHeldItemSlot(int)}. It will only be
 * fired in the {@link Phase#PRE} phase.
 *
 * @see Subscribe
 */
@Subscribable(Phase.PRE)
public interface InventoryHeldItemChangeEvent extends Event, Cancellable {

  /**
   * Retrieves the new slot in the hotbar that has been changed to. To get the old one, use {@link
   * PlayerInventory#getHeldItemSlot()}.
   *
   * @return The new slot in the hotbar in the range from 0 to 8
   */
  int getSlot();

  /**
   * Retrieves the item that is set on the new {@link #getSlot() slot}.
   *
   * @return The non-null item on the selected slot
   */
  ItemStack getItem();

  /** Factory for the {@link InventoryHeldItemChangeEvent}. */
  @AssistedFactory(InventoryHeldItemChangeEvent.class)
  interface Factory {

    /**
     * Creates a new {@link InventoryHeldItemChangeEvent}.
     *
     * @param slot The new slot in the hotbar in the range from 0 to 8
     * @param item The non-null item on the selected slot
     * @return The new non-null {@link InventoryHeldItemChangeEvent}.
     */
    InventoryHeldItemChangeEvent create(@Assisted int slot, @Assisted ItemStack item);
  }
}
