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

package net.flintmc.mcapi.player.type.hand;

/**
 * An enumeration of all available hands.
 */
public enum Hand {
  /**
   * The main-hand of a player
   */
  MAIN_HAND,
  /**
   * The off-hand of a player
   */
  OFF_HAND;

  /**
   * An enumeration of all available hand sides.
   */
  public enum Side {
    LEFT,
    RIGHT;

    /**
     * Retrieves the opposite hand.
     *
     * @return The opposite hand.
     */
    public Side opposite() {
      return this == LEFT ? RIGHT : LEFT;
    }
  }
}
