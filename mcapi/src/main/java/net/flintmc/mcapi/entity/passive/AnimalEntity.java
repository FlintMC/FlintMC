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
import net.flintmc.mcapi.entity.AgeableEntity;
import net.flintmc.mcapi.entity.type.EntityType;
import net.flintmc.mcapi.items.ItemStack;
import net.flintmc.mcapi.player.PlayerEntity;

/**
 * Represents the Minecraft animal entity.
 */
public interface AnimalEntity extends AgeableEntity {

  /**
   * Whether the given {@link ItemStack} is the breeding item for this animal entity.
   *
   * @param breedingItem The breeding item to be checked.
   * @return {@code true} if the given item stack is the breeding item for this animal entity,
   * otherwise {@code false}.
   */
  boolean isBreedingItem(ItemStack breedingItem);

  /**
   * Whether the animal entity can breed.
   *
   * @return {@code true} if the animal entity can breed, otherwise {@code false}.
   */
  boolean canBreed();

  /**
   * Whether the animal entity is in love.
   *
   * @return {@code true} if the animal entity is in love, otherwise {@code false}.
   */
  boolean isInLove();

  /**
   * Changes the ticks, how long the animal being is in love.
   *
   * @param ticks The ticks how long the animal being is in love.
   */
  void setInLove(int ticks);

  /**
   * Changes the cupid of the animal entity.
   *
   * @param player The new cupid.
   */
  void setInLove(PlayerEntity player);

  /**
   * Resets the love of the animal entity.
   */
  void resetInLove();

  /**
   * Whether this animal entity can mate with the given animal.
   *
   * @param entity The animal to be checked.
   * @return {@code true} if this animal entity can mate with the given animal, otherwise {@code
   * false}.
   */
  boolean canMateWith(AnimalEntity entity);

  /**
   * A factory class for the {@link AnimalEntity}.
   */
  @AssistedFactory(AnimalEntity.class)
  interface Factory {

    /**
     * Creates a new {@link AnimalEntity} with the given parameters.
     *
     * @param entity     The entity.
     * @param entityType The type of the entity.
     * @return A created animal entity.
     */
    AnimalEntity create(
        @Assisted("entity") Object entity, @Assisted("entityType") EntityType entityType);
  }

  /**
   * Service interface for creating {@link AnimalEntity}'s.
   */
  interface Provider {

    /**
     * Creates a new {@link AnimalEntity} with the given entity.
     *
     * @param entity The entity.
     * @return A created animal entity.
     * @see Factory#create(Object, EntityType)
     */
    AnimalEntity get(Object entity);
  }
}
