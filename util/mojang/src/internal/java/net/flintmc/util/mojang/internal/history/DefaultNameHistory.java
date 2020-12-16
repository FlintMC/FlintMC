package net.flintmc.util.mojang.internal.history;

import com.google.common.collect.ImmutableList;
import java.util.Collection;
import java.util.UUID;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.util.mojang.history.NameHistory;
import net.flintmc.util.mojang.history.NameHistoryEntry;

@Implement(NameHistory.class)
public class DefaultNameHistory implements NameHistory {

  private final UUID uniqueId;
  private final Collection<NameHistoryEntry> entries;

  @AssistedInject
  private DefaultNameHistory(
      @Assisted UUID uniqueId, @Assisted Collection<NameHistoryEntry> entries) {
    this.uniqueId = uniqueId;
    this.entries = ImmutableList.copyOf(entries);
  }

  @Override
  public UUID getUniqueId() {
    return this.uniqueId;
  }

  @Override
  public Collection<NameHistoryEntry> getEntries() {
    return this.entries;
  }

  @Override
  public String toString() {
    return "DefaultNameHistory{" + "uniqueId=" + this.uniqueId + ", entries=" + this.entries + '}';
  }
}
