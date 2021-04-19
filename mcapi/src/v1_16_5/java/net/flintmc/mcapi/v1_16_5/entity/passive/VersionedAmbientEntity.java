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

package net.flintmc.mcapi.v1_16_5.entity.passive;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.entity.ai.EntitySenses;
import net.flintmc.mcapi.entity.mapper.EntityFoundationMapper;
import net.flintmc.mcapi.entity.passive.AmbientEntity;
import net.flintmc.mcapi.entity.type.EntityType;
import net.flintmc.mcapi.player.PlayerEntity;
import net.flintmc.mcapi.v1_16_5.entity.VersionedMobEntity;
import net.flintmc.mcapi.world.World;

@Implement(AmbientEntity.class)
public class VersionedAmbientEntity extends VersionedMobEntity implements AmbientEntity {

  @AssistedInject
  public VersionedAmbientEntity(
      @Assisted("entity") Object entity,
      @Assisted("entityType") EntityType entityType,
      World world,
      EntityFoundationMapper entityFoundationMapper,
      EntitySenses.Factory entitySensesFactory) {
    super(entity, entityType, world, entityFoundationMapper, entitySensesFactory);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean canBeLeashedTo(PlayerEntity playerEntity) {
    return false;
  }
}
