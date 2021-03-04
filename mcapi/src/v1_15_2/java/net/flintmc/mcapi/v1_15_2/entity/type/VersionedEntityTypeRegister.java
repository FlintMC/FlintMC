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

package net.flintmc.mcapi.v1_15_2.entity.type;

import com.beust.jcommander.internal.Maps;

import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;

import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.entity.type.EntityType;
import net.flintmc.mcapi.entity.type.EntityTypeMapper;
import net.flintmc.mcapi.entity.type.EntityTypeRegister;
import net.flintmc.render.gui.event.OpenGLInitializeEvent;
import net.minecraft.util.registry.Registry;

/**
 * 1.15.2 implementation of the {@link EntityTypeRegister}.
 */
@Singleton
@Implement(value = EntityTypeRegister.class)
public class VersionedEntityTypeRegister implements EntityTypeRegister {

  private final EntityTypeMapper entityTypeMapper;
  private final Map<String, EntityType> entityTypes;

  @Inject
  private VersionedEntityTypeRegister(EntityTypeMapper entityTypeMapper) {
    this.entityTypeMapper = entityTypeMapper;
    this.entityTypes = Maps.newHashMap();
    for (net.minecraft.entity.EntityType<?> entityType : Registry.ENTITY_TYPE) {
      String key = Registry.ENTITY_TYPE.getKey(entityType).getPath();
      this.entityTypes.put(key, this.entityTypeMapper.fromMinecraftEntityType(entityType));
    }
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public Map<String, EntityType> getEntityTypes() {
    return this.entityTypes;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public EntityType getEntityType(String key) {
    return this.entityTypes.get(key);
  }
}
