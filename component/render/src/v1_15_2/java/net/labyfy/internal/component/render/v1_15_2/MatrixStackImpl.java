package net.labyfy.internal.component.render.v1_15_2;

import net.labyfy.component.render.MatrixStack;
import net.labyfy.component.render.VertexBuffer;
import net.minecraft.client.renderer.Quaternion;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

public class MatrixStackImpl implements MatrixStack {

  private static final Map<String, MethodHandle> matrix3fConversionHandles = new HashMap<>();

  static {
    for (int x = 0; x < 3; x++) {
      for (int y = 0; y < 3; y++) {
        String name = "m" + x + y;
        try {
          Field declaredField = Matrix3f.class.getDeclaredField(name);
          declaredField.setAccessible(true);
          matrix3fConversionHandles.put(name, MethodHandles.lookup().unreflectGetter(declaredField));
        } catch (Exception ex) {
          ex.printStackTrace();
        }
      }
    }
  }

  private final VertexBuffer vertexBuffer;
  private final com.mojang.blaze3d.matrix.MatrixStack handle;

  public MatrixStackImpl(VertexBuffer vertexBuffer, com.mojang.blaze3d.matrix.MatrixStack handle) {
    this.vertexBuffer = vertexBuffer;
    this.handle = handle;
  }

  private static int bufferIndex4f(int a, int b) {
    return b * 4 + a;
  }

  public MatrixStackImpl push() {
    this.handle.push();
    this.updateVertexBuffer();
    return this;
  }

  public MatrixStackImpl rotate(Quaternionf quaternionf) {
    return this.rotate(quaternionf.x, quaternionf.y, quaternionf.z, quaternionf.w);
  }

  public MatrixStackImpl rotate(float x, float y, float z, float w) {
    this.handle.rotate(new Quaternion(x, y, z, w));
    this.updateVertexBuffer();
    return this;
  }

  public MatrixStackImpl translate(Vector3f vector3f) {
    this.handle.translate(vector3f.x, vector3f.y, vector3f.z);
    this.updateVertexBuffer();
    return this;
  }

  public MatrixStackImpl translate(float x, float y, float z) {
    this.handle.translate(x, y, z);
    this.updateVertexBuffer();
    return this;
  }

  public MatrixStackImpl updateVertexBuffer() {
    this.vertexBuffer.setWorldContext(this.convert(this.handle.getLast().getMatrix()));
    this.vertexBuffer.setNormalContext(this.convert(this.handle.getLast().getNormal()));
    return this;
  }

  private Matrix3f convert(net.minecraft.client.renderer.Matrix3f matrix3f) {
    try {
      return new Matrix3f(
          (float) matrix3fConversionHandles.get("m00").invoke(matrix3f),
          (float) matrix3fConversionHandles.get("m10").invoke(matrix3f),
          (float) matrix3fConversionHandles.get("m20").invoke(matrix3f),
          (float) matrix3fConversionHandles.get("m01").invoke(matrix3f),
          (float) matrix3fConversionHandles.get("m11").invoke(matrix3f),
          (float) matrix3fConversionHandles.get("m21").invoke(matrix3f),
          (float) matrix3fConversionHandles.get("m02").invoke(matrix3f),
          (float) matrix3fConversionHandles.get("m12").invoke(matrix3f),
          (float) matrix3fConversionHandles.get("m22").invoke(matrix3f)
      );
    } catch (Throwable throwable) {
      throwable.printStackTrace();
    }
    return null;
  }

  private Matrix4f convert(net.minecraft.client.renderer.Matrix4f matrix4f) {
    FloatBuffer floatBuffer = ByteBuffer.allocateDirect(Float.BYTES * 16).asFloatBuffer();
    matrix4f.write(floatBuffer);

    return new org.joml.Matrix4f(
        floatBuffer.get(bufferIndex4f(0, 0)),
        floatBuffer.get(bufferIndex4f(1, 0)),
        floatBuffer.get(bufferIndex4f(2, 0)),
        floatBuffer.get(bufferIndex4f(3, 0)),

        floatBuffer.get(bufferIndex4f(0, 1)),
        floatBuffer.get(bufferIndex4f(1, 1)),
        floatBuffer.get(bufferIndex4f(2, 1)),
        floatBuffer.get(bufferIndex4f(3, 1)),

        floatBuffer.get(bufferIndex4f(0, 2)),
        floatBuffer.get(bufferIndex4f(1, 2)),
        floatBuffer.get(bufferIndex4f(2, 2)),
        floatBuffer.get(bufferIndex4f(3, 2)),

        floatBuffer.get(bufferIndex4f(0, 3)),
        floatBuffer.get(bufferIndex4f(1, 3)),
        floatBuffer.get(bufferIndex4f(2, 3)),
        floatBuffer.get(bufferIndex4f(3, 3))
    );
  }

  public MatrixStackImpl scale(Vector3f vector3f) {
    return this.scale(vector3f.x, vector3f.y, vector3f.z);
  }

  public MatrixStackImpl scale(float scale) {
    return this.scale(scale, scale, scale);
  }

  public MatrixStackImpl scale(float x, float y, float z) {
    this.handle.scale(x, y, z);
    this.updateVertexBuffer();
    return this;
  }

  public MatrixStackImpl pop() {
    this.handle.pop();
    this.updateVertexBuffer();
    return this;
  }

  public boolean clear() {
    boolean clear = this.handle.clear();
    this.updateVertexBuffer();
    return clear;
  }

}
