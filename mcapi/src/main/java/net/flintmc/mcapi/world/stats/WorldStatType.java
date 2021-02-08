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

import net.flintmc.mcapi.world.stats.value.GeneralWorldStat;
import net.flintmc.mcapi.world.stats.value.ItemWorldStat;
import net.flintmc.mcapi.world.stats.value.MobWorldStat;
import net.flintmc.mcapi.world.stats.value.WorldStat;

/**
 * Types of stats for the statistics of a player in a world.
 */
public enum WorldStatType {

  /**
   * The {@link WorldStat}s will be an instance of {@link GeneralWorldStat} and contain stuff like
   * the number of games quit, distance walked.
   */
  GENERAL,

  /**
   * The {@link WorldStat}s will be an instance of {@link ItemWorldStat} and contain stuff like the
   * number of items broken and picked up.
   */
  ITEM,

  /**
   * The {@link WorldStat}s will be an instance of {@link MobWorldStat} and contain the number of
   * kills for each entity type by the player and the mob.
   */
  MOB;

}
