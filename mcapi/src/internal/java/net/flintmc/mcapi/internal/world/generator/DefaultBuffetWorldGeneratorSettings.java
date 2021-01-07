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

import com.google.common.base.Preconditions;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.world.biome.Biome;
import net.flintmc.mcapi.world.biome.BiomeRegistry;
import net.flintmc.mcapi.world.generator.buffet.BuffetWorldGeneratorSettings;
import net.flintmc.mcapi.world.generator.buffet.BuffetWorldGeneratorType;

@Implement(BuffetWorldGeneratorSettings.class)
public class DefaultBuffetWorldGeneratorSettings implements BuffetWorldGeneratorSettings {

  private Biome biome;
  private BuffetWorldGeneratorType type;

  @AssistedInject
  public DefaultBuffetWorldGeneratorSettings(BiomeRegistry registry) {
    this(registry.getBiomes().iterator().next(), BuffetWorldGeneratorType.SURFACE);
  }

  @AssistedInject
  public DefaultBuffetWorldGeneratorSettings(
      @Assisted Biome biome, @Assisted BuffetWorldGeneratorType type) {
    this.biome = biome;
    this.type = type;
  }

  @Override
  public Biome getBiome() {
    return this.biome;
  }

  @Override
  public void setBiome(Biome biome) {
    this.biome = biome;
  }

  @Override
  public BuffetWorldGeneratorType getType() {
    return this.type;
  }

  @Override
  public void setType(BuffetWorldGeneratorType type) {
    this.type = type;
  }

  @Override
  public BuffetWorldGeneratorSettings validate() {
    Preconditions.checkNotNull(this.biome, "Invalid biome provided");
    Preconditions.checkNotNull(this.type, "Invalid type provided");

    return this;
  }
}
