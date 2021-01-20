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

import com.google.common.collect.Multimap;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.entity.Entity.Classification;
import net.flintmc.mcapi.resources.ResourceLocation;
import net.flintmc.mcapi.world.math.BlockPosition;

/**
 * Represents a single biome of a world with a specific behavior like raining and snowing.
 *
 * @see BiomeRegistry
 */
public interface Biome {

  /**
   * Retrieves the resource name of this biome, unique per {@link BiomeRegistry}.
   *
   * @return The non-null resource name of this biome
   */
  ResourceLocation getName();

  /**
   * Retrieves the RGB value of the grass at the specific position in this biome.
   *
   * @param x The absolute x coordinate in the world, not in the biome
   * @param z The absolute z coordinate in the world, not in the biome
   * @return The RGB value of the grass color at the given position
   */
  int getGrassColor(double x, double z);

  /**
   * Retrieves the RGB value of the foliage in this biome.
   *
   * @return The RGB value of the foliage color at the given position
   */
  int getFoliageColor();

  /**
   * Retrieves the type of rain that can happen in this biome.
   *
   * @return The non-null rain type of this biome
   */
  RainType getRainType();

  /**
   * Retrieves the display name of this biome which usually is a translation component.
   *
   * @return The non-null display name of this biome
   */
  ChatComponent getDisplayName();

  /**
   * Retrieves the category of temperature of this biome.
   *
   * @return The non-null temperature category
   */
  TemperatureCategory getTemperatureCategory();

  /**
   * Retrieves the default temperature of this biome.
   *
   * @return The default temperature of this biome
   */
  float getDefaultTemperature();

  /**
   * Retrieves the temperature at a specific position in this biome.
   *
   * @param position The absolute position to get the temperature from
   * @return The temperature at the given position for this biome
   */
  float getTemperature(BlockPosition position);

  /**
   * Retrieves the humidity in this biome.
   *
   * @return The humidity of this biome
   */
  float getHumidity();

  /**
   * Retrieves whether or not this biome has what Minecraft considers as a high humidity.
   *
   * @return {@code true} if this biome has a high humidity, {@code false} otherwise
   */
  boolean hasHighHumidity();

  /**
   * Retrieves the RGB value of the water color in this biome.
   *
   * @return The RGB value of the water color in this biome
   */
  int getWaterColor();

  /**
   * Retrieves the RGB value of the water fog color in this biome.
   *
   * @return The RGB value of the water fog color in this biome
   */
  int getWaterFogColor();

  /**
   * Retrieves the category of this biome.
   *
   * @return The non-null category of this biome
   */
  BiomeCategory getCategory();

  /**
   * Retrieves a map of the entity spawn rates per entity classification in this biome.
   *
   * @return The non-null map of all entity spawn rates
   */
  Multimap<Classification, BiomeEntitySpawnRate> getSpawnRates();

  /**
   * Retrieves the spawn chance of mobs in this biome, defaults to 0.1. This is in not a specific
   * range but some format of Minecraft. For example in snowy biomes the spawn rate is 0.07.
   *
   * @return The spawn chance of mobs
   */
  float getMobSpawnChance();

  /**
   * Retrieves the RGB value of the sky color in this biome.
   *
   * @return The RGB value of the sky color in this biome
   */
  int getSkyColor();

  /**
   * Factory for the {@link Biome}. Only inteded to be used internally, consider using the {@link
   * BiomeRegistry} instead.
   */
  @AssistedFactory(Biome.class)
  interface Factory {

    /**
     * Creates a new {@link Biome} with the given name and Minecraft value.
     *
     * @param name   The non-null name of the new biome, unique per {@link BiomeRegistry}
     * @param handle The non-null version-specific Minecraft value of the new biome
     * @return The new non-null {@link Biome}
     */
    Biome create(@Assisted ResourceLocation name, @Assisted("handle") Object handle);

  }

}
