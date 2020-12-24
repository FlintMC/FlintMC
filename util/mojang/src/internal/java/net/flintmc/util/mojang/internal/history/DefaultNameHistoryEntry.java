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
