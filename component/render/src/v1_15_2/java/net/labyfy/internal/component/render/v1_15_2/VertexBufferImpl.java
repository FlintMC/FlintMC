package net.labyfy.internal.component.render.v1_15_2;


import net.labyfy.component.render.AdvancedVertexBuffer;
import net.labyfy.component.render.VertexBuffer;
import net.labyfy.component.render.VertexFormat;
import net.minecraft.client.renderer.BufferBuilder;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;

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
    Vector3f vector3f = new Vector3f(x, y, z);
    if (this.worldContext != null) {
      vector3f.mulPosition(this.worldContext);
    }
    return this.pushFloats("position", vector3f.x, vector3f.y, vector3f.z);
  }

  public VertexBuffer pos(Vector3f position) {
    return this.pos(position.x, position.y, position.z);
  }

  public VertexBufferImpl normal(float x, float y, float z) {
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
    if (this.writtenBytes != this.vertexCount * this.vertexFormat.getBytes()) {
      throw new IllegalStateException("Not all or too many vertex elements have been written.");
    }
    try {
      vertexCountFieldSetter.invoke(this.buffer, this.vertexCount);
    } catch (Throwable throwable) {
      throwable.printStackTrace();
    }
    return this;
  }

  public VertexBufferImpl pushFloats(String name, float... floats) {
    this.growBufferEventually(((this.vertexCount + 1) * this.vertexFormat.getBytes()));
    vertexFormat.pushFloats(this.byteBuffer, this, name, floats);
    this.writtenBytes += floats.length * Float.BYTES;
    return this;
  }

  public VertexBufferImpl pushBytes(String name, byte... bytes) {
    this.growBufferEventually(((this.vertexCount + 1) * this.vertexFormat.getBytes()));
    vertexFormat.pushBytes(this.byteBuffer, this, name, bytes);
    this.writtenBytes += bytes.length;
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

  public VertexBufferImpl box(float x, float y, float z, float width, float height, float depth) {

    this
        .quad(
            x, y, z,
            x + width, y, z,
            x + width, y + height, z,
            x, y + height, z
        )
        .quad(
            x, y, z + depth,
            x + width, y, z + depth,
            x + width, y + height, z + depth,
            x, y + height, z + depth
        )
        .quad(
            x, y, z,
            x, y, z + depth,
            x + width, y, z + depth,
            x + width, y, z
        )
        .quad(
            x, y + height, z,
            x, y + height, z + depth,
            x + width, y + height, z + depth,
            x + width, y + height, z
        )
        .quad(
            x, y, z,
            x, y, z + depth,
            x, y + height, z + depth,
            x, y + height, z
        )
        .quad(
            x + width, y, z,
            x + width, y, z + depth,
            x + width, y + height, z + depth,
            x + width, y + height, z
        );

    return this;
  }

  public VertexBufferImpl quad(
      float x1, float y1, float z1,
      float x2, float y2, float z2,
      float x3, float y3, float z3,
      float x4, float y4, float z4
  ) {
    this
        .triangle(
            x1, y1, z1,
            x2, y2, z2,
            x3, y3, z3
        )
        .triangle(
            x3, y3, z3,
            x1, y1, z1,
            x4, y4, z4
        );

    return this;
  }

  public VertexBufferImpl triangle(
      float x1, float y1, float z1,
      float x2, float y2, float z2,
      float x3, float y3, float z3
  ) {
    this
        .pos(x1, y1, z1)
        .end()
        .pos(x2, y2, z2)
        .end()
        .pos(x3, y3, z3)
        .end();

    return this;
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
