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

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.render.gui.input.Key;

/**
 * A generic, operating system window.
 */
public interface Window {

  /**
   * Retrieves the platform native handle.
   *
   * @return The platform native handle
   */
  long getHandle();

  /**
   * Retrieves the width of the window.
   *
   * @return The current width of the window
   */
  float getWidth();

  /**
   * Retrieves the height of the window.
   *
   * @return The current height of the window
   */
  float getHeight();

  /**
   * Retrieves the size of the window. This method should be preferred over 2 separate calls to
   * {@link #getWidth()} and {@link #getHeight()}, as implementations might be able to optimize this
   * call into a single call into native code.
   *
   * @return The size of the window packaged as an array with the format {@code [width, height]}
   */
  float[] getSize();

  /**
   * Closes the window and disposed its resources.
   *
   * <p>Note that this might or might not take effect immediately, but the window should be
   * considered invalid after this method has been invoked!
   *
   * @throws IllegalStateException If the window is already closed
   */
  void close();

  /**
   * Adds a renderer to this window and enables it. The renderer will be added to the end of the
   * rendering chain.
   *
   * @param renderer The renderer to add to this window
   * @throws IllegalStateException If the window is already closed
   */
  void addRenderer(WindowRenderer renderer);

  /**
   * Disables and removes a renderer from this window.
   *
   * @param renderer The renderer to remove
   * @return {@code true} if the renderer had been added and was removed now, {@code false}
   * otherwise
   * @throws IllegalStateException If the window is already closed
   */
  boolean removeRenderer(WindowRenderer renderer);

  /**
   * Tests whether the window is currently focused.
   *
   * @return {@code true} if the window is focused currently, {@code false} otherwise
   * @throws IllegalStateException If the window is already closed
   */
  boolean isFocused();

  /**
   * Retrieves whether the given key is currently pressed by the user.
   *
   * @param key The non-null key to check for
   * @return {@code true} if the key is pressed, {@code false otherwise}
   * @throws IllegalStateException If the window is already closed
   * @see #isClosed()
   */
  boolean isKeyPressed(Key key);

  /**
   * Retrieves whether this window has been closed. If it has been closed, no more operations should
   * be called to this window, otherwise they will end in errors.
   *
   * @return {@code true} if it has been closed and should no more be used, {@code false} otherwise
   */
  boolean isClosed();

  /**
   * Factory for {@link Window}s.
   */
  @AssistedFactory(Window.class)
  interface Factory {

    /**
     * Creates a new window and displays it. The OpenGL context of the window will a child context
     * of the minecraft context.
     *
     * @param title  The title of new the window
     * @param width  The width of new the window
     * @param height The height of the new window
     * @return The created window
     */
    Window create(
        @Assisted("title") String title,
        @Assisted("width") int width,
        @Assisted("height") int height);
  }
}
