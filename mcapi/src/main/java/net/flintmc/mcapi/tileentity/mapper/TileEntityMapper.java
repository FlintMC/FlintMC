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

package net.flintmc.mcapi.tileentity.mapper;

import net.flintmc.mcapi.tileentity.SignTileEntity;
import net.flintmc.mcapi.tileentity.TileEntity;

/**
 * Mapper between the Minecraft tile entity type and Flint tile entity.
 */
public interface TileEntityMapper {

  /**
   * Retrieves a Minecraft tile entity by using the Flint {@link TileEntity} as the base.
   *
   * @param tileEntity The non-null Flint {@link TileEntity}.
   * @return A Minecraft tile entity or {@code null}, if the given tile entity was invalid.
   */
  Object toMinecraftTileEntity(TileEntity tileEntity);

  /**
   * Creates a new {@link TileEntity} by using the given Minecraft tile entity as the base.
   *
   * @param tileEntity The non-null Minecraft tile entity.
   * @return The new Flint {@link TileEntity} or {@code null}, if the given tile entity was invalid.
   * @throws IllegalArgumentException If the given object is no Minecraft tile entity.
   */
  TileEntity fromMinecraftTileEntity(Object tileEntity);

  /**
   * Retrieves a Minecraft sign tile entity by using the Flint {@link SignTileEntity} as the base.
   *
   * @param signTileEntity The non-null Flint {@link SignTileEntity}.
   * @return A Minecraft sign tile entity or {@code null}, if the given sign tile entity was invalid.
   */
  Object toMinecraftSignTileEntity(SignTileEntity signTileEntity);

  /**
   * Creates a new {@link SignTileEntity} by using the given Minecraft sign tile entity as the
   * base.
   *
   * @param signTileEntity The non-null Minecraft sign tile entity.
   * @return The new Flint {@link SignTileEntity} or {@code null}, if the given sign tile entity was
   * invalid.
   * @throws IllegalArgumentException If the given object is no Minecraft sign tile entity.
   */
  SignTileEntity fromMinecraftSignTileEntity(Object signTileEntity);
}
