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

package net.flintmc.mcapi.v1_16_5.player.provider;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.entity.type.EntityTypeRegister;
import net.flintmc.mcapi.player.RemoteClientPlayer;

/**
 * 1.16.5 implementation of the {@link RemoteClientPlayer.Provider}.
 */
@Singleton
@Implement(value = RemoteClientPlayer.Provider.class)
public class VersionedRemoteClientPlayerEntityProvider implements RemoteClientPlayer.Provider {

  private final RemoteClientPlayer.Factory remoteClientPlayerEntity;
  private final EntityTypeRegister entityTypeRegister;

  @Inject
  private VersionedRemoteClientPlayerEntityProvider(
      RemoteClientPlayer.Factory remoteClientPlayerEntity, EntityTypeRegister entityTypeRegister) {
    this.remoteClientPlayerEntity = remoteClientPlayerEntity;
    this.entityTypeRegister = entityTypeRegister;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public RemoteClientPlayer get(Object entity) {
    return this.remoteClientPlayerEntity.create(
        entity, this.entityTypeRegister.getEntityType("player"));
  }
}
