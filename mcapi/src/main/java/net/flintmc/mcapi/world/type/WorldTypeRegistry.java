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

package net.flintmc.mcapi.world.type;

import net.flintmc.mcapi.world.generator.buffet.BuffetWorldGeneratorSettings;
import net.flintmc.mcapi.world.generator.flat.FlatWorldGeneratorSettings;
import java.util.List;

/**
 * Represents a world type register.
 *
 * @see WorldType
 */
public interface WorldTypeRegistry {

  /**
   * Retrieves a collection with all world types.
   *
   * @return A collection with all world types.
   */
  List<WorldType> getWorldTypes();

  /**
   * Retrieves the type to generate a default Minecraft world.
   *
   * @return The non-null default type
   */
  WorldType getDefaultType();

  /**
   * Retrieves the type to generate a Minecraft flat world.
   *
   * @return The non-null flat type
   * @see FlatWorldGeneratorSettings
   */
  WorldType getFlatType();

  /**
   * Retrieves the type to generate a default Minecraft world with larger biomes than normal.
   *
   * @return The non-null large biomes type
   */
  WorldType getLargeBiomesType();

  /**
   * Retrieves the type to generate an amplified Minecraft world.
   *
   * @return The non-null amplified type
   */
  WorldType getAmplifiedType();

  /**
   * Retrieves the buffet type to generate a world consisting of one biome.
   *
   * @return The non-null buffet type
   * @see BuffetWorldGeneratorSettings
   */
  WorldType getBuffetType();

  /**
   * Retrieves the type from this registry with matching the given name.
   *
   * @param name The non-null case-sensitive name (e.g. "default")
   * @return The world type with the given name or {@code null}, if there is no type matching the
   * given name
   * @see WorldType#getName()
   */
  WorldType getType(String name);

}
