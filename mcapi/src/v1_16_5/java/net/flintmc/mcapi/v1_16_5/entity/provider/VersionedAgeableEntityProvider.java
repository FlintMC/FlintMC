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

package net.flintmc.mcapi.v1_16_5.entity.provider;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.entity.AgeableEntity;
import net.flintmc.mcapi.entity.type.EntityTypeMapper;

/**
 * 1.15.2 implementation of the {@link AgeableEntity.Provider}.
 */
@Singleton
@Implement(value = AgeableEntity.Provider.class, version = "1.16.5")
public class VersionedAgeableEntityProvider implements AgeableEntity.Provider {

  private final AgeableEntity.Factory ageableEntityFactory;
  private final EntityTypeMapper entityTypeMapper;

  @Inject
  private VersionedAgeableEntityProvider(
      AgeableEntity.Factory ageableEntityFactory, EntityTypeMapper entityTypeMapper) {
    this.ageableEntityFactory = ageableEntityFactory;
    this.entityTypeMapper = entityTypeMapper;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public AgeableEntity get(Object entity) {
    if (!(entity instanceof net.minecraft.entity.AgeableEntity)) {
      throw new IllegalArgumentException(
          entity.getClass().getName()
              + " is not an instance of "
              + net.minecraft.entity.AgeableEntity.class.getName());
    }
    net.minecraft.entity.AgeableEntity ageableEntity = (net.minecraft.entity.AgeableEntity) entity;

    return this.ageableEntityFactory.create(
        ageableEntity, this.entityTypeMapper.fromMinecraftEntityType(ageableEntity.getType()));
  }
}
