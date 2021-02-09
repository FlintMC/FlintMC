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

package net.flintmc.mcapi.entity.type;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.entity.Entity;
import net.flintmc.mcapi.entity.EntitySize;

/**
 * Represents the entity type.
 */
public interface EntityType {

  /**
   * Retrieves the display name of this entity type, usually a translation component.
   *
   * @return The non-null display name of this entity type
   */
  ChatComponent getDisplayName();

  /**
   * Retrieves the classification of this entity type.
   *
   * @return The entity type classification.
   */
  Entity.Classification getClassification();

  /**
   * Whether the entity type is serializable.
   *
   * @return {@code true} if the type is serializable, otherwise {@code false}.
   */
  boolean isSerializable();

  /**
   * Whether the entity type is summonable.
   *
   * @return {@code true} if the type is summonable, otherwise {@code false}.
   */
  boolean isSummonable();

  /**
   * Whether the entity type is immune to fire.
   *
   * @return {@code true} if the type is immune to fire, otherwise {@code false}.
   */
  boolean isImmuneToFire();

  /**
   * Whether the entity type can spawn far from the player.
   *
   * @return {@code true} if the type can spawn far from the player, otherwise {@code false}.
   */
  boolean canSpawnFarFromPlayer();

  /**
   * Retrieves the size of this entity type.
   *
   * @return The size of this entity type.
   */
  EntitySize getSize();

  /**
   * A factory class for the {@link EntityType}
   */
  @AssistedFactory(EntityType.class)
  interface Factory {

    /**
     * Creates a new {@link EntityType} by the given parameters.
     *
     * @param displayName           The non-null display name of this entity type.
     * @param classification        The classifications for an entity type.
     * @param serializable          Whether the entity type if serializable.
     * @param summonable            Whether the entity type is summonable.
     * @param immuneToFire          Whether the entity type is immune to fire.
     * @param canSpawnFarFromPlayer Whether the entity type can spawn far from player.
     * @param entitySize            The size of the entity type.
     * @return The created entity type.
     */
    EntityType create(
        @Assisted("displayName") ChatComponent displayName,
        @Assisted("classification") Entity.Classification classification,
        @Assisted("serializable") boolean serializable,
        @Assisted("summonable") boolean summonable,
        @Assisted("immuneToFire") boolean immuneToFire,
        @Assisted("canSpawnFarFromPlayer") boolean canSpawnFarFromPlayer,
        @Assisted("entitySize") EntitySize entitySize);
  }
}
