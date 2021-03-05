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

package net.flintmc.mcapi.v1_16_5.world.storage;

import com.beust.jcommander.internal.Lists;
import com.google.inject.Inject;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import com.google.inject.Singleton;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.chat.MinecraftComponentMapper;
import net.flintmc.mcapi.chat.component.TranslationComponent;
import net.flintmc.mcapi.world.type.WorldType;
import net.flintmc.mcapi.world.type.WorldTypeRegistry;
import net.flintmc.util.mappings.ClassMappingProvider;
import net.minecraft.client.gui.screen.BiomeGeneratorTypeScreens;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

@Singleton
@Implement(WorldTypeRegistry.class)
public class VersionedWorldTypeRegistry implements WorldTypeRegistry {

  private static final Collection<String> BUFFET_TYPES =
      Arrays.asList("single_biome_surface", "single_biome_caves", "single_biome_floating_islands");

  private final List<WorldType> worldTypes;

  @Inject
  private VersionedWorldTypeRegistry(
      WorldType.Factory typeFactory,
      MinecraftComponentMapper componentMapper,
      TranslationComponent.Factory componentFactory,
      ClassMappingProvider mappingProvider)
      throws IllegalAccessException, NoSuchFieldException, ClassNotFoundException {
    this.worldTypes = Lists.newArrayList();

    List<?> types = mappingProvider.get(BiomeGeneratorTypeScreens.class.getName())
        .getField("field_239068_c_")
        .getValue(null);

    for (Object type : types) {
      BiomeGeneratorTypeScreens screens = (BiomeGeneratorTypeScreens) type;

      ITextComponent component = screens.func_239077_a_();
      String name = ((TranslationTextComponent) component).getKey().replaceFirst("generator.", "");

      if (BUFFET_TYPES.contains(name)) {
        continue;
      }
      WorldType worldType = typeFactory.create(
          name,
          componentMapper.fromMinecraft(component),
          !name.equals("debug_all_block_states"),
          name.equals("debug_all_block_states"),
          name.equals("flat") || name.equals("customized")
      );

      this.worldTypes.add(worldType);
    }

    TranslationComponent buffetName = componentFactory.create();
    buffetName.translationKey("generator.buffet");
    this.worldTypes.add(typeFactory.create("buffet", buffetName, true, false, true));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<WorldType> getWorldTypes() {
    return this.worldTypes;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public WorldType getDefaultType() {
    return this.getType("default");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public WorldType getFlatType() {
    return this.getType("flat");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public WorldType getLargeBiomesType() {
    return this.getType("large_biomes");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public WorldType getAmplifiedType() {
    return this.getType("amplified");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public WorldType getBuffetType() {
    return this.getType("buffet");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public WorldType getType(String name) {
    for (WorldType type : this.worldTypes) {
      if (type.getName().equals(name)) {
        return type;
      }
    }
    return null;
  }
}
