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

package net.flintmc.mcapi.settings.flint.options.numeric;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Defines a range with a minimum, maximum value and a maximum number of decimal places for the
 * number.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Range {

  /**
   * Retrieves the minimum value of this range.
   *
   * @return The minimum value of this range
   */
  double min() default 0;

  /**
   * Retrieves the maximum value of this range.
   *
   * @return The maximum value of this range
   */
  double max();

  /**
   * Retrieves the number of decimal places that is allowed on the number in this range. If it is 0,
   * this means, that the number can only be an integer.
   *
   * @return The number of decimal places
   */
  int decimals() default 0;
}
