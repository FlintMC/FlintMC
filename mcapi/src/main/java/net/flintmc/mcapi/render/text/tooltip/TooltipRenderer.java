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
 * Renderer for the tooltips that for example will be shown when the user hovers over an item in
 * their Inventory.
 */
public interface TooltipRenderer {

  /**
   * Renders the given text with the background as a tooltip. {@link TooltipRenderBuilder} should be
   * used instead of directly accessing this method for easier usage. The rendered tooltip cannot be
   * wider than 200 or half of the screen width, depending on what's the larger one.
   *
   * <p>Note that the position is not exactly a corner of the tooltip, Minecraft will automatically
   * position the tooltip so that it fits on the screen with this coordinate being just a hint on
   * where it should be rendered.
   *
   * @param x    The x position on the screen where the tooltip should be rendered
   * @param y    The y position on the screen where the tooltip should be rendered
   * @param text The non-null text to be rendered
   */
  void renderTooltip(float x, float y, String text);

  /**
   * Renders the given text with the background as a tooltip. {@link TooltipRenderBuilder} should be
   * used instead of directly accessing this method for easier usage. The rendered tooltip cannot be
   * wider than 200 or half of the screen width, depending on what's the larger one.
   *
   * <p>Note that the position is not exactly a corner of the tooltip, Minecraft will automatically
   * position the tooltip so that it fits on the screen with this coordinate being just a hint on
   * where it should be rendered.
   *
   * @param matrixStack The non-null matrix stack to display the tooltip correctly.
   * @param x    The x position on the screen where the tooltip should be rendered
   * @param y    The y position on the screen where the tooltip should be rendered
   * @param text The non-null text to be rendered
   */
  void renderTooltip(Object matrixStack, float x, float y, String text);
}
