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

package net.flintmc.mcapi.internal.world.stats;

import java.util.Map;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.items.type.ItemType;
import net.flintmc.mcapi.world.stats.StatsCategory;
import net.flintmc.mcapi.world.stats.value.ItemWorldStat;

@Implement(ItemWorldStat.class)
public class DefaultItemWorldStat implements ItemWorldStat {

  private final StatsCategory category;
  private final ItemType item;
  private final Map<ItemStatType, Integer> values;
  private final Map<ItemStatType, String> formattedValues;

  @AssistedInject
  private DefaultItemWorldStat(
      @Assisted("category") StatsCategory category, @Assisted("item") ItemType item,
      @Assisted("values") Map<ItemStatType, Integer> values,
      @Assisted("formattedValues") Map<ItemStatType, String> formattedValues) {
    this.category = category;
    this.item = item;
    this.values = values;
    this.formattedValues = formattedValues;
  }

  @Override
  public StatsCategory getCategory() {
    return this.category;
  }

  @Override
  public ItemType getItem() {
    return this.item;
  }

  @Override
  public int getValue(ItemStatType type) {
    return this.values.getOrDefault(type, 0);
  }

  @Override
  public String getFormattedValue(ItemStatType type) {
    return this.formattedValues.getOrDefault(type, "-");
  }
}
