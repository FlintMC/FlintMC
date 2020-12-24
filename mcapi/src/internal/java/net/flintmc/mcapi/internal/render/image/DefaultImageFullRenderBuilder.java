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

package net.flintmc.mcapi.internal.render.image;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.render.image.ImageFullRenderBuilder;
import net.flintmc.mcapi.render.image.ImageRenderer;

@Singleton
@Implement(ImageFullRenderBuilder.class)
public class DefaultImageFullRenderBuilder implements ImageFullRenderBuilder {

  protected final ImageRenderer renderer;

  protected float imageWidth;
  protected float imageHeight;
  protected float x;
  protected float y;
  protected Object matrix;
  protected float displayWidth;
  protected float displayHeight;
  protected int r;
  protected int g;
  protected int b;
  protected int a;
  protected int zLevel;

  @Inject
  protected DefaultImageFullRenderBuilder(ImageRenderer renderer) {
    this.renderer = renderer;
    this.reset();
  }

  protected void reset() {
    this.imageWidth = -1;
    this.imageHeight = -1;
    this.x = -1;
    this.y = -1;
    this.displayWidth = -1;
    this.displayHeight = -1;
    this.r = -1;
    this.g = -1;
    this.b = -1;
    this.a = -1;
    this.zLevel = 0;
  }

  protected void validate() {
    Preconditions.checkArgument(
        this.imageWidth > 0 && this.imageHeight > 0, "Image size not set/set to something <= 0");
    Preconditions.checkArgument(
        this.r >= -1 && this.g >= -1 && this.b >= -1 && this.a >= -1,
        "Colors (r, g, b or a) set to something < 0");
    Preconditions.checkArgument(
        this.r <= 255 && this.g <= 255 && this.b <= 255 && this.a <= 255,
        "Colors (r, g, b or a) set to something > 255");
  }

  /** {@inheritDoc} */
  @Override
  public ImageFullRenderBuilder fullImageSize(float width, float height) {
    this.imageWidth = width;
    this.imageHeight = height;
    return this;
  }

  /** {@inheritDoc} */
  @Override
  public ImageFullRenderBuilder at(float x, float y) {
    this.x = x;
    this.y = y;
    return this;
  }

  /** {@inheritDoc} */
  @Override
  public ImageFullRenderBuilder matrix(Object matrix) {
    this.matrix = matrix;
    return this;
  }

  @Override
  public ImageFullRenderBuilder displaySize(float width, float height) {
    this.displayWidth = width;
    this.displayHeight = height;
    return this;
  }

  /** {@inheritDoc} */
  @Override
  public ImageFullRenderBuilder color(int rgba) {
    return this.color((rgba >> 16) & 0xFF, (rgba >> 8) & 0xFF, rgba & 0xFF, (rgba >> 24) & 0xFF);
  }

  /** {@inheritDoc} */
  @Override
  public ImageFullRenderBuilder color(int r, int g, int b) {
    return this.color(r, g, b, 255);
  }

  /** {@inheritDoc} */
  @Override
  public ImageFullRenderBuilder color(int r, int g, int b, int a) {
    this.r = r;
    this.g = g;
    this.b = b;
    this.a = a;
    return this;
  }

  /** {@inheritDoc} */
  @Override
  public ImageFullRenderBuilder zLevel(int zLevel) {
    this.zLevel = zLevel;
    return this;
  }

  /** {@inheritDoc} */
  @Override
  public void draw() {
    this.validate();

    this.renderer.drawFullImage(
        this.x,
        this.y,
        this.zLevel,
        this.matrix,
        this.imageWidth,
        this.imageHeight,
        this.displayWidth,
        this.displayHeight,
        this.r,
        this.g,
        this.b,
        this.a);

    this.reset();
  }
}
