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

  private static final Map<String, MethodHandle> matrix3fConversionGetters = new HashMap<>();
  private static final Map<String, MethodHandle> matrix3fConversionSetters = new HashMap<>();
  private static final Map<String, MethodHandle> matrix4fConversionSetters = new HashMap<>();

  static {
    for (int x = 0; x < 3; x++) {
      for (int y = 0; y < 3; y++) {
        String name = "m" + x + y;
        try {
          Field declaredField = net.minecraft.client.renderer.Matrix3f.class.getDeclaredField(name);
          declaredField.setAccessible(true);
          matrix3fConversionGetters.put(name, MethodHandles.lookup().unreflectGetter(declaredField));
          matrix3fConversionSetters.put(name, MethodHandles.lookup().unreflectSetter(declaredField));
        } catch (Exception ex) {
          ex.printStackTrace();
        }
      }
    }

    for (int x = 0; x < 4; x++) {
      for (int y = 0; y < 4; y++) {
        String name = "m" + x + y;
        try {
          Field declaredField = net.minecraft.client.renderer.Matrix4f.class.getDeclaredField(name);
          declaredField.setAccessible(true);
          matrix4fConversionSetters.put(name, MethodHandles.lookup().unreflectSetter(declaredField));
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
    this.handle.rotate(new Quaternion(quaternionf.x, quaternionf.y, quaternionf.z, quaternionf.w));
    this.updateVertexBuffer();
    return this;
  }

  public MatrixStackImpl rotate(float angle, float xAxis, float yAxis, float zAxis) {
    return this.rotate(angle, new Vector3f(xAxis, yAxis, zAxis));
  }

  public MatrixStackImpl rotate(float angle, Vector3f axis) {
    return this.rotate(new Quaternionf().rotateAxis(angle, axis));
  }

  public MatrixStackImpl rotate(float angle, float xAxis, float yAxis, float zAxis, float xPoint, float yPoint, float zPoint) {
    return this.rotate(angle, new Vector3f(xAxis, yAxis, zAxis), new Vector3f(xPoint, yPoint, zPoint));
  }


  public MatrixStackImpl rotate(float angle, Vector3f axis, Vector3f point) {
    return this.rotate(new Quaternionf().rotateAxis(angle, axis), point);
  }

  public MatrixStackImpl rotate(Quaternionf rotation, Vector3f point) {
    this.translate(-point.x, -point.y, -point.z);
    this.rotate(rotation);
    this.translate(point.x, point.y, point.z);
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

  private net.minecraft.client.renderer.Matrix4f convert(Matrix4f matrix4f) {
    try {
      net.minecraft.client.renderer.Matrix4f minecraftMatrix = new net.minecraft.client.renderer.Matrix4f();
      matrix4fConversionSetters.get("m00").invoke(minecraftMatrix, matrix4f.m00());
      matrix4fConversionSetters.get("m10").invoke(minecraftMatrix, matrix4f.m01());
      matrix4fConversionSetters.get("m20").invoke(minecraftMatrix, matrix4f.m02());
      matrix4fConversionSetters.get("m30").invoke(minecraftMatrix, matrix4f.m03());
      matrix4fConversionSetters.get("m01").invoke(minecraftMatrix, matrix4f.m10());
      matrix4fConversionSetters.get("m11").invoke(minecraftMatrix, matrix4f.m11());
      matrix4fConversionSetters.get("m21").invoke(minecraftMatrix, matrix4f.m12());
      matrix4fConversionSetters.get("m31").invoke(minecraftMatrix, matrix4f.m13());
      matrix4fConversionSetters.get("m02").invoke(minecraftMatrix, matrix4f.m20());
      matrix4fConversionSetters.get("m12").invoke(minecraftMatrix, matrix4f.m21());
      matrix4fConversionSetters.get("m22").invoke(minecraftMatrix, matrix4f.m22());
      matrix4fConversionSetters.get("m32").invoke(minecraftMatrix, matrix4f.m23());
      return minecraftMatrix;
    } catch (Throwable throwable) {
      throwable.printStackTrace();
    }
    return null;
  }

  private net.minecraft.client.renderer.Matrix3f convert(Matrix3f matrix3f) {
    try {
      net.minecraft.client.renderer.Matrix3f minecraftMatrix = new net.minecraft.client.renderer.Matrix3f();
      matrix3fConversionSetters.get("m00").invoke(minecraftMatrix, matrix3f.m00);
      matrix3fConversionSetters.get("m10").invoke(minecraftMatrix, matrix3f.m01);
      matrix3fConversionSetters.get("m20").invoke(minecraftMatrix, matrix3f.m02);
      matrix3fConversionSetters.get("m01").invoke(minecraftMatrix, matrix3f.m10);
      matrix3fConversionSetters.get("m11").invoke(minecraftMatrix, matrix3f.m11);
      matrix3fConversionSetters.get("m21").invoke(minecraftMatrix, matrix3f.m12);
      matrix3fConversionSetters.get("m02").invoke(minecraftMatrix, matrix3f.m20);
      matrix3fConversionSetters.get("m12").invoke(minecraftMatrix, matrix3f.m21);
      matrix3fConversionSetters.get("m22").invoke(minecraftMatrix, matrix3f.m22);
      return minecraftMatrix;
    } catch (Throwable throwable) {
      throwable.printStackTrace();
    }
    return null;
  }


  private Matrix3f convert(net.minecraft.client.renderer.Matrix3f matrix3f) {
    try {
      return new Matrix3f(
          (float) matrix3fConversionGetters.get("m00").invoke(matrix3f),
          (float) matrix3fConversionGetters.get("m10").invoke(matrix3f),
          (float) matrix3fConversionGetters.get("m20").invoke(matrix3f),
          (float) matrix3fConversionGetters.get("m01").invoke(matrix3f),
          (float) matrix3fConversionGetters.get("m11").invoke(matrix3f),
          (float) matrix3fConversionGetters.get("m21").invoke(matrix3f),
          (float) matrix3fConversionGetters.get("m02").invoke(matrix3f),
          (float) matrix3fConversionGetters.get("m12").invoke(matrix3f),
          (float) matrix3fConversionGetters.get("m22").invoke(matrix3f)
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
