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

package net.flintmc.mcapi.v1_15_2.world.generator.flat;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.inject.Singleton;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.world.generator.flat.structure.FlatWorldStructure;
import net.flintmc.mcapi.world.generator.flat.structure.StructureOption;
import net.flintmc.mcapi.world.generator.flat.structure.SupportedStructureHolder;

@Singleton
@Implement(value = SupportedStructureHolder.class, version = "1.15.2")
public class VersionedSupportedStructureHolder implements SupportedStructureHolder {

  private static final Multimap<StructureOption, FlatWorldStructure> SUPPORTED_OPTIONS =
      HashMultimap.create();

  static {
    SUPPORTED_OPTIONS.put(StructureOption.DISTANCE, FlatWorldStructure.VILLAGE);
    SUPPORTED_OPTIONS.put(StructureOption.DISTANCE, FlatWorldStructure.BIOME);
    SUPPORTED_OPTIONS.put(StructureOption.DISTANCE, FlatWorldStructure.STRONGHOLD);
    SUPPORTED_OPTIONS.put(StructureOption.DISTANCE, FlatWorldStructure.END_CITY);
    SUPPORTED_OPTIONS.put(StructureOption.DISTANCE, FlatWorldStructure.MANSION);

    SUPPORTED_OPTIONS.put(StructureOption.COUNT, FlatWorldStructure.STRONGHOLD);
    SUPPORTED_OPTIONS.put(StructureOption.SPREAD, FlatWorldStructure.STRONGHOLD);

    SUPPORTED_OPTIONS.put(StructureOption.SEPARATION, FlatWorldStructure.OCEANMONUMENT);
    SUPPORTED_OPTIONS.put(StructureOption.SPACING, FlatWorldStructure.OCEANMONUMENT);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isSupportedStructure(FlatWorldStructure structure) {
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isSupportedOption(FlatWorldStructure structure, StructureOption option) {
    return SUPPORTED_OPTIONS.get(option).contains(structure);
  }
}
