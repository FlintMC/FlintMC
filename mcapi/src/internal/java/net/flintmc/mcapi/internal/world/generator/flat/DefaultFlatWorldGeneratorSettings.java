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

package net.flintmc.mcapi.internal.world.generator.flat;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.world.biome.Biome;
import net.flintmc.mcapi.world.generator.flat.FlatWorldGeneratorSettings;
import net.flintmc.mcapi.world.generator.flat.FlatWorldGeneratorSettingsSerializer;
import net.flintmc.mcapi.world.generator.flat.FlatWorldLayer;
import net.flintmc.mcapi.world.generator.flat.FlatWorldStructure;
import net.flintmc.mcapi.world.generator.flat.StructureOption;

@Implement(FlatWorldGeneratorSettings.class)
public class DefaultFlatWorldGeneratorSettings implements FlatWorldGeneratorSettings {

  private final FlatWorldGeneratorSettingsSerializer serializer;

  private Biome biome; // TODO add default value
  private final Set<FlatWorldStructure> structures;
  private final Map<FlatWorldStructure, Map<StructureOption, Integer>> options;
  private final List<FlatWorldLayer> layers;

  @Inject
  private DefaultFlatWorldGeneratorSettings(FlatWorldGeneratorSettingsSerializer serializer) {
    this.serializer = serializer;

    this.structures = new HashSet<>();
    this.options = new HashMap<>();
    this.layers = new ArrayList<>();
  }

  @Override
  public Biome getBiome() {
    return this.biome;
  }

  @Override
  public void setBiome(Biome biome) {
    this.biome = biome;
  }

  @Override
  public FlatWorldStructure[] getStructures() {
    return this.structures.toArray(new FlatWorldStructure[0]);
  }

  @Override
  public void addStructure(FlatWorldStructure structure) {
    this.structures.add(structure);
  }

  @Override
  public void removeStructure(FlatWorldStructure structure) {
    this.structures.remove(structure);
  }

  @Override
  public int getStructureValue(FlatWorldStructure structure, StructureOption option)
      throws IllegalArgumentException {
    return this.options.getOrDefault(structure, Collections.emptyMap())
        .getOrDefault(option, 0);
  }

  @Override
  public void setStructureValue(FlatWorldStructure structure, StructureOption option,
      int value) throws IllegalArgumentException {
    this.options.computeIfAbsent(structure, s -> new HashMap<>())
        .put(option, value);
  }

  @Override
  public FlatWorldLayer[] getLayers() {
    return this.layers.toArray(new FlatWorldLayer[0]);
  }

  @Override
  public void addLayer(FlatWorldLayer layer) {
    this.layers.add(layer);
  }

  @Override
  public void setLayer(int index, FlatWorldLayer layer) {
    this.layers.set(index, layer);
  }

  @Override
  public void removeLayer(int index) {
    this.layers.remove(index);
  }

  @Override
  public FlatWorldGeneratorSettings validate() {
    Preconditions.checkNotNull(this.biome, "Invalid biome provided");
    Preconditions.checkArgument(!this.layers.isEmpty(), "No layers set");

    return this;
  }

  @Override
  public String serialize() {
    return this.serializer.serialize(this.validate());
  }
}
