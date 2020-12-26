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

/**
 * Interface representing the minecraft main window.
 */
public interface MinecraftWindow extends Window {

  /**
   * Retrieves the scale factor currently applied to the minecraft window. This will usually be 1 to
   * 4, but could be changed by mods.
   *
   * @return The scale factor of the minecraft window
   */
  int getScaleFactor();

  /**
   * Retrieves the scaled width of the minecraft window.
   *
   * @return The current scaled with of the window
   */
  float getScaledWidth();

  /**
   * Retrieves the scaled height of the minecraft window.
   *
   * @return The current scaled height of the window
   */
  float getScaledHeight();

  /**
   * Retrieves the width of the framebuffer. This will usually be the same as {@link #getWidth()},
   * but may vary for for example upscaled screenshots. If you need the width for rendering, this
   * method should be used instead of {@link #getWidth()}
   *
   * @return The current framebuffer width
   */
  int getFramebufferWidth();

  /**
   * Retrieves the height of the framebuffer. This will usually be the same as {@link #getHeight()},
   * but may vary for for example upscaled screenshots. If you need the height for rendering, this
   * method should be used instead of {@link #getHeight()}
   *
   * @return The current framebuffer height
   */
  int getFramebufferHeight();

  /**
   * Retrieves the FPS counter of the minecraft window.
   *
   * @return The current FPS of the minecraft window
   */
  int getFPS();

  /**
   * Retrieves whether the game is currently ingame or not. Ingame means that a world is loaded, for
   * example being in a SinglePlayer world or on a MultiPlayer server.
   *
   * @return {@code true} if the client is ingame, {@code false} otherwise
   */
  boolean isIngame();
}
