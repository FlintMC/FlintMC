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

import net.flintmc.framework.generation.annotation.DataFactory;
import net.flintmc.framework.generation.annotation.TargetDataField;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.world.stats.StatsCategory;
import net.flintmc.mcapi.world.stats.WorldStats;

/**
 * Represents a simple statistic from the {@link WorldStats} with an integer value.
 */
public interface GeneralWorldStat extends WorldStat {

  /**
   * Retrieves the display name of this statistic.
   *
   * @return The non-null display name of this statistic
   */
  @TargetDataField("displayName")
  ChatComponent getDisplayName();

  /**
   * Retrieves the {@link #getValue() value} of this statistic formatted, for example for distances
   * this will be formatted with an "m" (or "cm"/"km", depending on the value) at the end.
   *
   * @return The non-null formatted value of this statistic
   */
  @TargetDataField("formattedValue")
  String getFormattedValue();

  /**
   * Retrieves the value of this statistic.
   *
   * @return The value of this statistic
   */
  @TargetDataField("value")
  int getValue();

  /**
   * Factory for the {@link GeneralWorldStat}.
   */
  @DataFactory(GeneralWorldStat.class)
  interface Factory {

    /**
     * Creates a new {@link GeneralWorldStat} with the given values.
     *
     * @param category       The non-null category of the new statistic
     * @param displayName    The non-null display name of the new statistic
     * @param formattedValue The non-null formatted value of the new statistic
     * @param value          The value of the new statistic
     * @return The new non-null {@link GeneralWorldStat}
     */
    GeneralWorldStat create(
        @TargetDataField("category") StatsCategory category,
        @TargetDataField("displayName") ChatComponent displayName,
        @TargetDataField("formattedValue") String formattedValue,
        @TargetDataField("value") int value);
  }

}
