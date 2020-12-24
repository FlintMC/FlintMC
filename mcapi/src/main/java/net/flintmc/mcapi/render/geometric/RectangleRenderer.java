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

package net.flintmc.mcapi.render.geometric;

/**
 * Renderer for simple rectangles.
 */
public interface RectangleRenderer {

  /**
   * Renders the border of a rectangle with the given borderWidth, the width will be added to the
   * inside of the rectangle.
   *
   * @param x           The x position of the top-left corner, the border will be drawn to the
   *                    bottom-right from this position
   * @param y           The y position of the top-left corner, the border will be drawn to the
   *                    bottom-right from this position
   * @param width       The width of the rectangle, x + width will be the most right position of the
   *                    border
   * @param height      The width of the rectangle, y + height will be the lowest position of the
   *                    border
   * @param rgba        The RGBA value to draw the border in
   * @param borderWidth The width of the border, should be > 0
   */
  void drawRectBorder(float x, float y, float width, float height, int rgba, float borderWidth);

  /**
   * Renders the full rectangle.
   *
   * @param x      The x position of the top-left corner
   * @param y      The y position of the top-left corner
   * @param width  The width of the rectangle, x + width will be the most right position of the
   *               rectangle
   * @param height The width of the rectangle, y + height will be the lowest position of the
   *               rectangle
   * @param rgba   The RGBA value to draw the rectangle in
   */
  void drawFilledRect(float x, float y, float width, float height, int rgba);
}
