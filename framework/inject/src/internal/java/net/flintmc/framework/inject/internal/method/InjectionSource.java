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

package net.flintmc.framework.inject.internal.method;

import com.google.inject.Key;

public class InjectionSource {

  private final int sourceIndex;
  private final Key<?> key;
  private final Class<?> type;

  public InjectionSource(int sourceIndex, Key<?> key, Class<?> type) {
    this.sourceIndex = sourceIndex;
    this.key = key;
    this.type = type;
  }

  public boolean isFromInjector() {
    return this.sourceIndex == -1;
  }

  public int getSourceIndex() {
    return this.sourceIndex;
  }

  public Key<?> getKey() {
    return this.key;
  }

  public Class<?> getType() {
    return this.type;
  }
}
