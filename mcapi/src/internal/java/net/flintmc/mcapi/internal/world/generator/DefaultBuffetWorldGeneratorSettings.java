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

package net.flintmc.mcapi.internal.world.generator;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.world.biome.Biome;
import net.flintmc.mcapi.world.biome.BiomeRegistry;
import net.flintmc.mcapi.world.generator.buffet.BuffetWorldGeneratorSettings;
import net.flintmc.mcapi.world.generator.buffet.BuffetWorldGeneratorType;
import net.flintmc.util.commons.Validate;

import javax.annotation.Nullable;

@Implement(BuffetWorldGeneratorSettings.class)
public class DefaultBuffetWorldGeneratorSettings implements BuffetWorldGeneratorSettings {

  private final BiomeRegistry biomeRegistry;

  private Biome biome;
  private BuffetWorldGeneratorType type;

  @AssistedInject
  public DefaultBuffetWorldGeneratorSettings(BiomeRegistry registry) {
    this(registry, null, BuffetWorldGeneratorType.SURFACE);
  }

  @AssistedInject
  public DefaultBuffetWorldGeneratorSettings(
      BiomeRegistry biomeRegistry,
      @Assisted @Nullable Biome biome,
      @Assisted BuffetWorldGeneratorType type) {
    this.biomeRegistry = biomeRegistry;
    this.biome = biome;
    this.type = type;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Biome getBiome() {
    if (this.biome == null) {
      this.biome = this.biomeRegistry.getDefaultBiome();
    }

    return this.biome;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BuffetWorldGeneratorSettings setBiome(Biome biome) {
    this.biome = biome;
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BuffetWorldGeneratorType getType() {
    return this.type;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BuffetWorldGeneratorSettings setType(BuffetWorldGeneratorType type) {
    this.type = type;
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BuffetWorldGeneratorSettings validate() {
    Validate.checkNotNull(this.type, "Invalid type provided");

    return this;
  }
}
