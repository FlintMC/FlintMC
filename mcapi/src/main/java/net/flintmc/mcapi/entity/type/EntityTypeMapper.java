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

import net.flintmc.mcapi.entity.Entity;
import net.flintmc.mcapi.entity.EntitySize;

/**
 * Mapper between the Minecraft entity type and Flint entity type.
 */
public interface EntityTypeMapper {

  /**
   * Creates a new {@link EntityType} by using the given Minecraft entity type as the base.
   *
   * @param handle The non-null Minecraft entity type.
   * @return The new Flint {@link EntityType} or {@code null}, if the given entity type was invalid.
   * @throws IllegalArgumentException If the given object is no Minecraft entity type.
   */
  EntityType fromMinecraftEntityType(Object handle);

  /**
   * Creates a new Minecraft entity classification by using the Flint {@link Entity.Classification}
   * as the base.
   *
   * @param classification The non-null Flint {@link Entity.Classification}.
   * @return The new Minecraft entity classification or {@code null}, if the given entity
   * classification was invalid.
   */
  Object toMinecraftEntityClassification(Entity.Classification classification);

  /**
   * Creates a new {@link Entity.Classification} by using the given Minecraft entity classification
   * as the base.
   *
   * @param handle The non-null Minecraft entity classification.
   * @return The new Flint {@link Entity.Classification} or {@code null}, if the given entity
   * classification was invalid.
   * @throws IllegalArgumentException If the given object is no Minecraft entity classification.
   */
  Entity.Classification fromMinecraftEntityClassification(Object handle);

  /**
   * Creates a new Minecraft entity size by using the Flint {@link EntitySize} as the base.
   *
   * @param entitySize The non-null Flint {@link EntitySize}.
   * @return The new Minecraft entity size or {@code null}, if the given entity size was invalid.
   */
  Object toMinecraftEntitySize(EntitySize entitySize);

  /**
   * Creates a new {@link EntitySize} by using the given Minecraft entity size as the base.
   *
   * @param handle The non-null Minecraft entity size.
   * @return The new Flint {@link EntitySize} or {@code null}, if the given entity size was invalid.
   * @throws IllegalArgumentException If the given object is no Minecraft entity size.
   */
  EntitySize fromMinecraftEntitySize(Object handle);
}
