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

package net.flintmc.util.csv;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class CSV<K, V> {

  protected final Map<K, List<V>> data;

  CSV(Map<K, List<V>> data) {
    this.data = data;
  }

  public List<V> get(final K key) {
    return data.get(key);
  }

  public Map<V, V> relation(final K key, final K value) {
    List<V> keys = data.get(key), values = data.get(value);
    Map<V, V> map = new HashMap<>();

    if (keys.size() != values.size()) {
      throw new IllegalStateException();
    }

    for (int i = 0; i < keys.size(); i++) {
      map.put(keys.get(i), values.get(i));
    }

    return map;
  }

  public Collection<List<V>> values() {
    return data.values();
  }
}
