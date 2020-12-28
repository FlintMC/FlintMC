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

package net.flintmc.mcapi.tileentity.type;

import java.util.Map;

/**
 * The register is used to register {@link TileEntityType}'s or to get {@link TileEntityType}'s.
 */
public interface TileEntityTypeRegister {

  /**
   * Retrieves a key-value system with the registered {@link TileEntityType}'s.
   *
   * @return A key-value system.
   */
  Map<String, TileEntityType> getTileEntitiesTypes();

  /**
   * Retrieves the tile entity type by teh given key.
   *
   * @param key The key to get an tile entity type.
   * @return A tile entity type or a default value.
   */
  TileEntityType getTileEntityType(String key);
}
