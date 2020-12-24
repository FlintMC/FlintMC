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
import net.flintmc.mcapi.player.PlayerEntity;
import net.flintmc.mcapi.player.type.hand.Hand;

/**
 * Represents the Minecraft ageable entity.
 */
public interface AgeableEntity extends CreatureEntity {

  /**
   * Whether the entity can process interactively.
   *
   * @param entity The player entity that interacts.
   * @param hand   The hand that interacts.
   * @return {@code true} if the entity can process interactively, otherwise {@code false}.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  boolean processInteract(PlayerEntity entity, Hand hand);

  /**
   * Retrieves the growing age of this ageable entity.
   *
   * @return The growing age.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  int getGrowingAge();

  /**
   * Changes the growing age of this ageable entity.
   *
   * @param age The new age.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  void setGrowingAge(int age);

  /**
   * Ages up the entity.
   *
   * @param growth          The growth in seconds.
   * @param updateForcedAge {@code true} to update the forced age, otherwise {@code false}.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  void ageUp(int growth, boolean updateForcedAge);

  /**
   * Adds growth to the ageable entity.
   *
   * @param growth The growth to be added.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  void addGrowth(int growth);

  @AssistedFactory(AgeableEntity.class)
  interface Factory {

    /**
     * Creates a new {@link AgeableEntity} with the given parameters.
     *
     * @param entity     The entity.
     * @param entityType The type of the entity.
     * @return A created ageable entity.
     */
    AgeableEntity create(
        @Assisted("entity") Object entity, @Assisted("entityType") EntityType entityType);
  }

  /**
   * Service interface for creating {@link AgeableEntity}'s.
   */
  interface Provider {

    /**
     * Creates a new {@link AgeableEntity} with the given parameters.
     *
     * @param entity The entity.
     * @return A created ageable entity.
     */
    AgeableEntity get(Object entity);
  }
}
