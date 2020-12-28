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

package net.flintmc.mcapi.entity.item;

/**
 * Mapper between the Minecraft item entities and Flint entities.
 */
public interface ItemEntityMapper {

  /**
   * Creates a new {@link ItemEntity} by using the given Minecraft item entity as the base.
   *
   * @param handle The non-null Minecraft item entity.
   * @return The new Flint {@link ItemEntity} or {@code null} if the given item entity was invalid.
   * @throws IllegalArgumentException If the given object is no Minecraft item entity.
   */
  ItemEntity fromMinecraftItemEntity(Object handle);

  /**
   * Creates a new Minecraft item entity by using the Flint {@link ItemEntity} as the base.
   *
   * @param itemEntity The non-null Flint {@link ItemEntity}.
   * @return The new Minecraft item entity or {@code null} if the given item entity was invalid.
   */
  Object toMinecraftItemEntity(ItemEntity itemEntity);
}
