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

import net.flintmc.framework.generation.annotation.TargetDataField;
import net.flintmc.mcapi.world.stats.StatsCategory;
import net.flintmc.mcapi.world.stats.WorldStats;

/**
 * Represents a single statistic value from the {@link WorldStats}.
 */
public interface WorldStat {

  /**
   * Retrieves the category to which this statistic value belongs to.
   *
   * @return The non-null category of this statistic value
   */
  @TargetDataField("category")
  StatsCategory getCategory();

}
