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

/**
 * An enumeration of all inventory clicks.
 */
public enum InventoryClick {

  /**
   * Drop all items on the selected slot.
   *
   * <p>The default keybind is Ctrl + Q (Command + Q on Mac).
   */
  DROP_ALL,

  /**
   * Drop one item on the selected slot.
   *
   * <p>The default keybind is Q.
   */
  DROP,

  /**
   * Clone the items on a specific slot in the creative mode.
   *
   * <p>The default keybind is Middle Mouse Button.
   */
  CLONE,

  /**
   * Pickup all items on a specific slot.
   *
   * <p>The default keybind is Left Mouse Button.
   */
  PICKUP_ALL,

  /**
   * Pickup half of the items on a specific slot.
   *
   * <p>The default keybind is Right Mouse Button.
   */
  PICKUP_HALF,

  /**
   * Move all items on a specific slot into another inventory.
   *
   * <p>The default keybind is Left and Right Mouse Button.
   */
  MOVE,

  /**
   * Collect one stack (or less, depending on the contents of the inventory) of a specific item in
   * an inventory.
   *
   * <p>The default keybind is Double Left Mouse Button.
   */
  MERGE_ALL
}
