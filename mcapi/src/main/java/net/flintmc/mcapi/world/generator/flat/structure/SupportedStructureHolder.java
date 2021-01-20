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

package net.flintmc.mcapi.world.generator.flat.structure;

/**
 * Utility class to check if a specific {@link FlatWorldStructure} or {@link StructureOption} exists
 * in the current Minecraft version.
 */
public interface SupportedStructureHolder {

  /**
   * Retrieves whether or not the given structure is supported in the current Minecraft version.
   *
   * @param structure The non-null structure to check for
   * @return {@code true} if it is supported, {@code false} otherwise
   */
  boolean isSupportedStructure(FlatWorldStructure structure);

  /**
   * Retrieves whether or not the given structure in combination with the is supported in the
   * current Minecraft version.
   *
   * @param structure The non-null structure to check for
   * @param option    The non-null option to check for
   * @return {@code true} if it is supported, {@code false} otherwise
   */
  boolean isSupportedOption(FlatWorldStructure structure, StructureOption option);

}
