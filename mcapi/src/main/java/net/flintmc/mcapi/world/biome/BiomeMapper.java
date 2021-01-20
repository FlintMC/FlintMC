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
 * Mapper between Flint biomes to and Minecraft biomes.
 *
 * @see Biome
 */
public interface BiomeMapper {

  /**
   * Maps the given Minecraft category to the Flint biome category.
   *
   * @param category The non-null Minecraft category to be mapped
   * @return The non-null Flint category matching the given Minecraft category
   * @see #toMinecraftBiomeCategory(BiomeCategory)
   */
  BiomeCategory fromMinecraftBiomeCategory(Object category);

  /**
   * Maps the given Flint category to the Minecraft biome category.
   *
   * @param category The non-null Flint category to be mapped
   * @return The non-null Minecraft category matching the given Flint category
   * @see #fromMinecraftBiomeCategory(Object)
   */
  Object toMinecraftBiomeCategory(BiomeCategory category);

  /**
   * Maps the given Minecraft rain type to the Flint biome rain type.
   *
   * @param rainType The non-null Minecraft rain type to be mapped
   * @return The non-null Flint rain type matching the given Minecraft rain type
   * @see #toMinecraftRainType(RainType)
   */
  RainType fromMinecraftRainType(Object rainType);

  /**
   * Maps the given Flint rain type to the Minecraft biome rain type.
   *
   * @param rainType The non-null Flint rain type to be mapped
   * @return The non-null Minecraft rain type matching the given Flint rain type
   * @see #fromMinecraftRainType(Object)
   */
  Object toMinecraftRainType(RainType rainType);

  /**
   * Maps the given Minecraft temperature category to the Flint biome temperature category.
   *
   * @param category The non-null Minecraft temperature category to be mapped
   * @return The non-null Flint temperature category matching the given Minecraft temperature
   * category
   * @see #toMinecraftTemperatureCategory(TemperatureCategory)
   */
  TemperatureCategory fromMinecraftTemperatureCategory(Object category);

  /**
   * Maps the given Flint temperature category to the Minecraft biome temperature category.
   *
   * @param category The non-null Flint temperature category to be mapped
   * @return The non-null Minecraft temperature category matching the given Flint temperature
   * category
   * @see #fromMinecraftTemperatureCategory(Object)
   */
  Object toMinecraftTemperatureCategory(TemperatureCategory category);

}
