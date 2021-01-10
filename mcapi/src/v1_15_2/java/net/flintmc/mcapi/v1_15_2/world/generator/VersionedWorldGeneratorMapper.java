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

package net.flintmc.mcapi.v1_15_2.world.generator;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.util.HashMap;
import java.util.Map;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.resources.ResourceLocationProvider;
import net.flintmc.mcapi.world.biome.BiomeRegistry;
import net.flintmc.mcapi.world.block.BlockState;
import net.flintmc.mcapi.world.generator.WorldGameMode;
import net.flintmc.mcapi.world.generator.WorldGeneratorBuilder;
import net.flintmc.mcapi.world.generator.WorldGeneratorMapper;
import net.flintmc.mcapi.world.generator.buffet.BuffetWorldGeneratorSettings;
import net.flintmc.mcapi.world.generator.flat.FlatWorldGeneratorSettings;
import net.flintmc.mcapi.world.generator.flat.FlatWorldLayer;
import net.flintmc.mcapi.world.generator.flat.FlatWorldStructure;
import net.flintmc.mcapi.world.generator.flat.StructureOption;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.GameType;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProviderType;
import net.minecraft.world.gen.FlatGenerationSettings;
import net.minecraft.world.gen.FlatLayerInfo;

@Singleton
@Implement(value = WorldGeneratorMapper.class, version = "1.15.2")
public class VersionedWorldGeneratorMapper implements WorldGeneratorMapper {

  private static final BiMap<String, FlatWorldStructure> STRUCTURES = HashBiMap.create();

  static {
    STRUCTURES.put("mineshaft", FlatWorldStructure.MINESHAFT);
    STRUCTURES.put("village", FlatWorldStructure.VILLAGE);
    STRUCTURES.put("stronghold", FlatWorldStructure.STRONGHOLD);
    STRUCTURES.put("swamp_hut", FlatWorldStructure.SWAMP_HUT);
    STRUCTURES.put("desert_pyramid", FlatWorldStructure.DESERT_PYRAMID);
    STRUCTURES.put("jungle_temple", FlatWorldStructure.JUNGLE_TEMPLE);
    STRUCTURES.put("igloo", FlatWorldStructure.IGLOO);
    STRUCTURES.put("shipwreck", FlatWorldStructure.SHIPWRECK);
    STRUCTURES.put("ocean_ruin", FlatWorldStructure.OCEAN_RUIN);
    STRUCTURES.put("lake", FlatWorldStructure.WATER_LAKE);
    STRUCTURES.put("lava_lake", FlatWorldStructure.LAVA_LAKE);
    STRUCTURES.put("endcity", FlatWorldStructure.END_CITY);
    STRUCTURES.put("mansion", FlatWorldStructure.MANSION);
    STRUCTURES.put("fortress", FlatWorldStructure.FORTRESS);
    STRUCTURES.put("biome_1", FlatWorldStructure.BIOME);
    STRUCTURES.put("oceanmonument", FlatWorldStructure.OCEANMONUMENT);
    STRUCTURES.put("pillager_outpost", FlatWorldStructure.PILLAGER_OUTPOST);
    STRUCTURES.put("dungeon", FlatWorldStructure.DUNGEON);
    STRUCTURES.put("decoration", FlatWorldStructure.DECORATION);
  }

  private static final StructureOption[] OPTIONS = StructureOption.values();

  private final BiomeRegistry biomeRegistry;
  private final ResourceLocationProvider provider;
  private final BlockState.Factory blockStateFactory;
  private final FlatWorldLayer.Factory layerFactory;
  private final FlatWorldGeneratorSettings.Factory flatSettingsFactory;

  @Inject
  private VersionedWorldGeneratorMapper(
      BiomeRegistry biomeRegistry,
      ResourceLocationProvider provider,
      BlockState.Factory blockStateFactory,
      FlatWorldLayer.Factory layerFactory,
      FlatWorldGeneratorSettings.Factory flatSettingsFactory) {
    this.biomeRegistry = biomeRegistry;
    this.provider = provider;
    this.blockStateFactory = blockStateFactory;
    this.layerFactory = layerFactory;
    this.flatSettingsFactory = flatSettingsFactory;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object toMinecraftGenerator(WorldGeneratorBuilder generator) {
    generator.validate();

    GameType type;
    switch (generator.extended().mode()) {
      case SURVIVAL:
      case HARDCORE:
        type = GameType.SURVIVAL;
        break;
      case CREATIVE:
        type = GameType.CREATIVE;
        break;
      default:
        throw new IllegalStateException("Unexpected value: " + generator.extended().mode());
    }

    WorldType worldType = WorldType.byName(generator.extended().type().getName());
    if (worldType == null) {
      throw new IllegalArgumentException(
          String.format("WorldType %s doesn't exist", generator.extended().type().getName()));
    }

    WorldSettings settings = new WorldSettings(
        generator.extended().seed(),
        type,
        generator.extended().generateStructures(),
        generator.extended().mode() == WorldGameMode.HARDCORE,
        worldType
    );

    if (generator.extended().allowCheats()) {
      settings.enableCommands();
    }
    if (generator.extended().bonusChest()) {
      settings.enableBonusChest();
    }

    JsonObject json = settings.getGeneratorOptions().getAsJsonObject();

    if (generator.hasFlatSettings()) {
      this.appendFlatSettings(json, generator.flatSettings());
    }

    if (generator.hasBuffetSettings()) {
      this.appendBuffetSettings(json, generator.buffetSettings());
    }

    return settings;
  }

  private void appendFlatSettings(JsonObject json, FlatWorldGeneratorSettings settings) {
    json.addProperty("biome", settings.getBiome().getName().toString());

    JsonArray layers = new JsonArray();
    json.add("layers", layers);

    for (FlatWorldLayer layer : settings.getLayers()) {
      JsonObject layerJson = new JsonObject();
      layers.add(layerJson);

      layerJson.addProperty("block", layer.getBlockState().getType().getName().toString());
      layerJson.addProperty("heght", layer.getLayerHeight());
    }

    JsonObject structures = new JsonObject();
    json.add("structures", structures);

    for (FlatWorldStructure structure : settings.getStructures()) {
      String structureHandle = STRUCTURES.inverse().get(structure);
      if (structureHandle == null) {
        throw new IllegalStateException(
            "Structure " + structure + " is not supported in this version");
      }

      JsonObject structureJson = new JsonObject();
      structures.add(structureHandle, structureJson);

      for (StructureOption option : OPTIONS) {
        if (!settings.hasStructureOption(structure, option)) {
          continue;
        }

        int value = settings.getStructureOption(structure, option);
        structureJson.addProperty(option.name().toLowerCase(), value);
      }
    }
  }

  private void appendBuffetSettings(JsonObject json, BuffetWorldGeneratorSettings settings) {
    JsonObject biomeSource = new JsonObject();
    json.add("biome_source", biomeSource);

    biomeSource.addProperty(
        "type", Registry.BIOME_SOURCE_TYPE.getKey(BiomeProviderType.FIXED).toString());

    JsonObject biomeOptions = new JsonObject();
    JsonArray biomes = new JsonArray();
    biomeOptions.add("biomes", biomes);
    biomeSource.add("options", biomeOptions);
    biomes.add(settings.getBiome().getName().toString());

    JsonObject chunkGenerator = new JsonObject();
    json.add("chunk_generator", chunkGenerator);

    chunkGenerator.addProperty("type", "minecraft:" + settings.getType().name().toLowerCase());

    JsonObject chunkOptions = new JsonObject();
    chunkGenerator.add("options", chunkOptions);
    // hardcoded in CreateBuffetWorldScreen.serialize
    chunkOptions.addProperty("default_fluid", "minecraft:water");
    chunkOptions.addProperty("default_block", "minecraft:stone");
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
      ResourceLocation key = Registry.BIOME.getKey(handle.getBiome());
      result.setBiome(this.biomeRegistry.getBiome(this.provider.fromMinecraft(key)));
    }

    for (FlatLayerInfo layer : handle.getFlatLayers()) {
      BlockState state = this.blockStateFactory.create(layer.getLayerMaterial());
      result.addLayer(this.layerFactory.create(state, layer.getLayerCount()));
    }

    handle.getWorldFeatures().forEach((structure, options) -> {
      FlatWorldStructure structureEnum = STRUCTURES.get(structure);
      if (structureEnum == null) {
        return;
      }

      result.addStructure(structureEnum);

      options.forEach((option, value) -> {
        try {
          StructureOption optionEnum = StructureOption.valueOf(option.toUpperCase());
          result.setStructureOption(structureEnum, optionEnum, Integer.parseInt(value));
        } catch (IllegalArgumentException ignored) {
        }
      });
    });

    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object toMinecraftFlatSettings(FlatWorldGeneratorSettings settings) {
    FlatGenerationSettings result = new FlatGenerationSettings();

    Biome biome = Registry.BIOME.getOrDefault(settings.getBiome() == null
        ? new ResourceLocation("")
        : settings.getBiome().getName().getHandle());
    result.setBiome(biome);

    for (FlatWorldLayer layer : settings.getLayers()) {
      Block block = layer.getBlockState().getType().getHandle();
      result.getFlatLayers().add(new FlatLayerInfo(layer.getLayerHeight(), block));
    }

    for (FlatWorldStructure structure : settings.getStructures()) {
      String structureHandle = STRUCTURES.inverse().get(structure);
      if (structureHandle == null) {
        throw new IllegalStateException(
            "Structure " + structure + " is not supported in this version");
      }

      Map<String, String> options = new HashMap<>();
      result.getWorldFeatures().put(structureHandle, options);

      for (StructureOption option : OPTIONS) {
        if (!settings.hasStructureOption(structure, option)) {
          continue;
        }

        int value = settings.getStructureOption(structure, option);
        options.put(option.name().toLowerCase(), String.valueOf(value));
      }
    }

    return result;
  }
}
