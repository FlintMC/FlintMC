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

package net.flintmc.mcapi.v1_16_5.world.generator.flat;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.world.generator.WorldGeneratorMapper;
import net.flintmc.mcapi.world.generator.flat.FlatWorldGeneratorSettings;
import net.flintmc.mcapi.world.generator.flat.FlatWorldGeneratorSettingsSerializer;
import net.minecraft.client.gui.screen.FlatPresetsScreen;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.FlatGenerationSettings;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;

@Singleton
@Implement(FlatWorldGeneratorSettingsSerializer.class)
public class VersionedFlatWorldGeneratorSettingsSerializer
    implements FlatWorldGeneratorSettingsSerializer {

  private final FlatPresetsShadow flatPresets;
  private final WorldGeneratorMapper mapper;

  @Inject
  private VersionedFlatWorldGeneratorSettingsSerializer(WorldGeneratorMapper mapper) {
    this.flatPresets = (FlatPresetsShadow) new FlatPresetsScreen(null);
    this.mapper = mapper;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String serialize(FlatWorldGeneratorSettings settings) {
    DynamicRegistries registries = DynamicRegistries.func_239770_b_();
    Registry<Biome> biomeRegistry = registries.getRegistry(Registry.BIOME_KEY);

    FlatGenerationSettings handle =
        (FlatGenerationSettings) this.mapper.toMinecraftFlatSettings(settings);

    return this.flatPresets.func_243303_b(biomeRegistry, handle);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public FlatWorldGeneratorSettings deserialize(String serialized) {
    DynamicRegistries.Impl registries = DynamicRegistries.func_239770_b_();
    Registry<Biome> biomeRegistry = registries.getRegistry(Registry.BIOME_KEY);

    DimensionStructuresSettings structures =
        new DimensionStructuresSettings(false /* false = No Stronghold */);
    FlatGenerationSettings base = new FlatGenerationSettings(structures, biomeRegistry);

    FlatGenerationSettings parsed =
        FlatPresetsScreen.func_243299_a(biomeRegistry, serialized, base);

    return this.mapper.fromMinecraftFlatSettings(parsed);
  }
}
