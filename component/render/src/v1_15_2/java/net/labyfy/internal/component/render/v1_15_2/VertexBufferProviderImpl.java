package net.labyfy.internal.component.render.v1_15_2;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.labyfy.component.commons.math.MathFactory;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.render.RenderType;
import net.labyfy.component.render.VertexBuffer;
import net.labyfy.component.render.VertexBufferProvider;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import org.joml.Matrix4f;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

@Implement(VertexBufferProvider.class)
public class VertexBufferProviderImpl implements VertexBufferProvider {

  private final MathFactory mathFactory;
  private final Collection<VertexBuffer> createdBuffers = new HashSet<>();
  private final MatrixStack matrixStack;
  private final IRenderTypeBuffer iRenderTypeBuffer;
  private Matrix4f matrixTransformation;

  @Inject
  private VertexBufferProviderImpl(MathFactory mathFactory, @Assisted("matrixStack") Object matrixStack, @Assisted("renderTypeBuffer") Object iRenderTypeBuffer) {
    this.mathFactory = mathFactory;
    this.matrixStack = (MatrixStack) matrixStack;
    this.iRenderTypeBuffer = (IRenderTypeBuffer) iRenderTypeBuffer;
    this.matrixTransformation = mathFactory.getMatrix4f();
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
      VertexBufferImpl vertexBuffer = new VertexBufferImpl(mathFactory, matrixTransformation, matrixStack, (BufferBuilder) buffer, renderType);
      this.createdBuffers.add(vertexBuffer);
      return vertexBuffer;
    } catch (Throwable throwable) {
      throw new IllegalStateException(throwable);
    }
  }

  public VertexBufferProvider setMatrixTransform(Matrix4f matrix4f) {
    this.matrixTransformation = matrix4f;
    return this;
  }

  public Collection<VertexBuffer> getCreatedBuffers() {
    return Collections.unmodifiableCollection(this.createdBuffers);
  }

}
