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

package net.flintmc.mcapi.items.inventory;

import net.flintmc.framework.stereotype.NameSpacedKey;
import net.flintmc.mcapi.items.inventory.player.PlayerInventory;

/**
 * Represents a controller for inventories.
 */
public interface InventoryController {

  /**
   * Retrieves the opened inventory.
   *
   * @return the opened inventory.
   */
  Inventory getOpenInventory();

  /**
   * Retrieves the player's inventory.
   *
   * @return the player's inventory.
   */
  PlayerInventory getPlayerInventory();

  /**
   * Retrieves whether the player is currently looking at an inventory. The type of the inventory
   * doesn't matter.
   *
   * @return {@code true} if there is an inventory opened, otherwise {@code false}
   */
  boolean hasInventoryOpened();

  /**
   * Retrieves an array of all registered inventory types.
   *
   * @return an array of all registered inventory types.
   */
  InventoryType[] getTypes();

  /**
   * Retrieves the inventory type by the given registry name.
   *
   * @param registryName The registry name of an inventory type
   * @return an inventory with the registry name or {@code null}
   */
  InventoryType getType(NameSpacedKey registryName);

  /**
   * Registers an inventory type to the controller.
   *
   * @param type The inventory type to register
   */
  void registerType(InventoryType type);

  /**
   * Whether inventories can be opened. For example they cannot be opened when the player is not
   * ingame.
   *
   * @return {@code true} if inventories can be opened, otherwise {@code false}
   */
  boolean canOpenInventories();

  /**
   * Sends a click in this inventory in the given slot to the server.
   *
   * @param click The non-null type of click to be sent to the server
   * @param slot  The slot to be clicked or -1 to simulate a click outside of the inventory
   * @throws IllegalArgumentException If currently no inventory is opened to be clicked
   */
  void performClick(InventoryClick click, int slot);

  /**
   * Sends a hotkey press in this inventory in the given slot to the server to move a specific item
   * between two slots.
   *
   * @param hotkey The hotkey from 0 - 8 to be pressed
   * @param slot   The target slot to simulate the hotkey press
   * @throws IndexOutOfBoundsException If the slot is either smaller than 0, or greater or equal to
   *                                   the highest possible slot in this inventory
   * @throws IllegalArgumentException  If the given hotkey is not in the range from 0 - 8
   * @throws IllegalArgumentException  If currently no inventory is opened to be clicked
   */
  void performHotkeyPress(int hotkey, int slot) throws IndexOutOfBoundsException;
}
