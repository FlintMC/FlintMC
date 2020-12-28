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

package net.flintmc.mcapi.v1_16_4.entity.provider;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.entity.MobEntity;
import net.flintmc.mcapi.entity.type.EntityTypeMapper;

/** 1.15.2 implementation of the {@link MobEntity.Provider}. */
@Singleton
@Implement(value = MobEntity.Provider.class, version = "1.15.2")
public class VersionedMobEntityProvider implements MobEntity.Provider {

  private final MobEntity.Factory mobEntityFactory;
  private final EntityTypeMapper entityTypeMapper;

  @Inject
  private VersionedMobEntityProvider(
      MobEntity.Factory mobEntityFactory, EntityTypeMapper entityTypeMapper) {
    this.mobEntityFactory = mobEntityFactory;
    this.entityTypeMapper = entityTypeMapper;
  }

  /** {@inheritDoc} */
  @Override
  public MobEntity get(Object entity) {
    if (!(entity instanceof net.minecraft.entity.MobEntity)) {
      throw new IllegalArgumentException(
          entity.getClass().getName()
              + " is not an instance of "
              + net.minecraft.entity.MobEntity.class.getName());
    }
    net.minecraft.entity.MobEntity mobEntity = (net.minecraft.entity.MobEntity) entity;

    return this.mobEntityFactory.create(
        mobEntity, this.entityTypeMapper.fromMinecraftEntityType(mobEntity.getType()));
  }
}
