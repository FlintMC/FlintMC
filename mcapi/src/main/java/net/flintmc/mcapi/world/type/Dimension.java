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

package net.flintmc.mcapi.world.type;

/**
 * An enumeration of all available dimensions.
 */
public enum Dimension {

  /**
   * The overworld is the dimension in which all player begin their.
   */
  OVERWORLD(0),
  /**
   * The nether is a dangerous hell-like dimension.
   */
  NETHER(-1),
  /**
   * The end is a dark, space-like dimension.
   */
  THE_END(1);

  private final int id;

  /**
   * Initializes a new dimension with an identifier
   *
   * @param id The dimension identifier
   */
  Dimension(int id) {
    this.id = id;
  }

  /**
   * Retrieves the identifier of this dimension.
   *
   * @return The identifier of this dimension.
   */
  public int getId() {
    return id;
  }
}
