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

package net.flintmc.mcapi.v1_15_2.render.geometric;

import com.google.inject.Singleton;
import com.mojang.blaze3d.systems.RenderSystem;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.render.geometric.CircleRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.*;

@Singleton
@Implement(CircleRenderer.class)
public class VersionedCircleRenderer implements CircleRenderer {

  private static final float CIRCLE = (float) (Math.PI * 2F);
  private static final float PART = CIRCLE / 720F;

  /**
   * {@inheritDoc}
   */
  @Override
  public void drawCircleBorder(float x, float y, float radius, int rgba, float borderWidth) {
    if (borderWidth != 1F) {
      GL11.glLineWidth(borderWidth);
    }

    Tessellator tessellator = Tessellator.getInstance();

    BufferBuilder buffer = tessellator.getBuffer();
    RenderSystem.enableBlend();
    RenderSystem.disableTexture();

    int alpha = rgba >> 24 & 255;
    int red = rgba >> 16 & 255;
    int green = rgba >> 8 & 255;
    int blue = rgba & 255;

    glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
    buffer.begin(GL_LINE_LOOP, DefaultVertexFormats.POSITION_COLOR);
    for (float rad = 0; rad < CIRCLE; rad += PART) {
      buffer
          .pos(x + Math.cos(rad) * radius, y + Math.sin(rad) * radius, 0D)
          .color(red, green, blue, alpha)
          .endVertex();
    }
    buffer.finishDrawing();

    WorldVertexBufferUploader.draw(buffer);

    RenderSystem.enableTexture();
    RenderSystem.disableBlend();
  }
}
