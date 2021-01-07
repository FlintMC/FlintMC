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

package net.flintmc.mcapi.world.generator.flat;

import net.flintmc.mcapi.world.biome.Biome;

public interface FlatWorldGeneratorSettings {

  Biome getBiome();

  void setBiome(Biome biome);

  FlatWorldStructure[] getStructures();

  void addStructure(FlatWorldStructure structure);

  void removeStructure(FlatWorldStructure structure);

  int getStructureValue(FlatWorldStructure structure, StructureOption option);

  void setStructureValue(FlatWorldStructure structure, StructureOption option, int value);

  FlatWorldLayer[] getLayers();

  void addLayer(FlatWorldLayer layer);

  void setLayer(int index, FlatWorldLayer layer);

  void removeLayer(int index);

  FlatWorldGeneratorSettings validate();

  String serialize();

  interface Factory {

    /**
     * Creates the default Minecraft flat world settings which are 3 layers:
     * <ul>
     *   <li>1 * grass</li>
     *   <li>2 * dirt</li>
     *   <li>1 * bedrock</li>
     * </ul>
     *
     * @return The new non-null settings with the default values
     */
    FlatWorldGeneratorSettings createDefault();

    FlatWorldGeneratorSettings createEmpty();

    FlatWorldGeneratorSettings parseString(String serialized);

  }

}
