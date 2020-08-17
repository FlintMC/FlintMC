package net.labyfy.internal.component.render.v1_15_2;


import net.labyfy.component.render.AdvancedVertexBuffer;
import net.labyfy.component.render.VertexBuffer;
import net.labyfy.component.render.VertexFormat;
import net.minecraft.client.renderer.BufferBuilder;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.awt.*;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;

public class VertexBufferImpl implements AdvancedVertexBuffer, VertexBuffer {
  private final static MethodHandle byteBufferFieldGetter;
  private final static MethodHandle byteBufferFieldSetter;
  private final static MethodHandle vertexCountFieldSetter;

  static {
    MethodHandle byteBufferFieldGetterTmp = null;
    MethodHandle byteBufferFieldSetterTmp = null;
    MethodHandle vertexCountFieldSetterTmp = null;
    try {
      Field byteBuffer = BufferBuilder.class.getDeclaredField("byteBuffer");
      byteBuffer.setAccessible(true);
      byteBufferFieldGetterTmp = MethodHandles.lookup().unreflectGetter(byteBuffer);
      byteBufferFieldSetterTmp = MethodHandles.lookup().unreflectSetter(byteBuffer);

      Field vertexCount = BufferBuilder.class.getDeclaredField("vertexCount");
      vertexCount.setAccessible(true);
      vertexCountFieldSetterTmp = MethodHandles.lookup().unreflectSetter(vertexCount);

    } catch (NoSuchFieldException | IllegalAccessException e) {
      e.printStackTrace();
    }
    byteBufferFieldGetter = byteBufferFieldGetterTmp;
    byteBufferFieldSetter = byteBufferFieldSetterTmp;
    vertexCountFieldSetter = vertexCountFieldSetterTmp;
  }

  private final BufferBuilder buffer;
  private final VertexFormat vertexFormat;
  private Matrix4f worldContext;
  private Matrix3f normalContext;
  private ByteBuffer byteBuffer;
  private int vertexCount;
  private int writtenBytes;

  public VertexBufferImpl(BufferBuilder buffer, VertexFormat vertexFormat) throws Throwable {
    this.byteBuffer = (ByteBuffer) byteBufferFieldGetter.invoke(buffer);
    this.buffer = buffer;
    this.vertexFormat = vertexFormat;
    this.worldContext = new Matrix4f();
    this.normalContext = new Matrix3f();
  }

  public VertexBufferImpl pos(float x, float y, float z) {
    if (!this.getFormat().hasElement("position"))
      return this;
    Vector3f vector3f = new Vector3f(x, y, z);
    if (this.worldContext != null) {
      vector3f.mulPosition(this.worldContext);
    }
    return this.pushFloats("position", vector3f.x, vector3f.y, vector3f.z);
  }

  public VertexBuffer color(int r, int g, int b, int alpha) {
    if (!this.getFormat().hasElement("color"))
      return this;
    this.pushBytes("color", ((byte) r), ((byte) g), ((byte) b), ((byte) alpha));
    return this;
  }

  public VertexBuffer color(Color color) {
    if (color == null)
      return this;
    return this.color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
  }

  public VertexBuffer pos(Vector3f position) {
    return this.pos(position.x, position.y, position.z);
  }

  public VertexBufferImpl normal(float x, float y, float z) {
    if (!this.getFormat().hasElement("normal"))
      return this;
    Vector3f vector3f = new Vector3f(x, y, z);
    if (this.worldContext != null) {
      vector3f.mulPosition(this.worldContext);
    }
    return this.pushFloats("normal", vector3f.x, vector3f.y, vector3f.z);
  }

  public VertexBuffer normal(Vector3f normal) {
    return this.normal(normal.x, normal.y, normal.z);
  }

  public VertexBufferImpl end() {
    this.vertexCount++;
    if (this.writtenBytes != this.vertexCount * this.vertexFormat.getSize()) {
      throw new IllegalStateException("Not all or too many vertex elements have been written.");
    }
    try {
      vertexCountFieldSetter.invoke(this.buffer, this.vertexCount);
    } catch (Throwable throwable) {
      throwable.printStackTrace();
    }
    return this;
  }

  public VertexBufferImpl lightmap(short sky, short ground) {
    if (!this.getFormat().hasElement("lightmap"))
      return this;
    this.pushShorts("lightmap", sky, ground);
    return this;
  }

  public VertexBuffer lightmap(int masked) {
    return this.lightmap((short) (masked & 0x00ff), (short) (((masked & 0xffff0000) >> 16) & 0xffff));
  }

  public VertexBufferImpl texture(float x, float y) {
    if (!this.getFormat().hasElement("texture"))
      return this;
    this.pushFloats("texture", x, y);
    return this;
  }

  public VertexBuffer texture(Vector2f texture) {
    return this.texture(texture.x, texture.y);
  }

  public VertexBufferImpl pushFloats(String name, float... floats) {
    this.growBufferEventually(((this.vertexCount + 1) * this.vertexFormat.getSize()));
    vertexFormat.pushFloats(this.byteBuffer, this, name, floats);
    this.writtenBytes += floats.length * Float.BYTES;
    return this;
  }

  public VertexBufferImpl pushBytes(String name, byte... bytes) {
    this.growBufferEventually(((this.vertexCount + 1) * this.vertexFormat.getSize()));
    vertexFormat.pushBytes(this.byteBuffer, this, name, bytes);
    this.writtenBytes += bytes.length;
    return this;
  }

  public AdvancedVertexBuffer pushShorts(String name, short... shorts) {
    this.growBufferEventually(((this.vertexCount + 1) * this.vertexFormat.getSize()));
    vertexFormat.pushShorts(this.byteBuffer, this, name, shorts);
    this.writtenBytes += shorts.length * Short.BYTES;
    return this;
  }

  public AdvancedVertexBuffer incrementVertexCount(int count) {
    this.vertexCount += count;
    try {
      vertexCountFieldSetter.invoke(this.buffer, this.vertexCount);
    } catch (Throwable throwable) {
      throwable.printStackTrace();
    }
    return this;
  }

  public VertexBufferImpl growBufferEventually(int targetSize) {
    if (this.byteBuffer.limit() < targetSize) {
      ByteBuffer oldBuffer = this.byteBuffer;
      this.byteBuffer = ByteBuffer.allocateDirect(targetSize);
      this.byteBuffer.put(oldBuffer);
      try {
        byteBufferFieldSetter.invoke(this.buffer, byteBuffer);
      } catch (Throwable throwable) {
        throwable.printStackTrace();
      }
    }
    return this;
  }

  public VertexFormat getFormat() {
    return this.vertexFormat;
  }

  public Matrix4f getWorldContext() {
    return new Matrix4f(this.worldContext);
  }

  public VertexBuffer setWorldContext(Matrix4f matrix) {
    if (matrix == null) matrix = new Matrix4f();
    this.worldContext = new Matrix4f(matrix);
    return this;
  }

  public VertexBuffer setNormalContext(Matrix3f normalContext) {
    if (normalContext == null) normalContext = new Matrix3f();
    this.normalContext = new Matrix3f(normalContext);
    return this;
  }

  public Matrix3f getNormalContext() {
    return new Matrix3f(normalContext);
  }

  public AdvancedVertexBuffer advanced() {
    return this;
  }

  public VertexBuffer simple() {
    return this;
  }

  public ByteBuffer getByteBuffer() {
    return this.byteBuffer;
  }

  public AdvancedVertexBuffer setByteBuffer(ByteBuffer byteBuffer) {
    this.byteBuffer = byteBuffer;
    return this;
  }

  public int getVertexCount() {
    return this.vertexCount;
  }

}
