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

/**
 * An enumeration of all available armor parts of the player.
 */
public enum PlayerArmorPart {

  /**
   * The helmet which protects the head of the player.
   */
  HELMET(3),
  /**
   * The chest plate which protects the body of the player.
   */
  CHEST_PLATE(2),
  /**
   * The leggings which protects the legs of the player.
   */
  LEGGINGS(1),
  /**
   * The boots which protects the feet of the player.
   */
  BOOTS(0);

  private final int index;

  PlayerArmorPart(int index) {
    this.index = index;
  }

  /**
   * Retrieves the index of this armor part.
   *
   * @return the index of this armor part.
   */
  public int getIndex() {
    return this.index;
  }
}
