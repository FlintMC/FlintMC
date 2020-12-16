package net.flintmc.util.mojang.internal.history;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.util.mojang.history.NameHistoryEntry;

@Implement(NameHistoryEntry.class)
public class DefaultNameHistoryEntry implements NameHistoryEntry {

  private static final DateFormat FORMAT = new SimpleDateFormat();

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

  @Override
  public String toString() {
    return String.format(
        "NameHistoryEntry{%s; %s}",
        this.name, this.isFirstEntry() ? "first entry" : FORMAT.format(this.timestamp));
  }
}
