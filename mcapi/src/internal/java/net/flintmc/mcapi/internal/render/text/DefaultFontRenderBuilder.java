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

package net.flintmc.mcapi.internal.render.text;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.render.text.raw.FontRenderBuilder;
import net.flintmc.mcapi.render.text.raw.FontRenderer;
import net.flintmc.mcapi.render.text.raw.StringAlignment;
import net.flintmc.mcapi.version.VersionHelper;

@Singleton
@Implement(FontRenderBuilder.class)
public class DefaultFontRenderBuilder implements FontRenderBuilder {

  private static final int WHITE = 16777215; // r = 255; g = 255; b = 255; a = 255

  private final FontRenderer renderer;
  private final VersionHelper versionHelper;
  private float x;
  private float y;
  private String text;
  private int rgba;
  private StringAlignment alignment;
  private int maxLineLength;
  private boolean shadow;
  private float xFactor;
  private float yFactor;
  private Object matrixStack;

  @Inject
  private DefaultFontRenderBuilder(FontRenderer renderer, VersionHelper versionHelper) {
    this.renderer = renderer;
    this.versionHelper = versionHelper;
    this.reset();
  }

  private void validate() {
    Preconditions.checkNotNull(this.text, "Text not set/set to null");
    Preconditions.checkArgument(!this.text.isEmpty(), "Text cannot be empty");
    Preconditions.checkNotNull(this.alignment, "Alignment cannot be null");
    Preconditions.checkArgument(
        this.maxLineLength >= 0, "MaxLineLength cannot be < 0 (only 0 disables splitting)");
  }

  private void reset() {
    this.x = -1;
    this.y = -1;
    this.text = null;
    this.rgba = WHITE;
    this.alignment = StringAlignment.LEFT;
    this.maxLineLength = 0;
    this.shadow = true;
    this.xFactor = 1;
    this.yFactor = 1;
  }

  /** {@inheritDoc} */
  @Override
  public FontRenderBuilder at(float x, float y) {
    this.x = x;
    this.y = y;
    return this;
  }

  /** {@inheritDoc} */
  @Override
  public FontRenderBuilder text(String text) {
    this.text = text;
    return this;
  }

  /** {@inheritDoc} */
  @Override
  public FontRenderBuilder scale(float factor) {
    return this.scale(factor, factor);
  }

  /** {@inheritDoc} */
  @Override
  public FontRenderBuilder scale(float xFactor, float yFactor) {
    this.xFactor = xFactor;
    this.yFactor = yFactor;
    return this;
  }

  /** {@inheritDoc} */
  @Override
  public FontRenderBuilder color(int rgba) {
    this.rgba = rgba;
    return this;
  }

  /** {@inheritDoc} */
  @Override
  public FontRenderBuilder color(int r, int g, int b) {
    return this.color(r, g, b, 255);
  }

  /** {@inheritDoc} */
  @Override
  public FontRenderBuilder color(int r, int g, int b, int a) {
    return this.color(((a & 0xFF) << 24) | ((r & 0xFF) << 16) | ((g & 0xFF) << 8) | (b & 0xFF));
  }

  /** {@inheritDoc} */
  @Override
  public FontRenderBuilder align(StringAlignment alignment) {
    this.alignment = alignment;
    return this;
  }

  /** {@inheritDoc} */
  @Override
  public FontRenderBuilder useMultipleLines(int maxLineLength) {
    this.maxLineLength = maxLineLength;
    return this;
  }

  /** {@inheritDoc} */
  @Override
  public FontRenderBuilder useMultipleLines() {
    return this.useMultipleLines(Integer.MAX_VALUE);
  }

  /** {@inheritDoc} */
  @Override
  public FontRenderBuilder disableShadow() {
    this.shadow = false;
    return this;
  }

  /** {@inheritDoc} */
  @Override
  public FontRenderBuilder matrixStack(Object matrixStack) {
    this.matrixStack = matrixStack;
    return this;
  }

  /** {@inheritDoc} */
  @Override
  public void draw() {
    this.validate();

    boolean isNetherUpdate = this.versionHelper.isUnder(16);

    if (isNetherUpdate) {

      Preconditions.checkNotNull(this.matrixStack, "Matrix stack cannot be null!");

      this.renderer.drawString(
          this.matrixStack,
          this.x,
          this.y,
          this.text,
          this.rgba,
          this.alignment,
          this.maxLineLength,
          this.shadow,
          this.xFactor,
          this.yFactor);
    } else {

      this.renderer.drawString(
          this.x,
          this.y,
          this.text,
          this.rgba,
          this.alignment,
          this.maxLineLength,
          this.shadow,
          this.xFactor,
          this.yFactor);
    }

    this.reset();
  }
}
