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

package net.flintmc.mcapi.v1_16_5.entity.type;

import com.beust.jcommander.internal.Maps;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.flintmc.framework.eventbus.event.subscribe.PreSubscribe;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.entity.type.EntityType;
import net.flintmc.mcapi.entity.type.EntityTypeMapper;
import net.flintmc.mcapi.entity.type.EntityTypeRegister;
import net.flintmc.mcapi.registry.RegistryRegisterEvent;

/**
 * 1.16.5 implementation of the {@link EntityTypeRegister}.
 */
@Singleton
@Implement(EntityTypeRegister.class)
public class VersionedEntityTypeRegister implements EntityTypeRegister {

  private final EntityTypeMapper entityTypeMapper;
  private final Map<String, EntityType> entityTypes;

  @Inject
  private VersionedEntityTypeRegister(EntityTypeMapper entityTypeMapper) {
    this.entityTypeMapper = entityTypeMapper;
    this.entityTypes = Maps.newHashMap();
  }

  @PreSubscribe
  public void registerEntityTypes(final RegistryRegisterEvent event) {
    if (!event.getRegistryKeyLocation().getPath().equalsIgnoreCase("entity_type")) {
      return;
    }

    this.entityTypes.put(event.getRegistryValueLocation().getPath(),
        this.entityTypeMapper.fromMinecraftEntityType(event.getRegistryObject()));
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
