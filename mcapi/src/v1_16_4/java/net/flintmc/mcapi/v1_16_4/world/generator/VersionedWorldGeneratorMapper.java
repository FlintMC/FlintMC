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

package net.flintmc.mcapi.v1_16_4.world.generator;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.resources.ResourceLocationProvider;
import net.flintmc.mcapi.v1_16_4.world.generator.flat.FlatGenerationSettingsShadow;
import net.flintmc.mcapi.world.World;
import net.flintmc.mcapi.world.biome.BiomeRegistry;
import net.flintmc.mcapi.world.block.BlockState;
import net.flintmc.mcapi.world.generator.WorldGameMode;
import net.flintmc.mcapi.world.generator.WorldGeneratorBuilder;
import net.flintmc.mcapi.world.generator.WorldGeneratorMapper;
import net.flintmc.mcapi.world.generator.flat.FlatWorldGeneratorSettings;
import net.flintmc.mcapi.world.generator.flat.FlatWorldGeneratorSettings.Factory;
import net.flintmc.mcapi.world.generator.flat.FlatWorldLayer;
import net.flintmc.mcapi.world.generator.flat.FlatWorldStructure;
import net.flintmc.mcapi.world.generator.flat.StructureOption;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.codec.DatapackCodec;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.Difficulty;
import net.minecraft.world.GameRules;
import net.minecraft.world.GameType;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.FlatGenerationSettings;
import net.minecraft.world.gen.FlatLayerInfo;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraft.world.gen.settings.StructureSpreadSettings;

@Singleton
@Implement(value = WorldGeneratorMapper.class, version = "1.16.4")
public class VersionedWorldGeneratorMapper implements WorldGeneratorMapper {

  public static final String REGISTRIES_OPTION =
      "dynamicRegistries+" + UUID.randomUUID().toString();

  private static final BiMap<Structure<?>, FlatWorldStructure> STRUCTURES = HashBiMap.create();

  static {
    STRUCTURES.put(Structure.MINESHAFT, FlatWorldStructure.MINESHAFT);
    STRUCTURES.put(Structure.VILLAGE, FlatWorldStructure.VILLAGE);
    STRUCTURES.put(Structure.STRONGHOLD, FlatWorldStructure.STRONGHOLD);
    STRUCTURES.put(Structure.SWAMP_HUT, FlatWorldStructure.SWAMP_HUT);
    STRUCTURES.put(Structure.DESERT_PYRAMID, FlatWorldStructure.DESERT_PYRAMID);
    STRUCTURES.put(Structure.JUNGLE_PYRAMID, FlatWorldStructure.JUNGLE_TEMPLE);
    STRUCTURES.put(Structure.IGLOO, FlatWorldStructure.IGLOO);
    STRUCTURES.put(Structure.SHIPWRECK, FlatWorldStructure.SHIPWRECK);
    STRUCTURES.put(Structure.OCEAN_RUIN, FlatWorldStructure.OCEAN_RUIN);
    STRUCTURES.put(Structure.END_CITY, FlatWorldStructure.END_CITY);
    STRUCTURES.put(Structure.WOODLAND_MANSION, FlatWorldStructure.MANSION);
    STRUCTURES.put(Structure.FORTRESS, FlatWorldStructure.FORTRESS);
    STRUCTURES.put(Structure.PILLAGER_OUTPOST, FlatWorldStructure.PILLAGER_OUTPOST);
  }

  private final ResourceLocationProvider provider;
  private final World world;
  private final FlatWorldGeneratorSettings.Factory flatSettingsFactory;
  private final BiomeRegistry biomeRegistry;
  private final BlockState.Factory blockStateFactory;
  private final FlatWorldLayer.Factory layerFactory;

  @Inject
  private VersionedWorldGeneratorMapper(
      ResourceLocationProvider provider,
      World world,
      Factory flatSettingsFactory,
      BiomeRegistry biomeRegistry,
      BlockState.Factory blockStateFactory,
      FlatWorldLayer.Factory layerFactory) {
    this.provider = provider;
    this.world = world;
    this.flatSettingsFactory = flatSettingsFactory;
    this.biomeRegistry = biomeRegistry;
    this.blockStateFactory = blockStateFactory;
    this.layerFactory = layerFactory;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object toMinecraftGenerator(WorldGeneratorBuilder generator) {
    generator.validate();

    net.flintmc.mcapi.world.datapack.DatapackCodec datapacks = generator.datapackCodec();
    return new WorldSettings(
        generator.name(),
        this.mapGameMode(generator.extended().mode()),
        generator.extended().mode() == WorldGameMode.HARDCORE,
        (Difficulty) this.world.toMinecraftDifficulty(generator.extended().difficulty()),
        generator.extended().allowCheats(),
        new GameRules(), // TODO?
        datapacks == null ? DatapackCodec.VANILLA_CODEC
            : new DatapackCodec(datapacks.getEnabled(), datapacks.getDisabled())
    );
  }

  private GameType mapGameMode(WorldGameMode mode) {
    switch (mode) {
      case SURVIVAL:
      case HARDCORE:
        return GameType.SURVIVAL;
      case CREATIVE:
        return GameType.CREATIVE;
      default:
        throw new IllegalStateException("Unexpected value: " + mode);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public FlatWorldGeneratorSettings fromMinecraftFlatSettings(Object settings) {
    if (!(settings instanceof FlatGenerationSettings)) {
      throw new IllegalArgumentException(
          "Object needs to be an instance of " + FlatGenerationSettings.class.getName());
    }

    FlatGenerationSettings handle = (FlatGenerationSettings) settings;

    FlatWorldGeneratorSettings result = this.flatSettingsFactory.createEmpty();

    if (handle.getBiome() != null) {
      Registry<Biome> registry = ((FlatGenerationSettingsShadow) handle).getBiomeRegistry();
      ResourceLocation key = registry.getKey(handle.getBiome());
      result.setBiome(this.biomeRegistry.getBiome(this.provider.fromMinecraft(key)));
    }

    for (FlatLayerInfo layer : handle.getFlatLayers()) {
      BlockState state = this.blockStateFactory.create(layer.getLayerMaterial());
      result.addLayer(this.layerFactory.create(state, layer.getLayerCount()));
    }

    Map<Structure<?>, StructureSeparationSettings> structureOptions =
        handle.func_236943_d_().func_236195_a_();

    structureOptions.forEach((structure, options) -> {
      FlatWorldStructure structureEnum = STRUCTURES.get(structure);
      if (structureEnum == null) {
        return;
      }

      result.addStructure(structureEnum);

      result.setStructureOption(
          structureEnum, StructureOption.SEPARATION, options.func_236671_b_());
      result.setStructureOption(structureEnum, StructureOption.SPACING, options.func_236668_a_());
    });

    if (result.hasStructure(FlatWorldStructure.STRONGHOLD)) {
      StructureSpreadSettings stronghold = handle.func_236943_d_().func_236199_b_();

      result.setStructureOption(
          FlatWorldStructure.STRONGHOLD, StructureOption.DISTANCE, stronghold.func_236660_a_());
      result.setStructureOption(
          FlatWorldStructure.STRONGHOLD, StructureOption.SPREAD, stronghold.func_236662_b_());
      result.setStructureOption(
          FlatWorldStructure.STRONGHOLD, StructureOption.COUNT, stronghold.func_236663_c_());
    }

    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object toMinecraftFlatSettings(FlatWorldGeneratorSettings settings) {
    DynamicRegistries registries = (DynamicRegistries) settings.getOption(REGISTRIES_OPTION);
    if (registries == null) {
      registries = DynamicRegistries.func_239770_b_();
    }

    Registry<Biome> biomeRegistry = registries.getRegistry(Registry.BIOME_KEY);

    Optional<StructureSpreadSettings> strongholdSettings =
        settings.hasStructure(FlatWorldStructure.STRONGHOLD) ?
            Optional.of(new StructureSpreadSettings(
                settings
                    .getStructureOption(FlatWorldStructure.STRONGHOLD, StructureOption.DISTANCE),
                settings.getStructureOption(FlatWorldStructure.STRONGHOLD, StructureOption.SPREAD),
                settings.getStructureOption(FlatWorldStructure.STRONGHOLD, StructureOption.COUNT)
            )) : Optional.empty();

    Map<Structure<?>, StructureSeparationSettings> structureSettings = new HashMap<>();

    for (FlatWorldStructure structure : settings.getStructures()) {
      Structure<?> structureHandle = STRUCTURES.inverse().get(structure);
      if (structureHandle == null) {
        continue;
      }

      StructureSeparationSettings defaultSettings =
          DimensionStructuresSettings.field_236191_b_.get(structureHandle);
      if (defaultSettings == null) {
        throw new IllegalStateException(
            "No default settings for structure " + structure + " found");
      }

      int spacing = defaultSettings.func_236668_a_();
      int separation = defaultSettings.func_236671_b_();
      int salt = defaultSettings.func_236673_c_();

      if (settings.hasStructureOption(structure, StructureOption.SPACING)) {
        spacing = settings.getStructureOption(structure, StructureOption.SPACING);
      }
      if (settings.hasStructureOption(structure, StructureOption.SEPARATION)) {
        separation = settings.getStructureOption(structure, StructureOption.SEPARATION);
      }

      structureSettings.put(
          structureHandle, new StructureSeparationSettings(spacing, separation, salt));
    }

    DimensionStructuresSettings structuresSettings =
        new DimensionStructuresSettings(strongholdSettings, structureSettings);

    List<FlatLayerInfo> flatLayers = new ArrayList<>();
    for (FlatWorldLayer layer : settings.getLayers()) {
      Block block = layer.getBlockState().getType().getHandle();
      flatLayers.add(new FlatLayerInfo(layer.getLayerHeight(), block));
    }

    // In 1.16 only both can be generated together
    boolean generateLakes = settings.hasStructure(FlatWorldStructure.LAVA_LAKE)
        || settings.hasStructure(FlatWorldStructure.WATER_LAKE);

    // generate ores and grass/flowers etc.
    boolean decorate = settings.hasStructure(FlatWorldStructure.DECORATION);

    net.flintmc.mcapi.world.biome.Biome flintBiome = settings.getBiome();
    ResourceLocation biomeKey = flintBiome == null ? new ResourceLocation("")
        : new ResourceLocation(flintBiome.getName().getNamespace(), flintBiome.getName().getPath());
    Biome biome = biomeRegistry.getOrDefault(biomeKey);

    return new FlatGenerationSettings(
        biomeRegistry,
        structuresSettings,
        flatLayers,
        generateLakes,
        decorate,
        Optional.of(() -> biome)
    );
  }
}
