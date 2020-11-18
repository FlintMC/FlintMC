package net.flintmc.util.mojang.internal.history;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.util.mojang.history.NameHistoryEntry;

@Implement(NameHistoryEntry.class)
public class DefaultNameHistoryEntry implements NameHistoryEntry {

  private final String name;
  private final long timestamp;

  @AssistedInject
  public DefaultNameHistoryEntry(@Assisted String name, @Assisted long timestamp) {
    this.name = name;
    this.timestamp = timestamp;
  }

  @AssistedInject
  public DefaultNameHistoryEntry(@Assisted String name) {
    this(name, -1);
  }

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public long getTimestamp() {
    return this.timestamp;
  }

  @Override
  public boolean isFirstEntry() {
    return this.timestamp == -1;
  }
}
