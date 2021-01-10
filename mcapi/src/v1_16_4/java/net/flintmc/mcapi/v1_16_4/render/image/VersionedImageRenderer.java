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

package net.flintmc.mcapi.v1_16_4.render.image;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mojang.blaze3d.systems.RenderSystem;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.render.image.ImageRenderer;
import net.flintmc.mcapi.resources.ResourceLocation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.vector.Matrix4f;

@Singleton
@Implement(value = ImageRenderer.class, version = "1.16.4")
public class VersionedImageRenderer implements ImageRenderer {

  private final Minecraft minecraft;

  @Inject
  private VersionedImageRenderer() {
    this.minecraft = Minecraft.getInstance();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void bindTexture(ResourceLocation location) {
    this.minecraft.getTextureManager().bindTexture(location.getHandle());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void drawFullImage(
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
      int a) {
    this.drawPartImage(
        screenX,
        screenY,
        0F,
        0F,
        zLevel,
        matrix,
        sourceWidth,
        sourceHeight,
        sourceWidth,
        sourceHeight,
        displayWidth,
        displayHeight,
        r,
        g,
        b,
        a);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void drawPartImage(
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
      int a) {
    // no scaling if not set
    if (displayHeight == -1) {
      displayHeight = sourceHeight;
    }
    if (displayWidth == -1) {
      displayWidth = sourceWidth;
    }

    boolean colored = r != -1 && g != -1 && b != -1 && a != -1;
    if (colored) {
      RenderSystem.color4f(r / 255F, g / 255F, b / 255F, a / 255F);
    }

    boolean scale = sourceHeight != displayHeight || sourceWidth != displayWidth;

    if (scale) {
      RenderSystem.scalef(displayWidth / sourceWidth, displayHeight / sourceHeight, 1F);
    }

    float xScaleFactor = scale ? sourceWidth / displayWidth : 1F;
    float yScaleFactor = scale ? sourceHeight / displayHeight : 1F;

    if (matrix == null) {
      this.blit(
          null,
          screenX * xScaleFactor,
          screenY * yScaleFactor,
          zLevel,
          sourceX,
          sourceY,
          sourceWidth,
          sourceHeight,
          fullImageWidth,
          fullImageHeight);
    } else {
      this.blit(
          (Matrix4f) matrix,
          screenX * xScaleFactor,
          screenY * yScaleFactor,
          zLevel,
          sourceX,
          sourceY,
          sourceWidth,
          sourceHeight,
          fullImageWidth,
          fullImageHeight);
    }

    if (scale) {
      RenderSystem.scalef(xScaleFactor, yScaleFactor, 1F);
    }

    if (colored) {
      RenderSystem.color4f(1F, 1F, 1F, 1F);
    }
  }

  /**
   * Blit method from Minecrafts {@link net.minecraft.client.gui.AbstractGui}, with support for
   * specifying a {@link Matrix4f} and floats as the coordinates instead of ints.
   */
  private void blit(
      Matrix4f matrix,
      float x,
      float y,
      int zLevel,
      float sourceX,
      float sourceY,
      float sourceWidth,
      float sourceHeight,
      float originalWidth,
      float originalHeight) {
    innerBlit(
        matrix,
        x,
        x + sourceWidth,
        y,
        y + sourceHeight,
        zLevel,
        sourceWidth,
        sourceHeight,
        sourceX,
        sourceY,
        originalHeight,
        originalWidth);
  }

  /**
   * Blit method from Minecrafts {@link net.minecraft.client.gui.AbstractGui}, with support for
   * specifying a {@link Matrix4f} and floats as the coordinates instead of ints.
   */
  private void innerBlit(
      Matrix4f matrix,
      float left,
      float right,
      float top,
      float bottom,
      int zLevel,
      float sourceWidth,
      float sourceHeight,
      float sourceX,
      float sourceY,
      float originalHeight,
      float originalWidth) {
    innerBlit(
        matrix,
        left,
        right,
        top,
        bottom,
        zLevel,
        (sourceX + 0.0F) / originalHeight,
        (sourceX + sourceWidth) / originalHeight,
        (sourceY + 0.0F) / originalWidth,
        (sourceY + sourceHeight) / originalWidth);
  }

  /**
   * Blit method from Minecrafts {@link net.minecraft.client.gui.AbstractGui}, with support for
   * specifying a {@link Matrix4f} and floats as the coordinates instead of ints.
   */
  private void innerBlit(
      Matrix4f matrix,
      float left,
      float right,
      float top,
      float bottom,
      int zLevel,
      float u0,
      float u1,
      float v0,
      float v1) {
    BufferBuilder builder = Tessellator.getInstance().getBuffer();
    builder.begin(7, DefaultVertexFormats.POSITION_TEX);
    if (matrix != null) {
      builder.pos(matrix, left, bottom, (float) zLevel).tex(u0, v1).endVertex();
      builder.pos(matrix, right, bottom, (float) zLevel).tex(u1, v1).endVertex();
      builder.pos(matrix, right, top, (float) zLevel).tex(u1, v0).endVertex();
      builder.pos(matrix, left, top, (float) zLevel).tex(u0, v0).endVertex();
    } else {
      builder.pos(left, bottom, (float) zLevel).tex(u0, v1).endVertex();
      builder.pos(right, bottom, (float) zLevel).tex(u1, v1).endVertex();
      builder.pos(right, top, (float) zLevel).tex(u1, v0).endVertex();
      builder.pos(left, top, (float) zLevel).tex(u0, v0).endVertex();
    }
    builder.finishDrawing();
    RenderSystem.enableAlphaTest();
    WorldVertexBufferUploader.draw(builder);
  }
}
