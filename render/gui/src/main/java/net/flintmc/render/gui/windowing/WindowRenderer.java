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

package net.flintmc.render.gui.windowing;

import net.flintmc.render.gui.screen.ScreenName;
import java.util.Collection;

/**
 * Renderer for windows.
 */
public interface WindowRenderer {

  /**
   * Called when the renderer is added to a window. The OpenGL context in this method is the one
   * used for the window.
   */
  void initialize();

  /**
   * Determines if this renderer is taking full control of the content of the window. If this
   * setting is {@code true} on a renderer attached to the main window, the default render logic of
   * minecraft will be disabled.
   *
   * <p>This setting can't change while a renderer is attached! To change it, re-attach the
   * renderer with this method returning another value.
   *
   * @return {@code true} to mark the renderer as intrusive, {@code false} to mark it as cooperative
   */
  boolean isIntrusive();

  Collection<ScreenName> getIntrusiveScreens();

  /**
   * Called when window needs to be rendered. The OpenGL context in this method is the one used for
   * the window.
   */
  void render();

  /**
   * Called when the renderer is removed from a window. The OpenGL context in this method is the one
   * used for the window.
   */
  void cleanup();
}
