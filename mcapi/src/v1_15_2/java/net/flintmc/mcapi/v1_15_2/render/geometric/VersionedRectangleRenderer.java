package net.flintmc.mcapi.v1_15_2.render.geometric;

import com.google.inject.Singleton;
import com.mojang.blaze3d.systems.RenderSystem;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.render.geometric.RectangleRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Matrix4f;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.TransformationMatrix;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

@Singleton
@Implement(value = RectangleRenderer.class, version = "1.15.2")
public class VersionedRectangleRenderer implements RectangleRenderer {

  /** {@inheritDoc} */
  @Override
  public void drawRectBorder(
      float x, float y, float width, float height, int rgba, float borderWidth) {
    float left = x;
    float top = y;
    float right = left + width;
    float bottom = y + height;

    this.fill(left, top, left + borderWidth, bottom, rgba); // left
    this.fill(right - borderWidth, top, right, bottom, rgba); // right
    this.fill(left, top, right, top + borderWidth, rgba); // top
    this.fill(left, bottom - borderWidth, right, bottom, rgba); // bottom
  }

  /** {@inheritDoc} */
  @Override
  public void drawFilledRect(float x, float y, float width, float height, int rgba) {
    this.fill(x, y, x + width, y + height, rgba);
  }

  private void fill(float left, float top, float right, float bottom, int rgba) {
    this.fill(TransformationMatrix.identity().getMatrix(), left, top, right, bottom, rgba);
  }

  private void fill(Matrix4f matrix, float left, float top, float right, float bottom, int rgba) {
    if (left < right) {
      // swap left and right
      float tmp = left;
      left = right;
      right = tmp;
    }

    if (top < bottom) {
      // swap bottom and top
      float tmp = top;
      top = bottom;
      bottom = tmp;
    }

    float alpha = (float) (rgba >> 24 & 255) / 255.0F;
    float red = (float) (rgba >> 16 & 255) / 255.0F;
    float green = (float) (rgba >> 8 & 255) / 255.0F;
    float blue = (float) (rgba & 255) / 255.0F;
    BufferBuilder buffer = Tessellator.getInstance().getBuffer();
    RenderSystem.enableBlend();
    RenderSystem.disableTexture();
    RenderSystem.defaultBlendFunc();
    buffer.begin(7, DefaultVertexFormats.POSITION_COLOR);
    buffer.pos(matrix, left, bottom, 0.0F).color(red, green, blue, alpha).endVertex();
    buffer.pos(matrix, right, bottom, 0.0F).color(red, green, blue, alpha).endVertex();
    buffer.pos(matrix, right, top, 0.0F).color(red, green, blue, alpha).endVertex();
    buffer.pos(matrix, left, top, 0.0F).color(red, green, blue, alpha).endVertex();
    buffer.finishDrawing();
    WorldVertexBufferUploader.draw(buffer);
    RenderSystem.enableTexture();
    RenderSystem.disableBlend();
  }
}
