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

package net.flintmc.mcapi.entity;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.entity.type.EntityType;
import net.flintmc.mcapi.world.math.BlockPosition;

/**
 * Represents the Minecraft creature entity.
 */
public interface CreatureEntity extends MobEntity {

  /**
   * Retrieves the block path weight of the creature entity.
   *
   * @param position The block position of the entity.
   * @return The block path weight of the creature entity.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  float getBlockPathWeight(BlockPosition position);

  /**
   * Whether the creature entity has a path.
   *
   * @return {@code true} if the creature entity has a path, otherwise {@code false}.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  boolean hasPath();

  /**
   * A factory class for the {@link CreatureEntity}.
   */
  @AssistedFactory(CreatureEntity.class)
  interface Factory {

    /**
     * Creates a new {@link CreatureEntity} with the given parameters.
     *
     * @param entity     The entity.
     * @param entityType The type of the entity.
     * @return A created creature entity.
     */
    CreatureEntity create(
        @Assisted("entity") Object entity, @Assisted("entityType") EntityType entityType);
  }

  /**
   * Service interface for creating {@link CreatureEntity}'s.
   */
  interface Provider {

    /**
     * Creates a new {@link CreatureEntity}.
     *
     * @param entity The entity.
     * @return A created creature entity.
     */
    CreatureEntity get(Object entity);
  }
}
