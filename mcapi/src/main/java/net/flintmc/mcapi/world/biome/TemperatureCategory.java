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

package net.flintmc.mcapi.world.biome;

/**
 * Categories of temperatures in a biome.
 *
 * @see Biome#getTemperatureCategory()
 */
public enum TemperatureCategory {

  /**
   * This biome is an ocean biome and has ocean-like temperatures, those are for example the frozen
   * ocean and the lukewarm ocean biomes.
   */
  OCEAN,

  /**
   * This is a pretty cold biome, maybe also with snow in it, those are for example the snowy taiga
   * mountains and ice spikes biomes.
   */
  COLD,

  /**
   * The medium biome temperature, those are for example the giant tree taiga hills and taiga
   * biomes.
   */
  MEDIUM,

  /**
   * The warm biome temperature, those are for example the nether and desert biomes.
   */
  WARM

}
