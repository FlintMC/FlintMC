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

package net.flintmc.mcapi.world.biome;

import net.flintmc.mcapi.resources.ResourceLocation;
import java.util.Collection;

/**
 * Registry for all {@link Biome}s available in this Minecraft version.
 */
public interface BiomeRegistry {

  /**
   * Retrieves a collection of all available biomes in this Minecraft  version.
   *
   * @return The non-null collection of biomes
   */
  Collection<Biome> getBiomes();

  /**
   * Retrieves the default biome of this Minecraft version.
   *
   * @return The non-null default biome
   */
  Biome getDefaultBiome();

  /**
   * Retrieves the biome in this registry with the given resource name.
   *
   * @param name The non-null name of the biome to get
   * @return The biome in this registry for the given name or {@code null}, if no biome exists for
   * the given name
   * @see Biome#getName()
   */
  Biome getBiome(ResourceLocation name);

}
