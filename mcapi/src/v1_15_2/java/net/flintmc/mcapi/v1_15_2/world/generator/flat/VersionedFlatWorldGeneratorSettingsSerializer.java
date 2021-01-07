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

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.world.generator.WorldGeneratorMapper;
import net.flintmc.mcapi.world.generator.flat.FlatWorldGeneratorSettings;
import net.flintmc.mcapi.world.generator.flat.FlatWorldGeneratorSettingsSerializer;
import net.minecraft.world.gen.FlatGenerationSettings;

@Singleton
@Implement(value = FlatWorldGeneratorSettingsSerializer.class, version = "1.15.2")
public class VersionedFlatWorldGeneratorSettingsSerializer
    implements FlatWorldGeneratorSettingsSerializer {

  private final WorldGeneratorMapper mapper;

  @Inject
  private VersionedFlatWorldGeneratorSettingsSerializer(WorldGeneratorMapper mapper) {
    this.mapper = mapper;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String serialize(FlatWorldGeneratorSettings settings) {
    FlatGenerationSettings handle =
        (FlatGenerationSettings) this.mapper.toMinecraftFlatSettings(settings);
    return handle.toString();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public FlatWorldGeneratorSettings deserialize(String serialized) {
    FlatGenerationSettings handle =
        FlatGenerationSettings.createFlatGeneratorFromString(serialized);
    return this.mapper.fromMinecraftFlatSettings(handle);
  }
}
