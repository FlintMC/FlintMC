package net.flintmc.util.mojang.history;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;

import java.util.Collection;
import java.util.UUID;

public interface NameHistory {

  UUID getUniqueId();

  Collection<NameHistoryEntry> getEntries();

  @AssistedFactory(NameHistory.class)
  interface Factory {

    NameHistory create(@Assisted UUID uniqueId, @Assisted Collection<NameHistoryEntry> entries);
  }
}
