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

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.internal.world.generator.WorldGeneratorSettingsImplementation;
import net.flintmc.mcapi.world.generator.WorldGeneratorMapper;
import net.flintmc.mcapi.world.generator.flat.FlatWorldGeneratorSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.util.FileUtil;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.FlatGenerationSettings;
import java.nio.file.Path;

@Singleton
@Implement(value = WorldGeneratorSettingsImplementation.class, version = "1.16.4")
public class VersionedWorldGeneratorSettingsImplementation
    implements WorldGeneratorSettingsImplementation {

  private final WorldGeneratorMapper mapper;

  @Inject
  private VersionedWorldGeneratorSettingsImplementation(WorldGeneratorMapper mapper) {
    this.mapper = mapper;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public FlatWorldGeneratorSettings createDefault() {
    DynamicRegistries registries = DynamicRegistries.func_239770_b_();
    Registry<Biome> registry = registries.getRegistry(Registry.BIOME_KEY);

    FlatGenerationSettings settings = FlatGenerationSettings.func_242869_a(registry);
    return this.mapper.fromMinecraftFlatSettings(settings);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String findFileName(String worldName) {
    String saveDirName = worldName.isEmpty() ? "World" : worldName;

    Path directory = Minecraft.getInstance().getSaveLoader().getSavesDir();

    try {
      saveDirName = FileUtil.findAvailableName(directory, saveDirName, "");
    } catch (Exception var4) {
      saveDirName = "World";

      try {
        saveDirName = FileUtil.findAvailableName(directory, saveDirName, "");
      } catch (Exception exception) {
        throw new RuntimeException("Could not create save folder", exception);
      }
    }

    return saveDirName;
  }
}
