package net.flintmc.util.mojang.history;

import net.flintmc.framework.inject.assisted.Assisted;

public interface NameHistoryEntry {

  String getName();

  // -1 for the first name of the player
  long getTimestamp();

  boolean isFirstEntry();

  interface Factory {

    NameHistoryEntry create(@Assisted String name, @Assisted long timestamp);

    NameHistoryEntry create(@Assisted String name);
  }
}
