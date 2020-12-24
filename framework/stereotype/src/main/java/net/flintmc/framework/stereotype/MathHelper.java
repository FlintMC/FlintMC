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

package net.flintmc.framework.stereotype;

/** A helper for mathematical operations. */
public final class MathHelper {

  /**
   * Squares a number.
   *
   * @param number The number to be squared.
   * @return The squared number.
   */
  public static double square(double number) {
    return number * number;
  }

  /**
   * Restricts a number to be within a specified range.
   *
   * @param number The number to be restricted.
   * @param minimum The minimum desired value.
   * @param maximum The maximum desired value.
   * @return A value between {@code minimum} and {@code maximum}.
   */
  public static double clamp(double number, double minimum, double maximum) {
    return number < minimum ? minimum : Math.min(number, maximum);
  }
}
