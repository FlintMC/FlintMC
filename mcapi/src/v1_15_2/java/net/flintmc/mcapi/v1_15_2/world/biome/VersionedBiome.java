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

package net.flintmc.mcapi.v1_15_2.world.biome;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.chat.MinecraftComponentMapper;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.entity.Entity.Classification;
import net.flintmc.mcapi.entity.type.EntityType;
import net.flintmc.mcapi.entity.type.EntityTypeMapper;
import net.flintmc.mcapi.resources.ResourceLocation;
import net.flintmc.mcapi.world.World;
import net.flintmc.mcapi.world.biome.Biome;
import net.flintmc.mcapi.world.biome.BiomeCategory;
import net.flintmc.mcapi.world.biome.BiomeEntitySpawnRate;
import net.flintmc.mcapi.world.biome.BiomeMapper;
import net.flintmc.mcapi.world.biome.RainType;
import net.flintmc.mcapi.world.biome.TemperatureCategory;
import net.flintmc.mcapi.world.math.BlockPosition;
import net.minecraft.entity.EntityClassification;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome.SpawnListEntry;

import java.util.List;

@Implement(value = Biome.class, version = "1.15.2")
public class VersionedBiome implements Biome {

  private static final EntityClassification[] CLASSIFICATIONS = EntityClassification.values();

  private final World world;
  private final EntityTypeMapper entityTypeMapper;
  private final BiomeEntitySpawnRate.Factory spawnRateFactory;

  private final ResourceLocation name;
  private final BiomeCategory category;
  private final RainType rainType;
  private final TemperatureCategory temperatureCategory;
  private final ChatComponent displayName;
  private final Multimap<Classification, BiomeEntitySpawnRate> spawnRates;
  private final net.minecraft.world.biome.Biome handle;

  @AssistedInject
  public VersionedBiome(
      World world,
      EntityTypeMapper entityTypeMapper,
      BiomeEntitySpawnRate.Factory spawnRateFactory,
      BiomeMapper biomeMapper,
      MinecraftComponentMapper componentMapper,
      @Assisted ResourceLocation name,
      @Assisted("handle") Object handle) {
    this.world = world;
    this.entityTypeMapper = entityTypeMapper;
    this.spawnRateFactory = spawnRateFactory;
    this.name = name;
    this.handle = (net.minecraft.world.biome.Biome) handle;

    this.category = biomeMapper.fromMinecraftBiomeCategory(this.handle.getCategory());
    this.rainType = biomeMapper.fromMinecraftRainType(this.handle.getPrecipitation());
    this.temperatureCategory = biomeMapper
        .fromMinecraftTemperatureCategory(this.handle.getTempCategory());
    this.displayName = componentMapper.fromMinecraft(this.handle.getDisplayName());

    this.spawnRates = HashMultimap.create();
    this.initSpawnRates();
  }

  private void initSpawnRates() {
    for (EntityClassification classification : CLASSIFICATIONS) {
      List<SpawnListEntry> entries = this.handle.getSpawns(classification);
      if (entries == null) {
        continue;
      }

      Classification flintClassification =
          this.entityTypeMapper.fromMinecraftEntityClassification(classification);

      for (SpawnListEntry entry : entries) {
        EntityType type = this.entityTypeMapper.fromMinecraftEntityType(entry.entityType);
        int weight = ((WeightedRandomItemShadow) entry).getWeight();

        BiomeEntitySpawnRate spawnRate =
            this.spawnRateFactory.create(type, weight, entry.minGroupCount, entry.maxGroupCount);
        this.spawnRates.put(flintClassification, spawnRate);
      }
    }
  }

  @Override
  public ResourceLocation getName() {
    return this.name;
  }

  @Override
  public int getGrassColor(double x, double z) {
    return this.handle.getGrassColor(x, z);
  }

  @Override
  public int getFoliageColor() {
    return this.handle.getFoliageColor();
  }

  @Override
  public RainType getRainType() {
    return this.rainType;
  }

  @Override
  public ChatComponent getDisplayName() {
    return this.displayName;
  }

  @Override
  public TemperatureCategory getTemperatureCategory() {
    return this.temperatureCategory;
  }

  @Override
  public float getDefaultTemperature() {
    return this.handle.getDefaultTemperature();
  }

  @Override
  public float getTemperature(BlockPosition position) {
    return this.handle.getTemperature((BlockPos) this.world.toMinecraftBlockPos(position));
  }

  @Override
  public float getHumidity() {
    return this.handle.getDownfall();
  }

  @Override
  public boolean isHighHumidity() {
    return this.handle.isHighHumidity();
  }

  @Override
  public int getWaterColor() {
    return this.handle.getWaterColor();
  }

  @Override
  public int getWaterFogColor() {
    return this.handle.getWaterFogColor();
  }

  @Override
  public BiomeCategory getCategory() {
    return this.category;
  }

  @Override
  public Multimap<Classification, BiomeEntitySpawnRate> getSpawnRates() {
    return this.spawnRates;
  }

  @Override
  public float getSpawningChance() {
    return this.handle.getSpawningChance();
  }

  @Override
  public int getSkyColor() {
    return this.handle.getSkyColor();
  }
}
