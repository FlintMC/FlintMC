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

package net.flintmc.mcapi.v1_16_5.entity;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.entity.CreatureEntity;
import net.flintmc.mcapi.entity.ai.EntitySenses;
import net.flintmc.mcapi.entity.mapper.EntityFoundationMapper;
import net.flintmc.mcapi.entity.type.EntityType;
import net.flintmc.mcapi.world.World;
import net.flintmc.mcapi.world.math.BlockPosition;
import net.minecraft.util.math.BlockPos;

/**
 * 1.16.5 implementation of the {@link CreatureEntity}.
 */
@Implement(CreatureEntity.class)
public class VersionedCreatureEntity extends VersionedMobEntity implements CreatureEntity {

  private final net.minecraft.entity.CreatureEntity creatureEntity;

  @AssistedInject
  public VersionedCreatureEntity(
      @Assisted("entity") Object entity,
      @Assisted("entityType") EntityType entityType,
      World world,
      EntityFoundationMapper entityFoundationMapper,
      EntitySenses.Factory entitySensesFactory) {
    super(entity, entityType, world, entityFoundationMapper, entitySensesFactory);

    if (!(entity instanceof net.minecraft.entity.CreatureEntity)) {
      throw new IllegalArgumentException(
          entity.getClass().getName()
              + " is not an instance of "
              + net.minecraft.entity.CreatureEntity.class.getName());
    }
    this.creatureEntity = (net.minecraft.entity.CreatureEntity) entity;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getBlockPathWeight(BlockPosition position) {
    return this.creatureEntity.getBlockPathWeight(
        (BlockPos) this.getWorld().toMinecraftBlockPos(position));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasPath() {
    return this.creatureEntity.hasPath();
  }
}
