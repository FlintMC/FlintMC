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
import net.flintmc.mcapi.render.text.tooltip.TooltipRenderBuilder;
import net.flintmc.mcapi.render.text.tooltip.TooltipRenderer;
import net.flintmc.mcapi.version.VersionHelper;

@Singleton
@Implement(TooltipRenderBuilder.class)
public class DefaultTooltipRenderBuilder implements TooltipRenderBuilder {

  private final TooltipRenderer renderer;
  private final VersionHelper versionHelper;

  private float x;
  private float y;
  private String text;
  private Object matrixStack;

  @Inject
  private DefaultTooltipRenderBuilder(TooltipRenderer renderer, VersionHelper versionHelper) {
    this.renderer = renderer;
    this.versionHelper = versionHelper;
  }

  private void validate() {
    Preconditions.checkArgument(
        this.x >= 0 && this.y >= 0, "X/Y positions not set/set to something < 0");
    Preconditions.checkNotNull(this.text, "Text not set/set to null");
    Preconditions.checkArgument(!this.text.isEmpty(), "Text cannot be empty");
  }

  private void reset() {
    this.x = -1;
    this.y = -1;
    this.text = null;
  }

  /** {@inheritDoc} */
  @Override
  public TooltipRenderBuilder at(float x, float y) {
    this.x = x;
    this.y = y;
    return this;
  }

  /** {@inheritDoc} */
  @Override
  public TooltipRenderBuilder text(String text) {
    this.text = text;
    return this;
  }

  @Override
  public TooltipRenderBuilder matrixStack(Object matrixStack) {
    this.matrixStack = matrixStack;
    return this;
  }

  /** {@inheritDoc} */
  @Override
  public void draw() {
    this.validate();

    boolean isNetherUpdate = this.versionHelper.isUnder(16);

    if(isNetherUpdate) {
      Preconditions.checkNotNull(this.matrixStack, "Matrix stack cannot be null!");

      this.renderer.renderTooltip(this.matrixStack, this.x, this.y, this.text);
    } else {
      this.renderer.renderTooltip(this.x, this.y, this.text);
    }

    this.reset();
  }
}
