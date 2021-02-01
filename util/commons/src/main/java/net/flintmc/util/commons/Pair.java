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

package net.flintmc.util.commons;

import java.util.Objects;

public class Pair<U, V> {

  private U first;
  private V second;

  public Pair(U first, V second) {
    this.first = first;
    this.second = second;
  }

  public static <U, V> Pair<U, V> of(U first, V second) {
    return new Pair<>(first, second);
  }

  public U getFirst() {
    return this.first;
  }

  public void setFirst(U first) {
    this.first = first;
  }

  public V getSecond() {
    return this.second;
  }

  public void setSecond(V second) {
    this.second = second;
  }

  public Pair<V, U> swap() {
    return new Pair<>(second, first);
  }

  public U first() {
    return this.getFirst();
  }

  public V second() {
    return this.getSecond();
  }

  public U a() {
    return this.getFirst();
  }

  public V b() {
    return this.getSecond();
  }

  public U x() {
    return this.getFirst();
  }

  public V y() {
    return this.getSecond();
  }

  public U u() {
    return this.getFirst();
  }

  public V v() {
    return this.getSecond();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Pair<?, ?> pair = (Pair<?, ?>) o;
    return Objects.equals(first, pair.first) && Objects
        .equals(second, pair.second);
  }

  @Override
  public int hashCode() {
    return Objects.hash(first, second);
  }
}
