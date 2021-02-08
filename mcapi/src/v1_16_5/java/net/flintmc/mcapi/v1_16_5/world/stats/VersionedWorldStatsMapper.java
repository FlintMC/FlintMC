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

package net.flintmc.mcapi.v1_16_5.world.stats;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Multimap;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.util.HashMap;
import java.util.Map;
import net.flintmc.mcapi.chat.builder.TranslationComponentBuilder;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.entity.type.EntityType;
import net.flintmc.mcapi.entity.type.EntityTypeMapper;
import net.flintmc.mcapi.items.mapper.MinecraftItemMapper;
import net.flintmc.mcapi.items.type.ItemType;
import net.flintmc.mcapi.v1_16_5.world.stats.StatsScreenShadow.GeneralListShadow.GeneralStat;
import net.flintmc.mcapi.v1_16_5.world.stats.StatsScreenShadow.ItemListShadow;
import net.flintmc.mcapi.v1_16_5.world.stats.StatsScreenShadow.MobListShadow.MobStat;
import net.flintmc.mcapi.world.stats.StatsCategory;
import net.flintmc.mcapi.world.stats.WorldStatType;
import net.flintmc.mcapi.world.stats.WorldStats;
import net.flintmc.mcapi.world.stats.WorldStats.Factory;
import net.flintmc.mcapi.world.stats.value.GeneralWorldStat;
import net.flintmc.mcapi.world.stats.value.ItemWorldStat;
import net.flintmc.mcapi.world.stats.value.ItemWorldStat.ItemStatType;
import net.flintmc.mcapi.world.stats.value.MobWorldStat;
import net.flintmc.mcapi.world.stats.value.WorldStat;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.stats.Stat;
import net.minecraft.stats.StatType;
import net.minecraft.stats.StatisticsManager;
import net.minecraft.stats.Stats;

@Singleton
public class VersionedWorldStatsMapper {

  private static final Map<StatType<?>, ItemStatType> ITEM_STAT_TYPES = new HashMap<>();

  static {
    ITEM_STAT_TYPES.put(Stats.BLOCK_MINED, ItemStatType.BLOCK_MINED);
    ITEM_STAT_TYPES.put(Stats.ITEM_BROKEN, ItemStatType.ITEM_BROKEN);
    ITEM_STAT_TYPES.put(Stats.ITEM_CRAFTED, ItemStatType.ITEM_CRAFTED);
    ITEM_STAT_TYPES.put(Stats.ITEM_USED, ItemStatType.ITEM_USED);
    ITEM_STAT_TYPES.put(Stats.ITEM_PICKED_UP, ItemStatType.ITEM_PICKED_UP);
    ITEM_STAT_TYPES.put(Stats.ITEM_DROPPED, ItemStatType.ITEM_DROPPED);
  }

  private final Factory statsFactory;
  private final StatsCategory.Factory categoryFactory;
  private final GeneralWorldStat.Factory simpleStatFactory;
  private final MobWorldStat.Factory mobStatFactory;
  private final ItemWorldStat.Factory itemStatFactory;

  private final TranslationComponentBuilder.Factory componentBuilderFactory;
  private final EntityTypeMapper entityTypeMapper;
  private final MinecraftItemMapper itemMapper;

  private final StatsCategory generalCategory;
  private final StatsCategory mobCategory;
  private final StatsCategory itemCategory;
  private final Map<WorldStatType, StatsCategory> categories;

  @Inject
  private VersionedWorldStatsMapper(
      Factory statsFactory,
      StatsCategory.Factory categoryFactory,
      GeneralWorldStat.Factory simpleStatFactory,
      MobWorldStat.Factory mobStatFactory,
      ItemWorldStat.Factory itemStatFactory,
      TranslationComponentBuilder.Factory componentBuilderFactory,
      EntityTypeMapper entityTypeMapper,
      MinecraftItemMapper itemMapper) {
    this.statsFactory = statsFactory;
    this.componentBuilderFactory = componentBuilderFactory;
    this.categoryFactory = categoryFactory;
    this.simpleStatFactory = simpleStatFactory;
    this.mobStatFactory = mobStatFactory;
    this.itemStatFactory = itemStatFactory;
    this.entityTypeMapper = entityTypeMapper;
    this.itemMapper = itemMapper;

    this.generalCategory = this.createCategory(WorldStatType.GENERAL, "stat.generalButton");
    this.itemCategory = this.createCategory(WorldStatType.ITEM, "stat.itemsButton");
    this.mobCategory = this.createCategory(WorldStatType.MOB, "stat.mobsButton");

    this.categories = ImmutableMap.of(
        WorldStatType.GENERAL, this.generalCategory,
        WorldStatType.ITEM, this.itemCategory,
        WorldStatType.MOB, this.mobCategory
    );
  }

  public Map<WorldStatType, StatsCategory> getCategories() {
    return this.categories;
  }

  public WorldStats map(StatsScreenShadow shadow) {
    Multimap<WorldStatType, WorldStat> stats = HashMultimap.create();

    this.buildSimpleStats(stats, shadow);
    this.buildMobStats(stats, shadow);
    this.buildItemStats(stats, shadow);

    return this.statsFactory.create(System.currentTimeMillis(), stats);
  }

  private void buildSimpleStats(
      Multimap<WorldStatType, WorldStat> target, StatsScreenShadow shadow) {
    for (GeneralStat stat : shadow.getGeneralStats().getStats()) {
      int value = shadow.getStatsManager().getValue(stat.getStat());
      ChatComponent displayName = this.componentBuilderFactory.newBuilder()
          .translationKey("stat." + stat.getStat().getValue().toString().replace(':', '.'))
          .build();

      target.put(WorldStatType.GENERAL, this.simpleStatFactory
          .create(this.generalCategory, displayName, stat.getStat().format(value), value));
    }
  }

  private void buildMobStats(Multimap<WorldStatType, WorldStat> target, StatsScreenShadow shadow) {
    for (MobStat stat : shadow.getMobStats().getStats()) {
      net.minecraft.entity.EntityType<?> rawEntityType = stat.getEntityType();
      EntityType entityType = this.entityTypeMapper.fromMinecraftEntityType(rawEntityType);

      int killed = shadow.getStatsManager().getValue(Stats.ENTITY_KILLED.get(rawEntityType));
      int killedBy = shadow.getStatsManager().getValue(Stats.ENTITY_KILLED_BY.get(rawEntityType));
      ChatComponent killedComponent = this.buildMobComponent(
          entityType, Stats.ENTITY_KILLED.getTranslationKey(), true, killed);
      ChatComponent killedByComponent = this.buildMobComponent(
          entityType, Stats.ENTITY_KILLED_BY.getTranslationKey(), false, killedBy);

      MobWorldStat worldStat = this.mobStatFactory.create(
          this.mobCategory, entityType, killedComponent, killedByComponent, killed, killedBy);
      target.put(WorldStatType.MOB, worldStat);
    }
  }

  private void buildItemStats(Multimap<WorldStatType, WorldStat> target, StatsScreenShadow shadow) {
    ItemListShadow items = shadow.getItemStats();

    for (Item item : items.getAvailableItems()) {
      Map<ItemStatType, Integer> values = new HashMap<>();
      Map<ItemStatType, String> formattedValues = new HashMap<>();

      if (item instanceof BlockItem) {
        Block block = ((BlockItem) item).getBlock();
        for (StatType<Block> type : items.getBlockStatTypes()) {
          this.handleItemStat(values, formattedValues, shadow.getStatsManager(), type, block);
        }
      }

      for (StatType<Item> type : items.getItemStatTypes()) {
        this.handleItemStat(values, formattedValues, shadow.getStatsManager(), type, item);
      }

      ItemType type = this.itemMapper.fromMinecraftType(item);
      target.put(WorldStatType.ITEM,
          this.itemStatFactory.create(this.itemCategory, type, values, formattedValues));
    }
  }

  private <T> void handleItemStat(
      Map<ItemStatType, Integer> target,
      Map<ItemStatType, String> formattedTarget,
      StatisticsManager statsManager, StatType<T> type, T item) {
    Stat<T> stat = type.get(item);
    int value = statsManager.getValue(stat);
    if (value <= 0) {
      return;
    }

    ItemStatType flintType = ITEM_STAT_TYPES.get(type);
    if (flintType != null) {
      target.put(flintType, value);
      formattedTarget.put(flintType, stat.format(value));
    }
  }

  private ChatComponent buildMobComponent(EntityType entityType, String translationKey,
      boolean valueFirst, int value) {
    TranslationComponentBuilder builder = this.componentBuilderFactory.newBuilder()
        .translationKey(translationKey + (value == 0 ? ".none" : ""));

    if (valueFirst && value != 0) {
      // For the killed value the value is added before the display name
      builder.appendArgument(String.valueOf(value));
    }
    builder.appendArgument(entityType.getDisplayName());
    if (!valueFirst && value != 0) {
      // For the killedBy value the value is added after the display name
      builder.appendArgument(String.valueOf(value));
    }

    return builder.build();
  }

  private StatsCategory createCategory(WorldStatType type, String translationKey) {
    ChatComponent component = this.componentBuilderFactory
        .newBuilder().translationKey(translationKey).build();
    return this.categoryFactory.create(type, component);
  }
}
