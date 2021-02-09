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

package net.flintmc.mcapi.world.stats;

import com.google.common.collect.Multimap;
import net.flintmc.framework.generation.annotation.DataFactory;
import net.flintmc.framework.generation.annotation.TargetDataField;
import net.flintmc.mcapi.world.stats.value.WorldStat;

/**
 * Represents statistics from a specific timestamp per player in a world.
 */
public interface WorldStats {

  /**
   * Retrieves the timestamp when this stats object has been created in milliseconds.
   *
   * @return The timestamp in milliseconds
   * @see System#currentTimeMillis()
   */
  @TargetDataField("timestamp")
  long getTimestamp();

  /**
   * Retrieves the stats from this stats object mapped from its corresponding {@link
   * WorldStatType}.
   *
   * @return The non-null map containing every stat per world stat type
   */
  @TargetDataField("stats")
  Multimap<WorldStatType, WorldStat> getStats();

  /**
   * Factory for the {@link WorldStats}.
   */
  @DataFactory(WorldStats.class)
  interface Factory {

    /**
     * Creates a new {@link WorldStats} object with the given timestamp and stats.
     *
     * @param timestamp The current timestamp which represents the timestamp of the creation of the
     *                  new object
     * @param stats     The non-null map containing every stat mapped from its corresponding {@link
     *                  WorldStatType}
     * @return The new non-null {@link WorldStats} object
     */
    WorldStats create(
        @TargetDataField("timestamp") long timestamp,
        @TargetDataField("stats") Multimap<WorldStatType, WorldStat> stats);

  }

}
