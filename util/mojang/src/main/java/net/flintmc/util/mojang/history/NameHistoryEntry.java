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
   *     the history
   */
  long getTimestamp();

  /**
   * Retrieves whether this entry is the first one in the history.
   *
   * @return {@code true} if it is the first one, {@code false} otherwise
   */
  boolean isFirstEntry();

  /** Factory for the {@link NameHistoryEntry}. */
  @AssistedFactory(NameHistoryEntry.class)
  interface Factory {

    /**
     * Creates a new entry for a {@link NameHistory} at the given timestamp.
     *
     * @param name The non-null name at this timestamp
     * @param timestamp The timestamp when the player has changed their name to this one, -1 if the
     *     new entry is the first one in the history
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
