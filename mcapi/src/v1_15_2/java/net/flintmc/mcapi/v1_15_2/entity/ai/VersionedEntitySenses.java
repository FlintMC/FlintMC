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

package net.flintmc.mcapi.v1_15_2.entity.ai;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.entity.Entity;
import net.flintmc.mcapi.entity.MobEntity;
import net.flintmc.mcapi.entity.ai.EntitySenses;
import net.flintmc.mcapi.entity.mapper.EntityFoundationMapper;

/**
 * 1.15.2 implementation of the {@link EntitySenses}.
 */
@Implement(value = EntitySenses.class)
public class VersionedEntitySenses extends net.minecraft.entity.ai.EntitySenses
    implements EntitySenses {

  private final EntityFoundationMapper entityFoundationMapper;

  @AssistedInject
  private VersionedEntitySenses(
      EntityFoundationMapper entityFoundationMapper, @Assisted("mobEntity") MobEntity entity) {
    super(
        (net.minecraft.entity.MobEntity)
            entityFoundationMapper.getEntityMapper().toMinecraftMobEntity(entity));
    this.entityFoundationMapper = entityFoundationMapper;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean canSeeEntity(Entity entity) {
    return this.canSee(
        (net.minecraft.entity.Entity)
            this.entityFoundationMapper.getEntityMapper().toMinecraftEntity(entity));
  }
}
