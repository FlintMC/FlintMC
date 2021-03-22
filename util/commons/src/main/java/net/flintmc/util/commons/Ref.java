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

import java.util.function.Supplier;

public class Ref<T> {

  private T obj;

  public Ref(T obj) {
    this.obj = obj;
  }

  public Ref() {

  }

  public T get() {
    return this.obj;
  }

  public boolean isNull() {
    return obj == null;
  }

  public void set(T obj) {
    this.obj = obj;
  }

  public T getOr(T obj) {
    if (isNull()) {
      return obj;
    } else {
      return this.obj;
    }
  }

  public T getOrElse(Supplier<T> supplier) {
    if (this.isNull()) {
      return supplier.get();
    } else {
      return this.obj;
    }
  }

}
