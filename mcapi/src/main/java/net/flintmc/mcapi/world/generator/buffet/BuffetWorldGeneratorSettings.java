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

package net.flintmc.mcapi.world.generator.buffet;

import javax.annotation.Nullable;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.world.biome.Biome;
import net.flintmc.mcapi.world.biome.BiomeRegistry;
import net.flintmc.mcapi.world.generator.WorldGeneratorBuilder;
import net.flintmc.mcapi.world.type.WorldType;
import net.flintmc.mcapi.world.type.WorldTypeRegistry;

/**
 * Settings for the buffet world generator with the {@link WorldType} buffet.
 *
 * @see WorldGeneratorBuilder
 * @see WorldTypeRegistry#getBuffetType()
 */
public interface BuffetWorldGeneratorSettings {

  /**
   * Retrieves the biome that has been set for this generator settings. If not set, the default
   * biome of Minecraft will be used.
   *
   * @return The non-null biome that will be used for this generator
   */
  Biome getBiome();

  /**
   * Sets the biome of this generator settings. If not set, the default biome of Minecraft will be
   * used.
   *
   * @param biome The new biome to be used or {@code null} to use the default biome of Minecraft
   * @return This settings object for chaining
   */
  BuffetWorldGeneratorSettings setBiome(Biome biome);

  /**
   * Retrieves the buffet type of this generator settings.
   *
   * @return The non-null type of this generator settings
   */
  BuffetWorldGeneratorType getType();

  /**
   * Sets the buffet type of this generator settings.
   *
   * @param type The new non-null type
   * @return This settings object for chaining
   */
  BuffetWorldGeneratorSettings setType(BuffetWorldGeneratorType type);

  /**
   * Validates if this settings object is valid to be used to generate a new world.
   *
   * @return This settings object for chaining
   * @throws NullPointerException If {@link #getType()} is {@code null}
   */
  BuffetWorldGeneratorSettings validate();

  /**
   * Factory for the {@link BuffetWorldGeneratorSettings}.
   */
  @AssistedFactory(BuffetWorldGeneratorSettings.class)
  interface Factory {

    /**
     * Creates a new {@link BuffetWorldGeneratorSettings} object with the {@link
     * BiomeRegistry#getDefaultBiome() default biome} and {@link BuffetWorldGeneratorType#SURFACE}
     * type.
     *
     * @return The new non-null {@link BuffetWorldGeneratorSettings}
     */
    BuffetWorldGeneratorSettings createDefault();

    /**
     * Creates a new {@link BuffetWorldGeneratorSettings} object.
     *
     * @param biome The biome to be used or {@code null} to use the {@link
     *              BiomeRegistry#getDefaultBiome() default biome}
     * @param type  The non-null buffet type
     * @return The new non-null {@link BuffetWorldGeneratorSettings}
     */
    BuffetWorldGeneratorSettings create(
        @Assisted @Nullable Biome biome, @Assisted BuffetWorldGeneratorType type);

  }

}
