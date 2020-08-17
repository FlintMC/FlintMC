package net.labyfy.internal.component.render.v1_15_2;

import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.labyfy.component.render.RenderType;
import net.labyfy.component.render.VertexBuffer;
import net.labyfy.component.render.VertexBufferProvider;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;

public class VertexBufferProviderImpl implements VertexBufferProvider {

  private final IRenderTypeBuffer iRenderTypeBuffer;

  public VertexBufferProviderImpl(IRenderTypeBuffer iRenderTypeBuffer) {
    this.iRenderTypeBuffer = iRenderTypeBuffer;
  }

  /**
   * {@inheritDoc}
   */
  public VertexBuffer get(RenderType renderType) {
    net.minecraft.client.renderer.RenderType minecraftRenderTypeHandle = renderType.getHandle();
    IVertexBuilder buffer = this.iRenderTypeBuffer.getBuffer(minecraftRenderTypeHandle);
    if (!(buffer instanceof BufferBuilder)) {
      throw new IllegalStateException();
    }

    try {
      return new VertexBufferImpl((BufferBuilder) buffer, renderType.getFormat());
    } catch (Throwable throwable) {
      throw new IllegalStateException(throwable);
    }
  }

}
