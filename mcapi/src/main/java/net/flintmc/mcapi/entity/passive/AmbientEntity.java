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

package net.flintmc.mcapi.entity.passive;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.entity.MobEntity;
import net.flintmc.mcapi.entity.type.EntityType;

/**
 * Represents a Minecraft ambient entity.
 */
public interface AmbientEntity extends MobEntity {

  /**
   * A factory class for the {@link AmbientEntity}.
   */
  @AssistedFactory(AmbientEntity.class)
  interface Factory {

    /**
     * Creates a new {@link AmbientEntity} with the given parameters.
     *
     * @param entity     The entity.
     * @param entityType The type of the entity.
     * @return A created ambient entity.
     */
    AmbientEntity create(
        @Assisted("entity") Object entity, @Assisted("entityType") EntityType entityType);
  }

  /**
   * Service interface for creating {@link AmbientEntity}'s.
   */
  interface Provider {

    /**
     * Creates a new {@link AmbientEntity} with the given entity.
     *
     * @param entity The entity.
     * @return A created ambient entity.
     * @see AmbientEntity.Factory#create(Object, EntityType)
     */
    AmbientEntity get(Object entity);
  }
}
