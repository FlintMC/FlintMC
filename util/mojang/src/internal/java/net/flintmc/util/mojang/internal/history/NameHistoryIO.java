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

package net.flintmc.util.mojang.internal.history;

import com.google.inject.Inject;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;
import net.flintmc.util.mojang.history.NameHistory;
import net.flintmc.util.mojang.history.NameHistoryEntry;
import net.flintmc.util.mojang.internal.cache.DataStreamHelper;
import net.flintmc.util.mojang.internal.cache.object.CacheIO;
import net.flintmc.util.mojang.internal.cache.object.CachedObjectIO;

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
