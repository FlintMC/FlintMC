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

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.entity.type.EntityType;

/**
 * Represents the spawn rate of a specific entity type in a {@link Biome}.
 *
 * @see Biome#getSpawnRates()
 */
public interface BiomeEntitySpawnRate {

  /**
   * Retrieves the entity type which is represented by this spawn rate.
   *
   * @return The non-null entity type of this spawn rate
   */
  EntityType getEntityType();

  /**
   * Retrieves the weight of this spawn rate which is used to get a random value whether an entity
   * should be spawned in a tick.
   *
   * @return The weight of this spawn rate, can be any integer
   */
  int getWeight();

  /**
   * Retrieves the minimum count of groups of entities that should be spawned at the same time.
   *
   * @return The minimum group count of this spawn rate
   */
  int getMinGroupCount();

  /**
   * Retrieves the maximum count of groups of entities that should be spawned at the same time.
   *
   * @return The maximum group count of this spawn rate
   */
  int getMaxGroupCount();

  /**
   * Factory for the {@link BiomeEntitySpawnRate}.
   */
  @AssistedFactory(BiomeEntitySpawnRate.class)
  interface Factory {

    /**
     * Creates a new {@link BiomeEntitySpawnRate}.
     *
     * @param entityType    The non-null entity type of this spawn rate
     * @param weight        The weight of this spawn rate, can be any integer
     * @param minGroupCount The minimum group count of this spawn rate
     * @param maxGroupCount The maximum group count of this spawn rate
     * @return The new non-null {@link BiomeEntitySpawnRate}
     */
    BiomeEntitySpawnRate create(
        @Assisted EntityType entityType,
        @Assisted("weight") int weight,
        @Assisted("minGroupCount") int minGroupCount,
        @Assisted("maxGroupCount") int maxGroupCount);

  }

}
