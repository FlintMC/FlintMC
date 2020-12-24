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
import net.flintmc.mcapi.render.image.ImagePartRenderBuilder;
import net.flintmc.mcapi.render.image.ImageRenderer;

@Singleton
@Implement(ImagePartRenderBuilder.class)
public class DefaultImagePartRenderBuilder extends DefaultImageFullRenderBuilder
    implements ImagePartRenderBuilder {

  protected float sourceX;
  protected float sourceY;
  protected float sourceWidth;
  protected float sourceHeight;

  @Inject
  protected DefaultImagePartRenderBuilder(ImageRenderer renderer) {
    super(renderer);
    this.reset();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void reset() {
    super.reset();

    this.sourceX = -1;
    this.sourceY = -1;
    this.sourceWidth = -1;
    this.sourceHeight = -1;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void validate() {
    super.validate();

    Preconditions.checkArgument(
        this.sourceWidth > 0 && this.sourceHeight > 0, "Source size not set/set to something <= 0");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ImagePartRenderBuilder sourcePosition(float x, float y) {
    this.sourceX = x;
    this.sourceY = y;
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ImagePartRenderBuilder sourceSize(float width, float height) {
    this.sourceWidth = width;
    this.sourceHeight = height;
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void draw() {
    this.validate();

    this.renderer.drawPartImage(
        this.x,
        this.y,
        this.sourceX,
        this.sourceY,
        this.zLevel,
        this.matrix,
        this.sourceWidth,
        this.sourceHeight,
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
