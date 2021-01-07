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

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.resources.ResourceLocationProvider;
import net.flintmc.mcapi.world.biome.BiomeRegistry;
import net.flintmc.mcapi.world.block.BlockState;
import net.flintmc.mcapi.world.block.BlockTypeRegistry;
import net.flintmc.mcapi.world.generator.WorldGameMode;
import net.flintmc.mcapi.world.generator.WorldGeneratorBuilder;
import net.flintmc.mcapi.world.generator.WorldGeneratorBuilder.Factory;
import net.flintmc.mcapi.world.generator.WorldGeneratorMapper;
import net.flintmc.mcapi.world.generator.buffet.BuffetWorldGeneratorSettings;
import net.flintmc.mcapi.world.generator.flat.FlatWorldGeneratorSettings;
import net.flintmc.mcapi.world.generator.flat.FlatWorldLayer;
import net.flintmc.mcapi.world.generator.flat.FlatWorldStructure;
import net.flintmc.mcapi.world.generator.flat.StructureOption;
import net.flintmc.mcapi.world.type.WorldTypeRegistry;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.GameType;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.FlatGenerationSettings;
import net.minecraft.world.gen.FlatLayerInfo;

@Singleton
@Implement(value = WorldGeneratorMapper.class, version = "1.15.2")
public class VersionedWorldGeneratorMapper implements WorldGeneratorMapper {

  private static final StructureOption[] OPTIONS = StructureOption.values();

  private final BiomeRegistry biomeRegistry;
  private final ResourceLocationProvider provider;
  private final BlockState.Factory blockStateFactory;
  private final FlatWorldLayer.Factory layerFactory;
  private final WorldTypeRegistry typeRegistry;
  private final WorldGeneratorBuilder.Factory builderFactory;
  private final FlatWorldGeneratorSettings.Factory flatSettingsFactory;

  @Inject
  private VersionedWorldGeneratorMapper(
      BiomeRegistry biomeRegistry,
      ResourceLocationProvider provider,
      BlockState.Factory blockStateFactory,
      FlatWorldLayer.Factory layerFactory,
      WorldTypeRegistry typeRegistry,
      Factory builderFactory,
      FlatWorldGeneratorSettings.Factory flatSettingsFactory) {
    this.biomeRegistry = biomeRegistry;
    this.provider = provider;
    this.blockStateFactory = blockStateFactory;
    this.layerFactory = layerFactory;
    this.typeRegistry = typeRegistry;
    this.builderFactory = builderFactory;
    this.flatSettingsFactory = flatSettingsFactory;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public WorldGeneratorBuilder fromMinecraftGenerator(Object generator) {
    if (!(generator instanceof WorldSettings)) {
      throw new IllegalArgumentException(
          "Object needs to be an instance of " + WorldSettings.class.getName());
    }

    WorldSettings settings = (WorldSettings) generator;

    WorldGeneratorBuilder builder = this.builderFactory.newBuilder();

    builder.extended()
        .seed(settings.getSeed())
        .mode(settings.getHardcoreEnabled() ? WorldGameMode.HARDCORE
            : settings.getGameType() == GameType.CREATIVE ? WorldGameMode.CREATIVE
                : WorldGameMode.SURVIVAL)
        .type(this.typeRegistry.getType(settings.getTerrainType().getName()))
        .generateStructures(settings.isMapFeaturesEnabled())
        .allowCheats(settings.areCommandsAllowed())
        .bonusChest(settings.isBonusChestEnabled());

    // TODO read json

    return builder;
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
      JsonObject structureJson = new JsonObject();
      structures.add(this.mapStructure(structure), structureJson);

      for (StructureOption option : OPTIONS) {
        if (!settings.hasStructureOption(structure, option)) {
          continue;
        }

        int value = settings.getStructureOption(structure, option);
        structureJson.addProperty(option.name().toLowerCase(), value);
      }
    }
  }

  private FlatWorldStructure mapStructure(String structure) {
    switch (structure) {
      case "mineshaft":
        return FlatWorldStructure.MINESHAFT;
      case "village":
        return FlatWorldStructure.VILLAGE;
      case "stronghold":
        return FlatWorldStructure.STRONGHOLD;
      case "swamp_hut":
        return FlatWorldStructure.SWAMP_HUT;
      case "desert_pyramid":
        return FlatWorldStructure.DESERT_PYRAMID;
      case "jungle_temple":
        return FlatWorldStructure.JUNGLE_TEMPLE;
      case "igloo":
        return FlatWorldStructure.IGLOO;
      case "shipwreck":
        return FlatWorldStructure.SHIPWRECK;
      case "ocean_ruin":
        return FlatWorldStructure.OCEAN_RUIN;
      case "lake":
        return FlatWorldStructure.WATER_LAKE;
      case "lava_lake":
        return FlatWorldStructure.LAVA_LAKE;
      case "endcity":
        return FlatWorldStructure.END_CITY;
      case "mansion":
        return FlatWorldStructure.MANSION;
      case "fortress":
        return FlatWorldStructure.FORTRESS;
      case "biome_1":
        return FlatWorldStructure.BIOME;
      case "oceanmonument":
        return FlatWorldStructure.OCEANMONUMENT;
      case "pillager_outpost":
        return FlatWorldStructure.PILLAGER_OUTPOST;
      case "dungeon":
        return FlatWorldStructure.DUNGEON;
      case "decoration":
        return FlatWorldStructure.DECORATION;
      default:
        return null;
    }
  }

  private String mapStructure(FlatWorldStructure structure) {
    switch (structure) {
      case MINESHAFT:
        return "mineshaft";
      case VILLAGE:
        return "village";
      case STRONGHOLD:
        return "stronghold";
      case SWAMP_HUT:
        return "swamp_hut";
      case DESERT_PYRAMID:
        return "desert_pyramid";
      case JUNGLE_TEMPLE:
        return "jungle_temple";
      case IGLOO:
        return "igloo";
      case SHIPWRECK:
        return "shipwreck";
      case OCEAN_RUIN:
        return "ocean_ruin";
      case WATER_LAKE:
        return "lake";
      case LAVA_LAKE:
        return "lava_lake";
      case END_CITY:
        return "endcity";
      case MANSION:
        return "mansion";
      case FORTRESS:
        return "fortress";
      case BIOME:
        return "biome_1";
      case OCEANMONUMENT:
        return "oceanmonument";
      case PILLAGER_OUTPOST:
        return "pillager_outpost";
      case DUNGEON:
        return "dungeon";
      case DECORATION:
        return "decoration";
      default:
        throw new IllegalStateException(
            "Structure " + structure + " is not supported in this version");
    }
  }

  private void appendBuffetSettings(JsonObject json, BuffetWorldGeneratorSettings settings) {

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
      FlatWorldStructure structureEnum = this.mapStructure(structure);
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

    return result;
  }
}
