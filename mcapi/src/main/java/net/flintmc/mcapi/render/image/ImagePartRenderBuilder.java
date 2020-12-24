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
 * Singleton builder for rendering parts of an image in a resource. This builder is not thread-safe
 * and may only be used on the render thread of Minecraft.
 *
 * <p>The necessary values to be set are the following:
 *
 * <ul>
 *   <li>{@link #at(float, float)}
 *   <li>{@link #fullImageSize(float, float)}
 *   <li>{@link #sourcePosition(float, float)}
 *   <li>{@link #sourceSize(float, float)}
 * </ul>
 *
 * @see ImageRenderer#drawPartImage(float, float, float, float, int, Object, float, float, float,
 * float, float, float, int, int, int, int)
 */
public interface ImagePartRenderBuilder extends ImageFullRenderBuilder {

  /**
   * Changes the position of the content in the texture to be drawn on the screen.
   *
   * @param x The x position of one pixel above the top-left corner of the content in the texture to
   *          draw on the screen
   * @param y The y position of one pixel above the top-left corner of the content in the texture to
   *          draw on the screen
   * @return this builder for chaining
   */
  ImagePartRenderBuilder sourcePosition(float x, float y);

  /**
   * Changes the size of the content in the texture to be drawn on the screen.
   *
   * @param width  The width of the content in the texture to be drawn
   * @param height The height of the content in the texture to be drawn
   * @return this builder for chaining
   */
  ImagePartRenderBuilder sourceSize(float width, float height);

  /**
   * Draws the values that have been set in this builder on the screen and resets this builder to be
   * re-used for the next rendering.
   *
   * <p><b>Important</b>: The texture to be drawn needs to be bound first with {@link
   * ImageRenderer#bindTexture(ResourceLocation)}
   *
   * @throws IllegalArgumentException If no fullImageHeight (or something <= 0), no position (or
   *                                  something < 0), no source size (or something <= 0) and/or
   *                                  color components (r, g, b, a) not >= 0 and <= 255
   * @see ImageRenderer#drawPartImage(float, float, float, float, int, Object, float, float, float,
   * float, float, float, int, int, int, int)
   */
  void draw() throws IllegalArgumentException;
}
