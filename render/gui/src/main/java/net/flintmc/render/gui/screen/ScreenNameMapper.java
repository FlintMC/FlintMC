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

package net.flintmc.render.gui.screen;

/**
 * Service converting class names to screen names.
 */
public interface ScreenNameMapper {

  /**
   * Converts the given fully qualified class name to a screen name.
   *
   * @param className The fully qualified class name to convert
   * @return The converted screen name
   */
  ScreenName fromClass(String className);

  /**
   * Converts the given object into a screen name.
   *
   * @param screen The screen object
   * @return The converted screen name, or {@code null}, if screen is {@code null}
   */
  default ScreenName fromObject(Object screen) {
    if (screen == null) {
      return null;
    }

    return fromClass(screen.getClass().getName());
  }
}
