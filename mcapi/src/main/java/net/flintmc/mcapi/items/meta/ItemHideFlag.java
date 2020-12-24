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

package net.flintmc.mcapi.items.meta;

/**
 * Flags to be hidden on an item.
 */
public enum ItemHideFlag {

  /**
   * Flag to hide all enchantments of an item.
   */
  ENCHANTMENTS(),

  /**
   * Flag to hide all attributes like Damage, Armor points, etc.
   */
  ATTRIBUTES(),

  /**
   * Flag to hide that an item is unbreakable.
   */
  UNBREAKABLE(),

  /**
   * Flag to hide the blocks that can be destroyed with an item.
   */
  CAN_DESTROY(),

  /**
   * Flag to hide the blocks that an item can be placed on.
   */
  CAN_PLACE_ON(),

  /**
   * Flag to hide the potion effects of an item.
   */
  POTION_EFFECTS();

  private final byte modifier;

  ItemHideFlag() {
    this.modifier = (byte) (1 << super.ordinal());
  }

  /**
   * Retrieves the modifier which is used to generate and read the value in the NBT of the item.
   *
   * @return The modifier
   */
  public byte getModifier() {
    return this.modifier;
  }
}
