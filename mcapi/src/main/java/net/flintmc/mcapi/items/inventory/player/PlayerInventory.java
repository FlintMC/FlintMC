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

package net.flintmc.mcapi.items.inventory.player;

import net.flintmc.mcapi.items.ItemStack;
import net.flintmc.mcapi.items.inventory.EquipmentSlotType;
import net.flintmc.mcapi.items.inventory.Inventory;

public interface PlayerInventory extends Inventory {

  /**
   * Retrieves a part of the armor of this inventory.
   *
   * @param part The non-null part to get the armor from
   * @return The non-null item stack in the given slot
   */
  ItemStack getArmorPart(PlayerArmorPart part);

  /**
   * Retrieves an item from the player's hand.
   *
   * @param hand The hand to get the item from
   * @return The non-null item stack in the given hand
   */
  ItemStack getItemInHand(PlayerHand hand);

  int getHeldItemSlot();

  void setHeldItemSlot(int slot);

  /**
   * Retrieves the type of a specific slot in this inventory.
   *
   * @param slot The slot to get the type from
   * @return The type of the given slot or {@code null} if the given slot is no special type
   * @throws IndexOutOfBoundsException If the slot is either smaller than 0, or greater or equal to
   *                                   the highest possible slot in this inventory
   */
  EquipmentSlotType getSlotType(int slot);

  /**
   * Retrieves an item in a specific slot in this inventory.
   *
   * @param slot The slot to get the item from. Slots from 0 to 4 are the crafting inventory. Slots
   *             from 5 to 8 are the armor. Slots from 9 to 35 are the main inventory. Slots from 36
   *             to 44 are the hotbar. 45 is the off hand.
   * @return The non-null item out of this slot
   * @throws IndexOutOfBoundsException If the slot is either smaller than 0, or greater or equal to
   *                                   the highest possible slot in this inventory
   */
  @Override
  ItemStack getItem(int slot) throws IndexOutOfBoundsException;

  /**
   * Retrieves an item in a specific slot in this inventory.
   *
   * @param slotType The non-null unique type of slot to get the item from
   * @return The non-null item out of this slot
   */
  ItemStack getItem(EquipmentSlotType slotType);

  /**
   * Retrieves the slot from the player's hand.
   *
   * @param hand The hand to get the slot from
   * @return The slot from the given hand
   */
  int getHandSlot(PlayerHand hand);

  /**
   * Whether the player has this hand.
   *
   * @param hand The hand to check
   * @return {@code true} when the player has the given hand, otherwise {@code false}
   */
  boolean hasHand(PlayerHand hand);

  /**
   * Retrieves the item that is currently held on the cursor by the player.
   *
   * @return The item on the cursor or {@code null} if the player has no item on the cursor
   */
  ItemStack getCursor();

  /**
   * Closes the currently opened inventory in the client. If no inventory is opened, this method
   * does nothing.
   */
  void closeInventory();
}
