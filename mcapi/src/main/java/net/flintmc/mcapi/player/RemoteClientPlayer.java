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

package net.flintmc.mcapi.player;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.entity.type.EntityType;

/**
 * Represents the remote client player.
 */
public interface RemoteClientPlayer extends PlayerEntity, BaseClientPlayer {

  /**
   * {@inheritDoc}
   */
  @Override
  default boolean isSpectator() {
    return false;
  }

  /**
   * A factory class for the {@link RemoteClientPlayer}.
   */
  @AssistedFactory(RemoteClientPlayer.class)
  interface Factory {

    /**
     * Creates a new {@link RemoteClientPlayer} with the given parameters.
     *
     * @param entity     The non-null Minecraft entity.
     * @param entityType The entity type.
     * @return A created {@link RemoteClientPlayer}.
     */
    RemoteClientPlayer create(
        @Assisted("entity") Object entity, @Assisted("entityType") EntityType entityType);
  }

  /**
   * Service interface for creating {@link RemoteClientPlayer}'s.
   */
  interface Provider {

    /**
     * Creates a new {@link RemoteClientPlayer} with the given parameter.
     *
     * @param entity The non-null Minecraft entity.
     * @return A created {@link RemoteClientPlayer}.
     */
    RemoteClientPlayer get(Object entity);
  }
}
