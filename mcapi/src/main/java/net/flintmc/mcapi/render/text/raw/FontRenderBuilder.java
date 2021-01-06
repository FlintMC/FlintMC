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

package net.flintmc.mcapi.render.text.raw;

/**
 * Singleton builder for the {@link FontRenderer}. This builder is not thread-safe and may only be
 * used on the render thread of Minecraft.
 *
 * <p>The necessary values to be set are the following:
 *
 * <ul>
 *   <li>{@link #at(float, float)}
 *   <li>{@link #text(String)} (the text needs to have at least a length of 1)
 * </ul>
 *
 * @see FontRenderer#drawString(float, float, String, int, StringAlignment, int, boolean, float,
 * float)
 */
public interface FontRenderBuilder {

  /**
   * Defines the position where the text should be rendered.
   *
   * @param x The x position on the screen, may be modified by {@link StringAlignment}.
   * @param y The y position on the screen
   * @return this builder for chaining
   */
  FontRenderBuilder at(float x, float y);

  /**
   * Sets the text to be rendered. Linebreaks in this text will be ignored if useMultipleLines
   * hasn't been enabled. To enable line breaks, use {@link #useMultipleLines()}. To also limit the
   * max length per line use {@link #useMultipleLines(int)}.
   *
   * @param text The non-null text to be rendered
   * @return this builder for chaining
   * @see #useMultipleLines()
   * @see #useMultipleLines(int)
   */
  FontRenderBuilder text(String text);

  /**
   * Equivalent to {@link #scale(float, float)} with both values being {@code factor}.
   *
   * @param factor The factor to scale the drawn text
   * @return this builder for chaining
   */
  FontRenderBuilder scale(float factor);

  /**
   * Sets the x and y scaling factors for the resulting text.
   *
   * @param xFactor The factor to scale the text on the x-axis
   * @param yFactor The factor to scale the text on the y-axis
   * @return this builder for chaining
   */
  FontRenderBuilder scale(float xFactor, float yFactor);

  /**
   * Changes the color of this builder to the given RGBA value. This will be overridden if the text
   * contains color codes like "§a", but until it doesn't contain any of those, the color will be
   * the set one.
   *
   * @param rgba The new RGBA as the base color of the rendered text
   * @return this builder for chaining
   */
  FontRenderBuilder color(int rgba);

  /**
   * Changes the color of this builder to the given RGB value. This will be overridden if the text
   * contains color codes like "§a", but until it doesn't contain any of those, the color will be
   * the set one.
   *
   * @param r The red value of the RGB to set from 0 - 255
   * @param g The green value of the RGB to set from 0 - 255
   * @param b The blue value of the RGB to set from 0 - 255
   * @return this builder for chaining
   */
  FontRenderBuilder color(int r, int g, int b);

  /**
   * Changes the color of this builder to the given RGB value. This will be overridden if the text
   * contains color codes like "§a", but until it doesn't contain any of those, the color will be
   * the set one.
   *
   * @param r The red value of the RGB to set from 0 - 255
   * @param g The green value of the RGB to set from 0 - 255
   * @param b The blue value of the RGB to set from 0 - 255
   * @param a The alpha value of the RGB to set from 0 - 255
   * @return this builder for chaining
   */
  FontRenderBuilder color(int r, int g, int b, int a);

  /**
   * Changes the alignment of the text to the given coordinates. The default alignment is {@link
   * StringAlignment#LEFT}.
   *
   * @param alignment The non-null alignment of the text
   * @return this builder for chaining
   */
  FontRenderBuilder align(StringAlignment alignment);

  /**
   * Changes the max length of each line in the given text. If this is set to something else than 0,
   * Minecraft will automatically split the text at the best position (if available Line
   * breaks/Spaces).
   *
   * @param maxLineLength The new max line length, 0 to disable, otherwise > 0
   * @return this builder for chaining
   */
  FontRenderBuilder useMultipleLines(int maxLineLength);

  /**
   * Enables that line breaks won't be ignored in the given text.
   *
   * @return this builder for chaining
   */
  FontRenderBuilder useMultipleLines();

  /**
   * Disables the shadow of the text when rendered. The shadow cannot be used for texts that are
   * rendered in multiple lines ({@link #useMultipleLines(int)}), in those it is always enabled.
   *
   * @return this builder for chaining
   */
  FontRenderBuilder disableShadow();

  /**
   * Changes the matrix stack of this builder. The matrix stack is used to correctly display the
   * font.
   *
   * <p><b>Note:</b> The matrix stack is used for font rendering in 1.16 and later.
   *
   * @param matrixStack The non-null matrix stack.
   * @return This builder for chaining.
   */
  FontRenderBuilder matrixStack(Object matrixStack);

  /**
   * Draws the values that have been set in this builder on the screen and resets this builder to be
   * re-used for the next rendering.
   *
   * @throws NullPointerException     If no text/null or null as the alignment has been set
   * @throws IllegalArgumentException If a maxLineLength < 0 and/or an empty text has been set
   * @see FontRenderer#drawString(float, float, String, int, StringAlignment, int, boolean, float,
   * float)
   */
  void draw() throws NullPointerException, IllegalArgumentException;
}
