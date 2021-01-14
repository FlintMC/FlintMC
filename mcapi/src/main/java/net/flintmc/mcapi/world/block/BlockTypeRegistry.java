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

package net.flintmc.mcapi.world.block;

import net.flintmc.mcapi.resources.ResourceLocation;
import java.util.Collection;

/**
 * Registry for all {@link BlockType}s available in this Minecraft version.
 */
public interface BlockTypeRegistry {

  /**
   * Retrieves a collection of all block types that are registered.
   *
   * @return The non-null collection of all block types
   */
  Collection<BlockType> getTypes();

  /**
   * Retrieves a specific block type for a specific location
   *
   * @param location The non-null location to get the type for (e.g. minecraft:stone)
   * @return The type for the given location or {@code null}, if there is no block type for the given
   * location
   */
  BlockType getType(ResourceLocation location);

}
