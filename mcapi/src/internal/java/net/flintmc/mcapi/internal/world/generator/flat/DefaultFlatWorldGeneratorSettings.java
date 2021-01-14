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

import com.google.inject.Inject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.world.biome.Biome;
import net.flintmc.mcapi.world.biome.BiomeRegistry;
import net.flintmc.mcapi.world.generator.flat.FlatWorldGeneratorSettings;
import net.flintmc.mcapi.world.generator.flat.FlatWorldGeneratorSettingsSerializer;
import net.flintmc.mcapi.world.generator.flat.FlatWorldLayer;
import net.flintmc.mcapi.world.generator.flat.FlatWorldStructure;
import net.flintmc.mcapi.world.generator.flat.StructureOption;
import net.flintmc.util.commons.Validate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Implement(FlatWorldGeneratorSettings.class)
public class DefaultFlatWorldGeneratorSettings implements FlatWorldGeneratorSettings {

  private final FlatWorldGeneratorSettingsSerializer serializer;
  private final BiomeRegistry biomeRegistry;

  private Biome biome;
  private final Map<FlatWorldStructure, Map<StructureOption, Integer>> structures;
  private final List<FlatWorldLayer> layers;
  private final Map<String, Object> options;

  @Inject
  private DefaultFlatWorldGeneratorSettings(
      FlatWorldGeneratorSettingsSerializer serializer,
      BiomeRegistry biomeRegistry) {
    this.serializer = serializer;
    this.biomeRegistry = biomeRegistry;

    this.structures = new HashMap<>();
    this.layers = new ArrayList<>();
    this.options = new HashMap<>();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Biome getBiome() {
    if (this.biome == null) {
      this.biome = this.biomeRegistry.getDefaultBiome();
    }

    return this.biome;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public FlatWorldGeneratorSettings setBiome(Biome biome) {
    this.biome = biome;
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public FlatWorldStructure[] getStructures() {
    return this.structures.keySet().toArray(new FlatWorldStructure[0]);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasStructure(FlatWorldStructure structure) {
    return this.structures.containsKey(structure);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public FlatWorldGeneratorSettings addStructure(FlatWorldStructure structure) {
    if (!this.structures.containsKey(structure)) {
      this.structures.put(structure, new HashMap<>());
    }
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public FlatWorldGeneratorSettings removeStructure(FlatWorldStructure structure) {
    this.structures.remove(structure);
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasStructureOption(FlatWorldStructure structure, StructureOption option) {
    return this.structures.containsKey(structure)
        && this.structures.get(structure).containsKey(option);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getStructureOption(FlatWorldStructure structure, StructureOption option)
      throws IllegalArgumentException {
    return this.structures.getOrDefault(structure, Collections.emptyMap())
        .getOrDefault(option, 0);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public FlatWorldGeneratorSettings setStructureOption(FlatWorldStructure structure,
      StructureOption option,
      int value) throws IllegalArgumentException {
    this.structures.computeIfAbsent(structure, s -> new HashMap<>()).put(option, value);
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public FlatWorldLayer[] getLayers() {
    return this.layers.toArray(new FlatWorldLayer[0]);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int layerCount() {
    return this.layers.size();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public FlatWorldGeneratorSettings addLayer(FlatWorldLayer layer) {
    this.layers.add(layer);
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public FlatWorldGeneratorSettings setLayer(int index, FlatWorldLayer layer) {
    this.layers.set(index, layer);
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public FlatWorldGeneratorSettings removeLayer(int index) {
    this.layers.remove(index);
    return this;
  }

  @Override
  public FlatWorldGeneratorSettings clearLayers() {
    this.layers.clear();
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public FlatWorldGeneratorSettings validate() {
    Validate.checkFalse(this.layers.isEmpty(), "No layers set");

    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String serialize() {
    return this.serializer.serialize(this.validate());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public FlatWorldGeneratorSettings setOption(String key, Object value) {
    this.options.put(key, value);
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object getOption(String key) {
    return this.options.get(key);
  }

}
