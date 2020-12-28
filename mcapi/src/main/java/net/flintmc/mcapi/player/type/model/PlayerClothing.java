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

package net.flintmc.mcapi.player.type.model;

/**
 * An enumeration of all available articles of clothing.
 */
public enum PlayerClothing {

  /**
   * The cloak of this player.
   */
  CLOAK(0, "cape"),
  /**
   * The jacket of this player.
   */
  JACKET(1, "jacket"),
  /**
   * The left sleeve of this player.
   */
  LEFT_SLEEVE(2, "left_sleeve"),
  /**
   * The right sleeve of this player.
   */
  RIGHT_SLEEVE(3, "right_sleeve"),
  /**
   * The left pants leg of this player.
   */
  LEFT_PANTS_LEG(4, "left_pants_leg"),
  /**
   * The right pants leg of this player.
   */
  RIGHT_PANTS_LEG(5, "right_pants_leg"),
  /**
   * The hat of this player.
   */
  HAT(6, "hat");

  private final int clothingId;
  private final int clothingMask;
  private final String clothingName;

  PlayerClothing(int clothingId, String clothingName) {
    this.clothingId = clothingId;
    this.clothingMask = 1 << clothingId;
    this.clothingName = clothingName;
  }

  /**
   * Retrieves the identifier of this clothing.
   *
   * @return The identifier of this clothing.
   */
  public int getClothingId() {
    return clothingId;
  }

  /**
   * Retrieves the mask of this clothing.
   *
   * @return The mask of this clothing.
   */
  public int getClothingMask() {
    return clothingMask;
  }

  /**
   * Retrieves the name of this clothing.
   *
   * @return The name of this clothing.
   */
  public String getClothingName() {
    return clothingName;
  }
}
