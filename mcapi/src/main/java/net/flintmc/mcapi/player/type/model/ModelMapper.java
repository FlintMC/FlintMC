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

package net.flintmc.mcapi.player.type.model;

/**
 * Mapper between the Minecraft player model part and the Flint {@link PlayerClothing}.
 */
public interface ModelMapper {

  /**
   * Retrieves a {@link PlayerClothing} constant by using the given Minecraft player model part.
   *
   * @param playerModelPart The non-null minecraft player model part.
   * @return The {@link PlayerClothing} constant.
   * @throws IllegalArgumentException If the given object is not a Minecraft player model part.
   */
  PlayerClothing fromMinecraftPlayerModelPart(Object playerModelPart);

  /**
   * Retrieves a Minecraft player model part constant by using the given {@link PlayerClothing}.
   *
   * @param playerClothing The non-null player clothing.
   * @return The player model part constant.
   */
  Object toMinecraftPlayerModelPart(PlayerClothing playerClothing);
}
