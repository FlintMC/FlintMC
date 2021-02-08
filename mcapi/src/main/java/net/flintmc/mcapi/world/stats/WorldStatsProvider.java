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

import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Provider to get the {@link WorldStats} from a server.
 */
public interface WorldStatsProvider {

  /**
   * Retrieves the current stats from the last time the server sent them or empty stats if the
   * server never sent them, though the timestamp will still be the one from the first invocation of
   * this method when the server never sent them.
   * <p>
   * For this to be changed {@link #requestStatsUpdate()} doesn't necessarily need to be called,
   * Minecraft itself can also send the stats on its own.
   *
   * @return The non-null stats from the last update from the server or empty stats (still non-null)
   * if the server never sent something
   */
  WorldStats getCurrentStats();

  /**
   * Requests an update of the stats from the server. When received {@link #getCurrentStats()} will
   * be updated and the retrieved future will be completed. This request won't time out if the
   * server doesn't send anything.
   *
   * @return The non-null future that will be completed when the server sends the response
   */
  CompletableFuture<WorldStats> requestStatsUpdate();

  /**
   * Retrieves all available categories mapped from their corresponding {@link WorldStatType}.
   *
   * @return The non-null immutable map with all categories
   */
  Map<WorldStatType, StatsCategory> getCategories();

}
