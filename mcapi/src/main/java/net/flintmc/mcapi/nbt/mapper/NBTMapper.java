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

package net.flintmc.mcapi.nbt.mapper;

import net.flintmc.mcapi.nbt.NBT;

/**
 * Mapper between the Minecraft NBT and Flint NBT.
 */
public interface NBTMapper {

  /**
   * Creates a new {@link NBT} by using the given Minecraft nbt as the base.
   *
   * @param handle The non-null minecraft nbt.
   * @return The new Flint {@link NBT} or {@code null}, if the given nbt was invalid.
   * @throws IllegalArgumentException IF the given object is no Minecraft nbt.
   */
  NBT fromMinecraftNBT(Object handle);

  /**
   * Creates a new Minecraft nbt by the given Flint {@link NBT} as the base.
   *
   * @param nbt The non-null Flint {@link NBT}.
   * @return The new Minecraft nbt or {@code null}, if the given component was invalid.
   */
  Object toMinecraftNBT(NBT nbt);
}
