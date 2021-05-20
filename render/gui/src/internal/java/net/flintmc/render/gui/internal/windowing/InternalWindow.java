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

package net.flintmc.render.gui.internal.windowing;

import net.flintmc.render.gui.input.Key;
import net.flintmc.render.gui.windowing.Window;
import java.util.Set;

/**
 * Interface for accessing internal components of windows from the internal implementation.
 */
public interface InternalWindow extends Window {

  /**
   * Determines whether the window is being rendered intrusively. This only has an effect for the
   * Minecraft window.
   *
   * @return {@code true} if the windows is renderer intrusively, {@code false} otherwise
   */
  boolean isRenderedIntrusively(String screen);

  /**
   * Renders the window by executing the render chain.
   */
  void render();

  /**
   * Mutable version of {@link #getPressedKeys()}.
   *
   * @return The non-null mutable set of currently pressed keys.
   */
  Set<Key> getMutablePressedKeys();
}
