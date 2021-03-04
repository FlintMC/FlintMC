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
import net.flintmc.mcapi.chat.builder.ComponentBuilder;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.items.ItemStack;
import net.flintmc.mcapi.items.mapper.MinecraftItemMapper;
import net.flintmc.mcapi.world.generator.WorldGeneratorMapper;
import net.flintmc.mcapi.world.generator.flat.FlatWorldGeneratorSettings;
import net.flintmc.mcapi.world.generator.flat.FlatWorldGeneratorSettingsSerializer;
import net.flintmc.mcapi.world.generator.flat.presets.FlatWorldPreset;
import net.flintmc.mcapi.world.generator.flat.presets.FlatWorldPresetRegistry;
import net.flintmc.util.mappings.ClassMappingProvider;
import net.minecraft.client.gui.screen.FlatPresetsScreen;
import net.minecraft.world.gen.FlatGenerationSettings;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Singleton
@Implement(value = FlatWorldPresetRegistry.class)
public class VersionedFlatWorldPresetRegistry implements FlatWorldPresetRegistry {

  private final Collection<FlatWorldPreset> presets;

  @Inject
  private VersionedFlatWorldPresetRegistry(
      FlatWorldGeneratorSettingsSerializer serializer,
      ComponentBuilder.Factory componentBuilderFactory,
      MinecraftItemMapper itemMapper,
      FlatWorldPreset.Factory presetFactory,
      ClassMappingProvider mappingProvider)
      throws ReflectiveOperationException {
    this.presets = new ArrayList<>();

    Object value = mappingProvider
        .get(FlatPresetsScreen.class.getName())
        .getField("FLAT_WORLD_PRESETS")
        .getValue(null);

    List<?> list = (List<?>) value;
    for (Object o : list) {
      FlatPresetsLayerShadow layer = (FlatPresetsLayerShadow) o;

      ChatComponent displayName = componentBuilderFactory.text().text(layer.getName()).build();
      ItemStack icon = itemMapper.fromMinecraft(layer.getIcon().getDefaultInstance());
      FlatWorldGeneratorSettings settings = serializer.deserialize(layer.getGeneratorInfo());

      this.addPreset(presetFactory.create(displayName, icon, settings));
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Collection<FlatWorldPreset> getPresets() {
    return this.presets;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addPreset(FlatWorldPreset preset) {
    this.presets.add(preset);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removePreset(FlatWorldPreset preset) {
    this.presets.remove(preset);
  }
}
