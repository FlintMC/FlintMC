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

package net.flintmc.mcapi.v1_16_4.player.provider;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.entity.type.EntityTypeRegister;
import net.flintmc.mcapi.player.PlayerEntity;

/** 1.16.4 implementation of the {@link PlayerEntity.Provider}. */
@Singleton
@Implement(value = PlayerEntity.Provider.class, version = "1.16.4")
public class VersionedPlayerEntityProvider implements PlayerEntity.Provider {

  private final PlayerEntity.Factory playerEntityFactory;
  private final EntityTypeRegister entityTypeRegister;

  @Inject
  public VersionedPlayerEntityProvider(
      PlayerEntity.Factory playerEntityFactory, EntityTypeRegister entityTypeRegister) {
    this.playerEntityFactory = playerEntityFactory;
    this.entityTypeRegister = entityTypeRegister;
  }

  /** {@inheritDoc} */
  @Override
  public PlayerEntity get(Object entity) {
    return this.playerEntityFactory.create(entity, this.entityTypeRegister.getEntityType("player"));
  }
}
