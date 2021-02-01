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

package net.flintmc.mcapi.world.mapper;

import net.flintmc.mcapi.world.storage.WorldOverview;
import net.flintmc.mcapi.world.type.WorldType;

/**
 * Mapper between the Minecraft world and Flint world.
 */
public interface WorldMapper {

  /**
   * Creates a new Minecraft world type by using the Flint {@link WorldType} as the base.
   *
   * @param worldType The non-null Flint {@link WorldType}.
   * @return The new Minecraft world type or {@code null}, if the given world type was invalid.
   */
  Object toMinecraftWorldType(WorldType worldType);

  /**
   * Creates a new {@link WorldType} by using the given Minecraft world type as the base.
   *
   * @param handle The non-null Minecraft world type.
   * @return The new Flint {@link WorldType} or {@code null}, if the given world type was invalid.
   * @throws IllegalArgumentException If the given object is no Minecraft world type.
   */
  WorldType fromMinecraftWorldType(Object handle);

  /**
   * Creates a new Minecraft world summary by using the Flint {@link WorldOverview} as the base.
   *
   * @param worldOverview The non-null Flint {@link WorldOverview}.
   * @return The new Minecraft world summary or {@code null}, if the given world summary was invalid.
   */
  Object toMinecraftWorldSummary(WorldOverview worldOverview);

  /**
   * Creates a new {@link WorldOverview} by using the given Minecraft world summary as the base.
   *
   * @param handle The non-null Minecraft world type.
   * @return The new Flint {@link WorldOverview} or {@code null}, if the given world summary was
   * invalid.
   * @throws IllegalArgumentException If the given object is no Minecraft world summary.
   */
  WorldOverview fromMinecraftWorldSummary(Object handle);

}
