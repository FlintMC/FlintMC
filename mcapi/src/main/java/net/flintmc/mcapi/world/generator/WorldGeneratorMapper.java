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

package net.flintmc.mcapi.world.generator;

import net.flintmc.mcapi.world.generator.flat.FlatWorldGeneratorSettings;

/**
 * Mapper between Flint implementations of world generator classes and their respective Minecraft
 * implementation.
 * <p>
 * Only intended to be used internally.
 */
public interface WorldGeneratorMapper {

  /**
   * Maps the given generator to a Minecraft instance.
   *
   * @param generator The non-null generator to be mapped
   * @return The new non-null Minecraft generator settings
   */
  Object toMinecraftGenerator(WorldGeneratorBuilder generator);

  /**
   * Maps the given Minecraft flat settings to a Flint instance.
   *
   * @param settings The non-null settings to be mapped
   * @return The new non-null flat settings from the Minecraft settings
   * @throws IllegalArgumentException If the given object is not an instance of the Minecraft
   *                                  generator settings of this version
   */
  FlatWorldGeneratorSettings fromMinecraftFlatSettings(Object settings);

  /**
   * Maps the given Flint flat settings to a Minecraft instance.
   *
   * @param settings The non-null settings to be mapped
   * @return The new non-null Minecraft flat settings
   */
  Object toMinecraftFlatSettings(FlatWorldGeneratorSettings settings);
}
