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

package net.flintmc.framework.config.internal.storage;

import net.flintmc.framework.config.generator.ParsedConfig;
import net.flintmc.framework.config.storage.ConfigStorage;

public class ComparableConfigStorage implements Comparable<ComparableConfigStorage>, ConfigStorage {

  private final ConfigStorage wrapped;
  private final int priority;

  private ComparableConfigStorage(ConfigStorage wrapped, int priority) {
    this.wrapped = wrapped;
    this.priority = priority;
  }

  public static ComparableConfigStorage wrap(ConfigStorage storage, int priority) {
    if (storage instanceof ComparableConfigStorage) {
      return (ComparableConfigStorage) storage;
    }

    return new ComparableConfigStorage(storage, priority);
  }

  @Override
  public int compareTo(ComparableConfigStorage o) {
    return Integer.compare(this.priority, o.priority);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getName() {
    return this.wrapped.getName();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void write(ParsedConfig config) {
    this.wrapped.write(config);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void read(ParsedConfig config) {
    this.wrapped.read(config);
  }
}
