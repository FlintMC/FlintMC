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

package net.flintmc.mcapi.v1_16_5.world.biome;

import com.google.inject.Singleton;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.world.biome.BiomeCategory;
import net.flintmc.mcapi.world.biome.BiomeMapper;
import net.flintmc.mcapi.world.biome.RainType;
import net.flintmc.mcapi.world.biome.TemperatureCategory;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.Category;

@Singleton
@Implement(value = BiomeMapper.class, version = "1.16.5")
public class VersionedBiomeMapper implements BiomeMapper {

  /**
   * {@inheritDoc}
   */
  @Override
  public BiomeCategory fromMinecraftBiomeCategory(Object category) {
    switch ((Category) category) {
      case NONE:
        return BiomeCategory.NONE;
      case TAIGA:
        return BiomeCategory.TAIGA;
      case EXTREME_HILLS:
        return BiomeCategory.EXTREME_HILLS;
      case JUNGLE:
        return BiomeCategory.JUNGLE;
      case MESA:
        return BiomeCategory.MESA;
      case PLAINS:
        return BiomeCategory.PLAINS;
      case SAVANNA:
        return BiomeCategory.SAVANNA;
      case ICY:
        return BiomeCategory.ICY;
      case THEEND:
        return BiomeCategory.THE_END;
      case BEACH:
        return BiomeCategory.BEACH;
      case FOREST:
        return BiomeCategory.FOREST;
      case OCEAN:
        return BiomeCategory.OCEAN;
      case DESERT:
        return BiomeCategory.DESERT;
      case RIVER:
        return BiomeCategory.RIVER;
      case SWAMP:
        return BiomeCategory.SWAMP;
      case MUSHROOM:
        return BiomeCategory.MUSHROOM;
      case NETHER:
        return BiomeCategory.NETHER;
      default:
        throw new IllegalStateException("Unexpected value: " + category);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object toMinecraftBiomeCategory(BiomeCategory category) {
    switch (category) {
      case NONE:
        return Category.NONE;
      case TAIGA:
        return Category.TAIGA;
      case EXTREME_HILLS:
        return Category.EXTREME_HILLS;
      case JUNGLE:
        return Category.JUNGLE;
      case MESA:
        return Category.MESA;
      case PLAINS:
        return Category.PLAINS;
      case SAVANNA:
        return Category.SAVANNA;
      case ICY:
        return Category.ICY;
      case THE_END:
        return Category.THEEND;
      case BEACH:
        return Category.BEACH;
      case FOREST:
        return Category.FOREST;
      case OCEAN:
        return Category.OCEAN;
      case DESERT:
        return Category.DESERT;
      case RIVER:
        return Category.RIVER;
      case SWAMP:
        return Category.SWAMP;
      case MUSHROOM:
        return Category.MUSHROOM;
      case NETHER:
        return Category.NETHER;
      default:
        throw new IllegalStateException("Unexpected value: " + category);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public RainType fromMinecraftRainType(Object rainType) {
    switch ((Biome.RainType) rainType) {
      case NONE:
        return RainType.NONE;
      case RAIN:
        return RainType.RAIN;
      case SNOW:
        return RainType.SNOW;
      default:
        throw new IllegalStateException("Unexpected value: " + rainType);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object toMinecraftRainType(RainType rainType) {
    switch (rainType) {
      case NONE:
        return Biome.RainType.NONE;
      case RAIN:
        return Biome.RainType.RAIN;
      case SNOW:
        return Biome.RainType.SNOW;
      default:
        throw new IllegalStateException("Unexpected value: " + rainType);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public TemperatureCategory fromMinecraftTemperatureCategory(Object category) {
    throw new UnsupportedOperationException("Not supported in 1.16.5");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object toMinecraftTemperatureCategory(TemperatureCategory category) {
    throw new UnsupportedOperationException("Not supported in 1.16.5");
  }
}
