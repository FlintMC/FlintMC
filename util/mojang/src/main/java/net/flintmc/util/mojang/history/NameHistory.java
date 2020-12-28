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

import java.util.Collection;
import java.util.UUID;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;

/**
 * Represents the history of all names of a specific player on Mojang.
 */
public interface NameHistory {

  /**
   * Retrieves the UUID of the player to whom this history belongs to.
   *
   * @return The non-null UUID of the player of this history
   */
  UUID getUniqueId();

  /**
   * Retrieves a collection containing all entries in the history of names of the player.
   *
   * @return The non-null unmodifiable with all entries (at least 1) in the history of the names of
   * this player
   */
  Collection<NameHistoryEntry> getEntries();

  /**
   * Factory for the {@link NameHistory}.
   */
  @AssistedFactory(NameHistory.class)
  interface Factory {

    /**
     * Creates a new {@link NameHistory} with the given UUID and entries. The entries will be mapped
     * to an unmodifiable collection for the history.
     *
     * @param uniqueId The non-null UUID of the player to whom the given entries belong to
     * @param entries  The non-null and non-empty collection of entries in the name history of the
     *                 given player
     * @return The new non-null {@link NameHistory}
     */
    NameHistory create(@Assisted UUID uniqueId, @Assisted Collection<NameHistoryEntry> entries);
  }
}
