package net.labyfy.internal.component.render.v1_15_2;


import com.mojang.blaze3d.vertex.IVertexConsumer;
import net.labyfy.component.commons.math.MathFactory;
import net.labyfy.component.render.*;
import net.minecraft.client.renderer.BufferBuilder;
import org.joml.*;

import java.awt.*;
import java.nio.ByteBuffer;

public class VertexBufferImpl implements AdvancedVertexBuffer, VertexBuffer {

  private final BufferBuilder buffer;
  private final RenderType renderType;
  private final MatrixStack matrixStack;
  private final MathFactory mathFactory;
  private Matrix4f worldContext;
  private Matrix3f normalContext;
  private ByteBuffer byteBuffer;
  private int vertexCount;
  private int writtenBytes;

  public VertexBufferImpl(MathFactory mathFactory, Matrix4f matrixTransformation, com.mojang.blaze3d.matrix.MatrixStack matrixStack, BufferBuilder buffer, RenderType renderType) throws Throwable {
    this.mathFactory = mathFactory;
    this.matrixStack = new MatrixStackImpl(mathFactory, this, matrixStack);
    this.matrixStack.mul(matrixTransformation);
    this.byteBuffer = ((BufferBuilderAccessor) buffer).getByteBuffer();
    this.buffer = buffer;
    this.renderType = renderType;
    this.worldContext = mathFactory.getMatrix4f();
    this.normalContext = mathFactory.getMatrix3f();
  }

  /**
   * {@inheritDoc}
   */
  public VertexBufferImpl pos(float x, float y, float z) {
    if (!this.getRenderType().getFormat().hasElement(VertexFormatElementType.POSITION))
      return this;
    Vector3f vector3f = mathFactory.getVector3f(x, y, z);
    if (this.worldContext != null) {
      vector3f.mulPosition(this.worldContext);
    }
    return this.pushFloats(VertexFormatElementType.POSITION, vector3f.x, vector3f.y, vector3f.z);
  }

  /**
   * {@inheritDoc}
   */
  public VertexBuffer color(int r, int g, int b, int alpha) {
    if (!this.getRenderType().getFormat().hasElement(VertexFormatElementType.COLOR))
      return this;
    this.pushBytes(VertexFormatElementType.COLOR, ((byte) r), ((byte) g), ((byte) b), ((byte) alpha));
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public VertexBuffer color(Color color) {
    if (color == null)
      return this;
    return this.color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
  }

  /**
   * {@inheritDoc}
   */
  public VertexBuffer pos(Vector3f position) {
    return this.pos(position.x, position.y, position.z);
  }

  /**
   * {@inheritDoc}
   */
  public VertexBufferImpl normal(float x, float y, float z) {
    if (!this.getRenderType().getFormat().hasElement(VertexFormatElementType.NORMAL))
      return this;
    Vector3f vector3f = mathFactory.getVector3f(x, y, z);
    if (this.normalContext != null) {
      vector3f.mul(this.normalContext);
    }
    return this.pushBytes(VertexFormatElementType.NORMAL, IVertexConsumer.normalInt(x), IVertexConsumer.normalInt(y), IVertexConsumer.normalInt(z));
  }

  /**
   * {@inheritDoc}
   */
  public VertexBuffer normal(Vector3f normal) {
    return this.normal(normal.x, normal.y, normal.z);
  }

  /**
   * {@inheritDoc}
   */
  public VertexBufferImpl end() {
    this.vertexCount++;
    if (this.writtenBytes != this.vertexCount * this.getRenderType().getFormat().getSize()) {
      throw new IllegalStateException("Not all or too many vertex elements have been written.");
    }
    ((BufferBuilderAccessor) buffer).setVertexCount(this.vertexCount);
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public VertexBufferImpl lightmap(short u, short v) {
    if (!this.getRenderType().getFormat().hasElement(VertexFormatElementType.LIGHTMAP))
      return this;
    this.pushShorts(VertexFormatElementType.LIGHTMAP, u, v);
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public VertexBuffer lightmap(int masked) {
    return this.lightmap((short) (masked & 0x00ff), (short) (((masked & 0xffff0000) >> 16) & 0xffff));
  }

  /**
   * {@inheritDoc}
   */
  public VertexBufferImpl texture(float u, float v) {
    if (!this.getRenderType().getFormat().hasElement(VertexFormatElementType.TEXTURE))
      return this;
    this.pushFloats(VertexFormatElementType.TEXTURE, u, v);
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public VertexBuffer texture(Vector2f texture) {
    return this.texture(texture.x, texture.y);
  }

  public VertexBuffer overlay(short x, short y) {
    if (!this.getRenderType().getFormat().hasElement(VertexFormatElementType.OVERLAY))
      return this;
    this.pushShorts(VertexFormatElementType.OVERLAY, x, y);
    return this;
  }

  public VertexBuffer overlay(Vector2i vector2i) {
    if (vector2i == null)
      return this.overlay((short) 0, (short) 10);
    return this.overlay((short) vector2i.x, (short) vector2i.y);
  }

  /**
   * {@inheritDoc}
   */
  public VertexBufferImpl pushFloats(VertexFormatElementType vertexFormatElementType, float... floats) {
    this.growBufferEventually(((this.vertexCount + 1) * getRenderType().getFormat().getSize()));
    getRenderType().getFormat().pushFloats(this.byteBuffer, this, vertexFormatElementType, floats);
    this.writtenBytes += floats.length * Float.BYTES;
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public VertexBufferImpl pushBytes(VertexFormatElementType vertexFormatElementType, byte... bytes) {
    this.growBufferEventually(((this.vertexCount + 1) * this.getRenderType().getFormat().getSize()));
    getRenderType().getFormat().pushBytes(this.byteBuffer, this, vertexFormatElementType, bytes);
    this.writtenBytes += bytes.length;
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public AdvancedVertexBuffer pushShorts(VertexFormatElementType vertexFormatElementType, short... shorts) {
    this.growBufferEventually(((this.vertexCount + 1) * this.getRenderType().getFormat().getSize()));
    getRenderType().getFormat().pushShorts(this.byteBuffer, this, vertexFormatElementType, shorts);
    this.writtenBytes += shorts.length * Short.BYTES;
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public AdvancedVertexBuffer incrementVertexCount(int count) {
    this.vertexCount += count;
    ((BufferBuilderAccessor) buffer).setVertexCount(vertexCount);
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public VertexBufferImpl growBufferEventually(int targetSize) {
    if (this.byteBuffer.limit() < targetSize) {
      ByteBuffer oldBuffer = this.byteBuffer;
      this.byteBuffer = ByteBuffer.allocateDirect(targetSize * 2);
      this.byteBuffer.put(oldBuffer);
      try {
        ((BufferBuilderAccessor) this.buffer).setByteBuffer(byteBuffer);
      } catch (Throwable throwable) {
        throwable.printStackTrace();
      }
    }
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public Matrix4f getWorldContext() {
    return mathFactory.getMatrix4f(this.worldContext);
  }

  /**
   * {@inheritDoc}
   */
  public VertexBuffer setWorldContext(Matrix4f matrix) {
    if (matrix == null) matrix = mathFactory.getMatrix4f();
    this.worldContext = mathFactory.getMatrix4f(matrix);
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public VertexBuffer setNormalContext(Matrix3f normalContext) {
    if (normalContext == null) normalContext = mathFactory.getMatrix3f();
    this.normalContext = mathFactory.getMatrix3f(normalContext);
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public Matrix3f getNormalContext() {
    return mathFactory.getMatrix3f(normalContext);
  }

  /**
   * {@inheritDoc}
   */
  public AdvancedVertexBuffer advanced() {
    return this;
  }

  public MatrixStack getMatrixStack() {
    return this.matrixStack;
  }

  /**
   * {@inheritDoc}
   */
  public VertexBuffer simple() {
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public ByteBuffer getByteBuffer() {
    return this.byteBuffer;
  }

  /**
   * {@inheritDoc}
   */
  public AdvancedVertexBuffer setByteBuffer(ByteBuffer byteBuffer) {
    this.byteBuffer = byteBuffer;
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public int getVertexCount() {
    return this.vertexCount;
  }

  public RenderType getRenderType() {
    return this.renderType;
  }

}
