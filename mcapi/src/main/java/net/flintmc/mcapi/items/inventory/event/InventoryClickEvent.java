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
import net.flintmc.framework.eventbus.event.subscribe.Subscribe.Phase;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.items.ItemStack;
import net.flintmc.mcapi.items.inventory.Inventory;
import net.flintmc.mcapi.items.inventory.InventoryClick;
import net.flintmc.mcapi.items.inventory.InventoryController;

/**
 * This event will be fired whenever the player clicks into the inventory. It will also be fired by
 * {@link InventoryController#performClick(InventoryClick, int)} and in both the PRE and POST
 * phases, but cancellation will only have an effect in the PRE phase. In the POST phase, the item
 * will be the new item after the click has taken effect.
 *
 * @see Subscribe
 */
@Subscribable({Phase.PRE, Phase.POST})
public interface InventoryClickEvent
    extends Event, InventoryEvent, InventorySlotEvent, Cancellable {

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
   * inventory when {@link #getClickType()} == {@link InventoryClick#DROP}
   */
  ItemStack getClickedItem();

  /**
   * Retrieves the type of click that has been performed by the client.
   *
   * @return The non-null type of click
   */
  InventoryClick getClickType();

  /**
   * Factory for the {@link InventoryClickEvent}.
   */
  @AssistedFactory(InventoryClickEvent.class)
  interface Factory {

    /**
     * Creates a new {@link InventoryClickEvent} with the given values.
     *
     * @param inventory The non-null inventory where the click has happened
     * @param slot      The slot in the inventory where the click has happened or {@code -1} if no
     *                  slot has been clicked (e.g. outside of the inventory when the clickType is
     *                  {@link InventoryClick#DROP})
     * @param clickType The non-null type of click performed by the player
     * @return The new non-null event
     */
    InventoryClickEvent create(
        @Assisted("inventory") Inventory inventory,
        @Assisted("slot") int slot,
        @Assisted("clickType") InventoryClick clickType);
  }
}
