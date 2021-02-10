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

package net.flintmc.mcapi.world.stats.value;

import java.util.Map;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.items.type.ItemType;
import net.flintmc.mcapi.world.stats.StatsCategory;
import net.flintmc.mcapi.world.stats.WorldStats;

/**
 * Represents a world stat for a specific item/block from the {@link WorldStats}.
 */
public interface ItemWorldStat extends WorldStat {

  /**
   * Retrieves the item that is represented by this statistic.
   *
   * @return The non-null item of this statistic
   */
  ItemType getItem();

  /**
   * Retrieves the value of this statistic for the given type which is for example the number of
   * blocks mined of the type of this statistic if the {@link ItemStatType} is {@link
   * ItemStatType#BLOCK_MINED}.
   *
   * @param type The non-null type to get the value for
   * @return The value out of this statistic or {@code 0} if there is no value for the given type
   */
  int getValue(ItemStatType type);

  /**
   * Retrieves the {@link #getValue(ItemStatType) value} for the given type formatted which for
   * example is "-" if the value is not set and just the value if it is.
   *
   * @param type The non-null type to get the value for
   * @return The formatted value out of this statistic
   */
  String getFormattedValue(ItemStatType type);

  /**
   * Factory for the {@link ItemWorldStat}.
   */
  @AssistedFactory(ItemWorldStat.class)
  interface Factory {

    /**
     * Creates a new {@link ItemWorldStat} with the given values.
     *
     * @param category        The non-null category of the new statistic
     * @param item            The non-null item of the new statistic
     * @param values          The non-null values of the new statistic mapped by the given type
     * @param formattedValues The non-null formatted values of the new statistic mapped by the given
     *                        type
     * @return The new non-null {@link ItemWorldStat}
     */
    ItemWorldStat create(
        @Assisted("category") StatsCategory category,
        @Assisted("item") ItemType item,
        @Assisted("values") Map<ItemStatType, Integer> values,
        @Assisted("formattedValues") Map<ItemStatType, String> formattedValues);
  }

  /**
   * An enumeration of all statistics that are counted per item.
   */
  enum ItemStatType {
    /**
     * The number of blocks mined of an item type.
     */
    BLOCK_MINED,

    /**
     * The number of items broken of an item type.
     */
    ITEM_BROKEN,

    /**
     * The number of items crafted of an item type.
     */
    ITEM_CRAFTED,

    /**
     * The number of times an item type has been used (for example to break a bloc with a pickaxe).
     */
    ITEM_USED,

    /**
     * The number of items that have been picked up of an item type.
     */
    ITEM_PICKED_UP,

    /**
     * The number of items that have been dropped of an item type.
     */
    ITEM_DROPPED;
  }

}
