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

package net.flintmc.mcapi.world.type.difficulty;

/**
 * An enumeration of all available difficulties.
 */
public enum Difficulty {

  /**
   * Everything lives in harmony and peace with each other and world hunger was defeated.
   */
  PEACEFUL,
  /**
   * Hostile mobs spawn, but they deal less damage than on {@link #NORMAL} difficulty. The hunger
   * bar can deplete damaging the player until they are left with 5 hearts if it drains completely.
   */
  EASY,
  /**
   * Hostile mobs spawn and deal standard damage. The hunger bar can deplete, damaging the player
   * until they are left with a half-heart if it drains completely.
   */
  NORMAL,
  /**
   * Hostile mobs deal more damage than on {@link #NORMAL} difficulty. The hunger bar can deplete,
   * damaging the player until they die if it drains completely.
   */
  HARD;

  /**
   * Retrieves the identifier of this difficulty.
   *
   * @return The identifier of this difficulty.
   */
  public int getId() {
    return this.ordinal();
  }
}
