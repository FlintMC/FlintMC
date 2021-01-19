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

package net.flintmc.mcapi.internal.world.biome;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.entity.type.EntityType;
import net.flintmc.mcapi.world.biome.BiomeEntitySpawnRate;

@Implement(BiomeEntitySpawnRate.class)
public class DefaultBiomeEntitySpawnRate implements BiomeEntitySpawnRate {

  private final EntityType entityType;
  private final int weight;
  private final int minGroupCount;
  private final int maxGroupCount;

  @AssistedInject
  public DefaultBiomeEntitySpawnRate(
      @Assisted EntityType entityType,
      @Assisted("weight") int weight,
      @Assisted("minGroupCount") int minGroupCount,
      @Assisted("maxGroupCount") int maxGroupCount) {
    this.entityType = entityType;
    this.weight = weight;
    this.minGroupCount = minGroupCount;
    this.maxGroupCount = maxGroupCount;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public EntityType getEntityType() {
    return this.entityType;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getWeight() {
    return this.weight;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getMinGroupCount() {
    return this.minGroupCount;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getMaxGroupCount() {
    return this.maxGroupCount;
  }
}
