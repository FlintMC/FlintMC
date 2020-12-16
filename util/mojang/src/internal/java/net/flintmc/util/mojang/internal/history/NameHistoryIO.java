package net.flintmc.util.mojang.internal.history;

import com.google.inject.Inject;
import net.flintmc.util.mojang.history.NameHistory;
import net.flintmc.util.mojang.history.NameHistoryEntry;
import net.flintmc.util.mojang.internal.cache.DataStreamHelper;
import net.flintmc.util.mojang.internal.cache.object.CacheIO;
import net.flintmc.util.mojang.internal.cache.object.CachedObjectIO;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;

@CacheIO
public class NameHistoryIO implements CachedObjectIO<NameHistory> {

  private final NameHistory.Factory factory;
  private final NameHistoryEntry.Factory entryFactory;

  @Inject
  private NameHistoryIO(NameHistory.Factory factory, NameHistoryEntry.Factory entryFactory) {
    this.factory = factory;
    this.entryFactory = entryFactory;
  }

  @Override
  public Class<NameHistory> getType() {
    return NameHistory.class;
  }

  @Override
  public void write(UUID uniqueId, NameHistory history, DataOutput output) throws IOException {
    output.writeShort(history.getEntries().size());
    for (NameHistoryEntry entry : history.getEntries()) {
      DataStreamHelper.writeString(output, entry.getName());
      output.writeLong(entry.getTimestamp());
    }
  }

  @Override
  public NameHistory read(UUID uniqueId, DataInput input) throws IOException {
    int size = input.readShort();
    Collection<NameHistoryEntry> entries = new HashSet<>(size);
    for (int i = 0; i < size; i++) {
      entries.add(this.entryFactory.create(DataStreamHelper.readString(input), input.readLong()));
    }

    return this.factory.create(uniqueId, entries);
  }
}
