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

package net.flintmc.mcapi.render.image;

import net.flintmc.mcapi.resources.ResourceLocation;

/**
 * Renderer for images.
 */
public interface ImageRenderer {

  /**
   * Binds the given texture location to be used for {@link #drawFullImage(float, float, int, float,
   * float, float, float, int, int, int, int)} and {@link #drawPartImage(float, float, float, float,
   * int, float, float, float, float, float, float, int, int, int, int)}.
   *
   * <p>Once bound, the texture can be used multiple times for those two methods and doesn't need
   * to
   * be bound multiple times.
   *
   * @param location The non-null texture to be bound
   */
  void bindTexture(ResourceLocation location);

  /**
   * Draws parts of the {@link #bindTexture(ResourceLocation) bound texture} on the screen at the
   * given position and scales it if necessary. A texture needs to be bound first by using {@link
   * #bindTexture(ResourceLocation)}.
   *
   * @param screenX       The x position of the top-left corner on the screen to draw the image at
   * @param screenY       The y position of the top-left corner on the screen to draw the image at
   *                      the screen
   * @param zLevel        The z level on the screen where the image should be drawn
   * @param sourceWidth   The width of the content in the texture to be drawn
   * @param sourceHeight  The height of the content in the texture to be drawn
   * @param displayWidth  The width of the texture to be displayed on the screen, if this is not
   *                      equal to the {@code sourceWidth}, the image will be scaled
   * @param displayHeight The height of the texture to be displayed on the screen, if this is not
   *                      equal to the {@code sourceHeight}, the image will be scaled
   * @param r             The red part of the RGBA color from 0 - 255, -1 to disable the colors
   * @param g             The green part of the RGBA color from 0 - 255, -1 to disable the colors
   * @param b             The blue part of the RGBA color from 0 - 255, -1 to disable the colors
   * @param a             The alpha part of the RGBA color from 0 - 255, -1 to disable the colors
   */
  void drawFullImage(
      float screenX,
      float screenY,
      int zLevel,
      Object matrix,
      float sourceWidth,
      float sourceHeight,
      float displayWidth,
      float displayHeight,
      int r,
      int g,
      int b,
      int a);

  /**
   * Draws parts of the {@link #bindTexture(ResourceLocation) bound texture} on the screen at the
   * given position and scales it if necessary. A texture needs to be bound first by using {@link
   * #bindTexture(ResourceLocation)}.
   *
   * @param screenX         The x position of the top-left corner on the screen to draw the image
   *                        at
   * @param screenY         The y position of the top-left corner on the screen to draw the image
   *                        at
   * @param sourceX         The x position of one pixel above the top-left corner of the content in
   *                        the texture to draw on the screen
   * @param sourceY         The y position of one pixel above the top-left corner of the content in
   *                        the texture to draw on the screen
   * @param zLevel          The z level on the screen where the image should be drawn
   * @param matrix          The matrix to be used for rendering the image, null to use no matrix
   * @param sourceWidth     The width of the content in the texture to be drawn
   * @param sourceHeight    The height of the content in the texture to be drawn
   * @param fullImageWidth  The width of the full texture
   * @param fullImageHeight The height of the full texture
   * @param displayWidth    The width of the texture to be displayed on the screen, if this is not
   *                        equal to the {@code sourceWidth}, the image will be scaled
   * @param displayHeight   The height of the texture to be displayed on the screen, if this is not
   *                        equal to the {@code sourceHeight}, the image will be scaled
   * @param r               The red part of the RGBA color from 0 - 255, -1 to disable the colors
   * @param g               The green part of the RGBA color from 0 - 255, -1 to disable the colors
   * @param b               The blue part of the RGBA color from 0 - 255, -1 to disable the colors
   * @param a               The alpha part of the RGBA color from 0 - 255, -1 to disable the colors
   */
  void drawPartImage(
      float screenX,
      float screenY,
      float sourceX,
      float sourceY,
      int zLevel,
      Object matrix,
      float sourceWidth,
      float sourceHeight,
      float fullImageWidth,
      float fullImageHeight,
      float displayWidth,
      float displayHeight,
      int r,
      int g,
      int b,
      int a);
}
