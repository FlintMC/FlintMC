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

package net.flintmc.util.commons.consumer;

/**
 * Utility class for a consumer accept 3 values.
 *
 * @param <A> The type of the first argument
 * @param <B> The type of the second argument
 * @param <C> The type of the third argument
 */
@FunctionalInterface
public interface TriConsumer<A, B, C> {

  /**
   * Performs the operation given the specified arguments.
   *
   * @param a the first input argument
   * @param b the second input argument
   * @param c the third input argument
   */
  void accept(A a, B b, C c);
}
