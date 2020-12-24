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

package net.flintmc.mcapi.entity.ai;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.entity.Entity;
import net.flintmc.mcapi.entity.MobEntity;

/**
 * Represents the Minecraft entity senses.
 */
public interface EntitySenses {

  /**
   * Clears every tick all lists.
   */
  void tick();

  /**
   * Whether the entity senses can see an entity.
   *
   * @param entity The entity.
   * @return {@code true} if the entity senses can see an entity.
   */
  boolean canSeeEntity(Entity entity);

  /**
   * A factory class for the {@link EntitySenses}.
   */
  @AssistedFactory(EntitySenses.class)
  interface Factory {

    /**
     * Creates a new {@link EntitySenses} from the given {@link MobEntity}.
     *
     * @param entity The mob entity.
     * @return A created entity senses.
     */
    EntitySenses create(@Assisted("mobEntity") MobEntity entity);
  }
}
