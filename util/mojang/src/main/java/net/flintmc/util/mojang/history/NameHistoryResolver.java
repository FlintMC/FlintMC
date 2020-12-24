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

package net.flintmc.util.mojang.history;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Resolver for the name history of players on Mojang.
 *
 * @see NameHistory
 */
public interface NameHistoryResolver {

  /**
   * Resolves the past names of a specified player.
   *
   * @param uniqueId The non-null UUID of the player to retrieve the name history for
   * @return The non-null future which will be completed with the history or {@code null} if there
   * is no player with the given uuid or an internal error occurred
   */
  CompletableFuture<NameHistory> resolveHistory(UUID uniqueId);
}
