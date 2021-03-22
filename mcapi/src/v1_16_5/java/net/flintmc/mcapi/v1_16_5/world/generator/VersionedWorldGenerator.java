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

package net.flintmc.mcapi.v1_16_5.world.generator;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.util.Collection;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.server.ServerController;
import net.flintmc.mcapi.world.generator.ExtendedWorldGeneratorSettings;
import net.flintmc.mcapi.world.generator.WorldGenerator;
import net.flintmc.mcapi.world.generator.WorldGeneratorBuilder;
import net.flintmc.mcapi.world.generator.WorldGeneratorMapper;
import net.flintmc.mcapi.world.generator.buffet.BuffetWorldGeneratorSettings;
import net.flintmc.mcapi.world.generator.flat.FlatWorldGeneratorSettings;
import net.flintmc.mcapi.world.type.WorldType;
import net.flintmc.mcapi.world.type.WorldTypeRegistry;
import net.flintmc.util.mappings.ClassMappingProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.BiomeGeneratorTypeScreens;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.Dimension;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.biome.provider.SingleBiomeProvider;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.FlatChunkGenerator;
import net.minecraft.world.gen.FlatGenerationSettings;
import net.minecraft.world.gen.settings.DimensionGeneratorSettings;

@Singleton
@Implement(WorldGenerator.class)
public class VersionedWorldGenerator implements WorldGenerator {

  private final ServerController serverController;
  private final WorldTypeRegistry typeRegistry;
  private final WorldGeneratorMapper mapper;
  private final Collection<BiomeGeneratorTypeScreens> minecraftWorldTypes;

  @Inject
  private VersionedWorldGenerator(
      ServerController serverController,
      WorldTypeRegistry typeRegistry,
      WorldGeneratorMapper mapper,
      ClassMappingProvider mappingProvider)
      throws IllegalAccessException, NoSuchFieldException, ClassNotFoundException {
    this.serverController = serverController;
    this.typeRegistry = typeRegistry;
    this.mapper = mapper;

    this.minecraftWorldTypes = mappingProvider
        .get(BiomeGeneratorTypeScreens.class.getName())
        .getField("field_239068_c_")
        .getValue(null);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void generateAndJoin(WorldGeneratorBuilder builder) {
    builder.validate();

    ExtendedWorldGeneratorSettings extended = builder.extended();
    DynamicRegistries.Impl registries = DynamicRegistries.func_239770_b_();

    BiomeGeneratorTypeScreens screens = this.getMinecraftWorldType(builder);

    DimensionGeneratorSettings generatorSettings =
        screens.func_241220_a_(registries, extended.seed(), extended.generateStructures(),
            extended.bonusChest());

    Registry<Dimension> registry = generatorSettings.func_236224_e_();
    Dimension overworld = registry.getValueForKey(Dimension.OVERWORLD);
    // cast to object because IntelliJ detects that Dimension is not an instance of DimensionShadow
    DimensionShadow shadow = (DimensionShadow) (Object) overworld;

    if (overworld.getChunkGenerator() instanceof FlatChunkGenerator) {
      ChunkGenerator generator = this.createFlatChunkGenerator(registries, builder.flatSettings());
      shadow.setChunkGenerator(generator);
    }

    if (builder.extended().type() == this.typeRegistry.getBuffetType()) {
      BiomeProvider provider = overworld.getChunkGenerator().getBiomeProvider();
      if (provider instanceof SingleBiomeProvider) {
        Biome biome = this.getBiome(registries, builder.buffetSettings());
        ((SingleBiomeProviderShadow) provider).setBiome(() -> biome);
      }
    }

    WorldSettings handle = (WorldSettings) this.mapper.toMinecraftGenerator(builder);

    this.serverController.disconnectFromServer();

    Minecraft.getInstance()
        .createWorld(builder.findFileName(), handle, registries, generatorSettings);
  }

  private Biome getBiome(DynamicRegistries registries, BuffetWorldGeneratorSettings settings) {
    net.flintmc.mcapi.world.biome.Biome flintBiome = settings.getBiome();

    Registry<Biome> biomeRegistry = registries.getRegistry(Registry.BIOME_KEY);
    for (Biome biome : biomeRegistry) {
      if (biomeRegistry.getKey(biome).equals(flintBiome.getName().getHandle())) {
        return biome;
      }
    }

    throw new IllegalArgumentException("Unknown biome: " + flintBiome.getName());
  }

  private ChunkGenerator createFlatChunkGenerator(
      DynamicRegistries registries, FlatWorldGeneratorSettings settings) {
    settings.setOption(VersionedWorldGeneratorMapper.REGISTRIES_OPTION, registries);

    FlatGenerationSettings handle =
        (FlatGenerationSettings) this.mapper.toMinecraftFlatSettings(settings);
    return new FlatChunkGenerator(handle);
  }

  private BiomeGeneratorTypeScreens getMinecraftWorldType(WorldGeneratorBuilder builder) {
    WorldType type = builder.extended().type();
    String typeName = type.getName();
    if (type == this.typeRegistry.getBuffetType()) {
      switch (builder.buffetSettings().getType()) {
        case CAVES:
          typeName = "single_biome_caves";
          break;
        case SURFACE:
          typeName = "single_biome_surface";
          break;
        case FLOATING_ISLANDS:
          typeName = "single_biome_floating_islands";
          break;
        default:
          throw new IllegalStateException(
              "Unexpected value: " + builder.buffetSettings().getType());
      }
    }

    for (BiomeGeneratorTypeScreens screens : this.minecraftWorldTypes) {
      String translationKey = ((TranslationTextComponent) screens.func_239077_a_()).getKey();
      if (translationKey.replaceFirst("generator.", "").equals(typeName)) {
        return screens;
      }
    }

    throw new IllegalArgumentException("Unknown world type: " + typeName);
  }

}
