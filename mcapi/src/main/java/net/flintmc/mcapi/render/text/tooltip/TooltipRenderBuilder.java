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

package net.flintmc.mcapi.render.text.tooltip;

/**
 * Singleton builder for the {@link TooltipRenderer}. This builder is not thread-safe and may only
 * be used on the render thread of Minecraft.
 *
 * <p>The necessary values to be set are the following:
 *
 * <ul>
 *   <li>{@link #at(float, float)}
 *   <li>{@link #text(String)} (the text needs to have at least a length of 1)
 * </ul>
 *
 * @see TooltipRenderer#renderTooltip(float, float, String)
 */
public interface TooltipRenderBuilder {

  /**
   * Defines the position on the screen where the tooltip should be rendered. Note that the position
   * is not exactly a corner of the tooltip, Minecraft will automatically position the tooltip so
   * that it fits on the screen with this coordinate being just a hint on where it should be
   * rendered.
   *
   * @param x The x position on the screen where the tooltip should be rendered
   * @param y The y position on the screen where the tooltip should be rendered
   * @return this builder for chaining
   */
  TooltipRenderBuilder at(float x, float y);

  /**
   * Defines the text that should be rendered.
   *
   * @param text The non-null text to be rendered with a length of at least 1
   * @return this builder for chaining
   */
  TooltipRenderBuilder text(String text);

  /**
   * Draws the values that have been set in this builder on the screen and resets this builder to be
   * re-used for the next rendering.
   *
   * @throws NullPointerException     If no text or null has been set
   * @throws IllegalArgumentException If no position and/or an empty text has been set
   * @see TooltipRenderer#renderTooltip(float, float, String)
   */
  void draw() throws NullPointerException, IllegalArgumentException;
}
