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

package net.flintmc.mcapi.v1_15_2.entity.provider;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.entity.LivingEntity;
import net.flintmc.mcapi.entity.type.EntityTypeMapper;

@Singleton
@Implement(value = LivingEntity.Provider.class, version = "1.15.2")
public class VersionedLivingEntityProvider implements LivingEntity.Provider {

  private final LivingEntity.Factory livingEntityFactory;
  private final EntityTypeMapper entityTypeMapper;

  @Inject
  private VersionedLivingEntityProvider(
      LivingEntity.Factory livingEntityFactory, EntityTypeMapper entityTypeMapper) {
    this.livingEntityFactory = livingEntityFactory;
    this.entityTypeMapper = entityTypeMapper;
  }

  /** {@inheritDoc} */
  @Override
  public LivingEntity get(Object entity) {
    if (!(entity instanceof net.minecraft.entity.LivingEntity)) {
      throw new IllegalArgumentException(
          entity.getClass().getName()
              + " is not an instance of "
              + net.minecraft.entity.LivingEntity.class.getName());
    }
    net.minecraft.entity.LivingEntity livingEntity = (net.minecraft.entity.LivingEntity) entity;

    return this.livingEntityFactory.create(
        livingEntity, this.entityTypeMapper.fromMinecraftEntityType(livingEntity.getType()));
  }
}
