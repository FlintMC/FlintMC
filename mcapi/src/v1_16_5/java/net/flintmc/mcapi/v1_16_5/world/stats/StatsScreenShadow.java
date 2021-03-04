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

import java.util.List;
import net.flintmc.transform.shadow.FieldGetter;
import net.flintmc.transform.shadow.MethodProxy;
import net.flintmc.transform.shadow.Shadow;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.stats.Stat;
import net.minecraft.stats.StatType;
import net.minecraft.stats.StatisticsManager;
import net.minecraft.util.ResourceLocation;

@Shadow(value = "net.minecraft.client.gui.screen.StatsScreen")
public interface StatsScreenShadow {

  @FieldGetter("stats")
  StatisticsManager getStatsManager();

  @FieldGetter("generalStats")
  GeneralListShadow getGeneralStats();

  @FieldGetter("mobStats")
  MobListShadow getMobStats();

  @FieldGetter("itemStats")
  ItemListShadow getItemStats();

  @MethodProxy
  void initLists();

  default void updateData() {
    this.initLists();
  }

  @Shadow(value = "net.minecraft.client.gui.screen.StatsScreen$CustomStatsList")
  interface GeneralListShadow {

    @SuppressWarnings("unchecked")
    default List<GeneralStat> getStats() {
      return (List<GeneralStat>) ((AbstractListShadow) this).getChildren();
    }

    @Shadow(value = "net.minecraft.client.gui.screen.StatsScreen$CustomStatsList$Entry")
    interface GeneralStat {

      @FieldGetter("field_214405_b")
      Stat<ResourceLocation> getStat();
    }
  }

  @Shadow(value = "net.minecraft.client.gui.screen.StatsScreen$MobStatsList")
  interface MobListShadow {

    @SuppressWarnings("unchecked")
    default List<MobStat> getStats() {
      return (List<MobStat>) ((AbstractListShadow) this).getChildren();
    }

    @Shadow(value = "net.minecraft.client.gui.screen.StatsScreen$MobStatsList$Entry")
    interface MobStat {

      @FieldGetter("field_214411_b")
      EntityType<?> getEntityType();
    }
  }

  @Shadow(value = "net.minecraft.client.gui.screen.StatsScreen$StatsList")
  interface ItemListShadow {

    @FieldGetter("field_195113_v")
    List<StatType<Block>> getBlockStatTypes();

    @FieldGetter("field_195114_w")
    List<StatType<Item>> getItemStatTypes();

    @FieldGetter("field_195116_y")
    List<Item> getAvailableItems();
  }

  @Shadow(value = "net.minecraft.client.gui.widget.list.AbstractList")
  interface AbstractListShadow {

    @FieldGetter("children")
    List<?> getChildren();

  }

}
