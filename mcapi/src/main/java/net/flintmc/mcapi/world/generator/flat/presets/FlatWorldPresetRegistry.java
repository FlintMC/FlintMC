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

package net.flintmc.mcapi.world.generator.flat.presets;

import net.flintmc.mcapi.world.generator.flat.FlatWorldGeneratorSettings;

import java.util.Collection;

/**
 * Registry for presets of {@link FlatWorldGeneratorSettings}.
 */
public interface FlatWorldPresetRegistry {

  /**
   * Retrieves a mutable list of presets for the flat world generator.
   *
   * @return The non-null mutable list of presets
   */
  Collection<FlatWorldPreset> getPresets();

  /**
   * Adds a new preset to this registry.
   *
   * @param preset The non-null preset to be added
   */
  void addPreset(FlatWorldPreset preset);

  /**
   * Removes an existing preset from this registry.
   *
   * @param preset The non-null preset to be removed
   */
  void removePreset(FlatWorldPreset preset);

}
