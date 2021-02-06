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

/**
 * Represents a builder to built entity types.
 */
public interface EntityTypeBuilder {

  EntityTypeBuilder displayName(ChatComponent displayName);

  /**
   * Sets the size of the entity type.
   *
   * @param width  The width of the entity type.
   * @param height The height of the entity type.
   * @return This builder, for chaining.
   */
  EntityTypeBuilder size(float width, float height);

  /**
   * Disables summoning for the entity type.
   *
   * @return This builder, for chaining.
   */
  EntityTypeBuilder disableSummoning();

  /**
   * Disables serialization for the entity type.
   *
   * @return This builder, for chaining.
   */
  EntityTypeBuilder disableSerialization();

  /**
   * Enables immune to fire for the entity type.
   *
   * @return This builder, for chaining.
   */
  EntityTypeBuilder immuneToFire();

  /**
   * Enables can spawn far from the player for the entity type.
   *
   * @return This builder, for chaining.
   */
  EntityTypeBuilder canSpawnFarFromPlayer();

  /**
   * Built a new entity type.
   *
   * @param id The identifier of the entity type.
   * @return The built entity type.
   */
  EntityType build(String id);

  /**
   * A factory class for {@link EntityTypeBuilder}.
   */
  @AssistedFactory(EntityTypeBuilder.class)
  interface Factory {

    /**
     * Creates a new {@link EntityTypeBuilder} with the given {@link Entity.Classification}.
     *
     * @param classification The classification for the entity builder.
     * @return A created entity type builder.
     */
    EntityTypeBuilder create(@Assisted("classification") Entity.Classification classification);
  }
}
