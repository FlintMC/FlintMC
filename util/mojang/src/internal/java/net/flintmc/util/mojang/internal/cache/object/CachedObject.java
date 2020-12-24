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

package net.flintmc.util.mojang.internal.cache.object;

public class CachedObject<T> {

  private final Class<T> type;
  private final T value;
  private final CachedObjectIO<T> io;
  private final long validUntil;

  public CachedObject(Class<T> type, T value, CachedObjectIO<T> io, long validUntil) {
    this.type = type;
    this.value = value;
    this.io = io;
    this.validUntil = validUntil;
  }

  public Class<T> getType() {
    return this.type;
  }

  public T getValue() {
    return this.value;
  }

  public CachedObjectIO<T> getIO() {
    return this.io;
  }

  public long getValidUntil() {
    return this.validUntil;
  }

  public boolean isValid() {
    return System.currentTimeMillis() <= this.validUntil;
  }
}
