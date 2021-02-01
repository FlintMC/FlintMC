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

import net.flintmc.mcapi.world.generator.flat.FlatWorldGeneratorSettingsSerializer;
import net.flintmc.transform.shadow.Shadow;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.FlatGenerationSettings;

@Shadow("net.minecraft.client.gui.screen.FlatPresetsScreen")
public interface FlatPresetsShadow {

  /**
   * Serializes the given settings into a string for {@link FlatWorldGeneratorSettingsSerializer#deserialize(String)}
   *
   * @param registry The non-null registry for biomes
   * @param settings The non-null settings to be serialized
   * @return The new non-null serialized string of the given settings
   */
  String func_243303_b(Registry<Biome> registry, FlatGenerationSettings settings);

}
