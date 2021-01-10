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

package net.flintmc.mcapi.v1_16_4.world.biome;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import java.util.List;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.chat.builder.ComponentBuilder;
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
import net.minecraft.util.text.LanguageMap;
import net.minecraft.world.biome.MobSpawnInfo.Spawners;

@Implement(value = Biome.class, version = "1.16.4")
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
      ComponentBuilder.Factory componentFactory,
      @Assisted ResourceLocation name,
      @Assisted("handle") Object handle) {
    this.world = world;
    this.entityTypeMapper = entityTypeMapper;
    this.spawnRateFactory = spawnRateFactory;
    this.name = name;
    this.handle = (net.minecraft.world.biome.Biome) handle;

    this.category = biomeMapper.fromMinecraftBiomeCategory(this.handle.getCategory());
    this.rainType = biomeMapper.fromMinecraftRainType(this.handle.getPrecipitation());
    this.temperatureCategory = this.parseTemperatureCategory(this.handle.getTemperature());
    this.displayName = this.createDisplayName(componentFactory);

    this.spawnRates = HashMultimap.create();
    this.initSpawnRates();
  }

  private ChatComponent createDisplayName(ComponentBuilder.Factory factory) {
    String key = "biome." + this.name.getNamespace() + '.' + this.name.getPath();

    if (LanguageMap.getInstance().func_230506_b_(key)) {
      return factory.translation().translationKey(key).build();
    } else {
      return factory.text().text(this.name.toString()).build();
    }
  }

  private TemperatureCategory parseTemperatureCategory(float temperature) {
    if (this.handle.getCategory() == net.minecraft.world.biome.Biome.Category.OCEAN) {
      return TemperatureCategory.OCEAN;
    } else if (temperature < 0.2D) {
      return TemperatureCategory.COLD;
    } else if (temperature < 1.0D) {
      return TemperatureCategory.MEDIUM;
    } else {
      return TemperatureCategory.WARM;
    }
  }

  private void initSpawnRates() {
    for (EntityClassification classification : CLASSIFICATIONS) {
      List<Spawners> spawners = this.handle.getMobSpawnInfo().getSpawners(classification);
      if (spawners == null) {
        continue;
      }

      Classification flintClassification =
          this.entityTypeMapper.fromMinecraftEntityClassification(classification);

      for (Spawners entry : spawners) {
        EntityType type = this.entityTypeMapper.fromMinecraftEntityType(entry.type);
        int weight = ((WeightedRandomItemShadow) entry).getWeight();

        BiomeEntitySpawnRate spawnRate =
            this.spawnRateFactory.create(type, weight, entry.minCount, entry.maxCount);
        this.spawnRates.put(flintClassification, spawnRate);
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ResourceLocation getName() {
    return this.name;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getGrassColor(double x, double z) {
    return this.handle.getGrassColor(x, z);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getFoliageColor() {
    return this.handle.getFoliageColor();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public RainType getRainType() {
    return this.rainType;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ChatComponent getDisplayName() {
    return this.displayName;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public TemperatureCategory getTemperatureCategory() {
    return this.temperatureCategory;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getDefaultTemperature() {
    return this.handle.getTemperature();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getTemperature(BlockPosition position) {
    return this.handle.getTemperature((BlockPos) this.world.toMinecraftBlockPos(position));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getHumidity() {
    return this.handle.getDownfall();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isHighHumidity() {
    return this.handle.isHighHumidity();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getWaterColor() {
    return this.handle.getWaterColor();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getWaterFogColor() {
    return this.handle.getWaterFogColor();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BiomeCategory getCategory() {
    return this.category;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Multimap<Classification, BiomeEntitySpawnRate> getSpawnRates() {
    return this.spawnRates;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getSpawningChance() {
    return this.handle.getMobSpawnInfo().getCreatureSpawnProbability();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getSkyColor() {
    return this.handle.getSkyColor();
  }
}
