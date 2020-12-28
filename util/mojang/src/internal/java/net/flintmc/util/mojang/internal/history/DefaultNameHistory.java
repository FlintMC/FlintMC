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
