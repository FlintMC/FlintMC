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

package net.flintmc.mcapi.v1_15_2.entity.passive.provider;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.entity.passive.AmbientEntity;
import net.flintmc.mcapi.entity.type.EntityTypeMapper;

@Singleton
@Implement(value = AmbientEntity.Provider.class, version = "1.15.2")
public class VersionedAmbientEntityProvider implements AmbientEntity.Provider {

  private final AmbientEntity.Factory ambientEntityFactory;
  private final EntityTypeMapper entityTypeMapper;

  @Inject
  private VersionedAmbientEntityProvider(
      AmbientEntity.Factory ambientEntityFactory, EntityTypeMapper entityTypeMapper) {
    this.ambientEntityFactory = ambientEntityFactory;
    this.entityTypeMapper = entityTypeMapper;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public AmbientEntity get(Object entity) {
    if (!(entity instanceof net.minecraft.entity.passive.AmbientEntity)) {
      throw new IllegalArgumentException(
          entity.getClass().getName()
              + " is not an instance of "
              + net.minecraft.entity.passive.AmbientEntity.class.getName());
    }
    net.minecraft.entity.passive.AmbientEntity ambientEntity =
        (net.minecraft.entity.passive.AmbientEntity) entity;

    return this.ambientEntityFactory.create(
        ambientEntity, this.entityTypeMapper.fromMinecraftEntityType(ambientEntity.getType()));
  }
}
