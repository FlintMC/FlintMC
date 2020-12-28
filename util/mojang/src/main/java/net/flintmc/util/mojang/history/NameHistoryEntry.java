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

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;

/**
 * Represents an entry in a {@link NameHistory} of a player that is (except for the first one in the
 * history) associated with a timestamp of the change.
 */
public interface NameHistoryEntry {

  /**
   * Retrieves the name of the player in the history of names of this entry.
   *
   * @return The non-null player name of this entry
   */
  String getName();

  // -1 for the first name of the player

  /**
   * Retrieves the timestamp when the player has changed their name to the one in this entry. If
   * this entry is the first one in the history, this will be -1.
   *
   * @return The timestamp in milliseconds since 01/01/1970 or -1 if this entry is the first one in
   * the history
   */
  long getTimestamp();

  /**
   * Retrieves whether this entry is the first one in the history.
   *
   * @return {@code true} if it is the first one, {@code false} otherwise
   */
  boolean isFirstEntry();

  /**
   * Factory for the {@link NameHistoryEntry}.
   */
  @AssistedFactory(NameHistoryEntry.class)
  interface Factory {

    /**
     * Creates a new entry for a {@link NameHistory} at the given timestamp.
     *
     * @param name      The non-null name at this timestamp
     * @param timestamp The timestamp when the player has changed their name to this one, -1 if the
     *                  new entry is the first one in the history
     * @return The new non-null {@link NameHistoryEntry}.
     */
    NameHistoryEntry create(@Assisted String name, @Assisted long timestamp);

    /**
     * Creates a new entry for a {@link NameHistory} that should be the first one in the history.
     *
     * @param name The non-null name at this timestamp
     * @return The new non-null {@link NameHistoryEntry}.
     */
    NameHistoryEntry create(@Assisted String name);
  }
}
