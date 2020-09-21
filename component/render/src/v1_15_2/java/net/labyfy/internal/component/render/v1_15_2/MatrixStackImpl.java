package net.labyfy.internal.component.render.v1_15_2;

import net.labyfy.component.commons.math.MathFactory;
import net.labyfy.component.render.MatrixStack;
import net.labyfy.component.render.VertexBuffer;
import net.labyfy.internal.component.render.HookedMatrix3f;
import net.labyfy.internal.component.render.HookedMatrix4f;
import net.minecraft.client.renderer.Quaternion;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class MatrixStackImpl implements MatrixStack {

  private final MathFactory mathFactory;
  private final VertexBuffer vertexBuffer;
  private final com.mojang.blaze3d.matrix.MatrixStack handle;

  public MatrixStackImpl(MathFactory mathFactory, VertexBuffer vertexBuffer, com.mojang.blaze3d.matrix.MatrixStack handle) {
    this.mathFactory = mathFactory;
    this.vertexBuffer = vertexBuffer;
    this.handle = handle;
  }

  /**
   * {@inheritDoc}
   */
  public MatrixStackImpl push() {
    this.handle.push();
    this.updateVertexBuffer();
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public MatrixStackImpl rotate(Quaternionf quaternionf) {
    this.handle.rotate(new Quaternion(quaternionf.x, quaternionf.y, quaternionf.z, quaternionf.w));
    this.updateVertexBuffer();
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public MatrixStackImpl rotate(float angle, float xAxis, float yAxis, float zAxis) {
    return this.rotate(angle, mathFactory.getVector3f(xAxis, yAxis, zAxis));
  }

  /**
   * {@inheritDoc}
   */
  public MatrixStackImpl rotate(float angle, Vector3f axis) {
    return this.rotate(mathFactory.getQuaternionf().rotateAxis(angle, axis));
  }

  /**
   * {@inheritDoc}
   */
  public MatrixStackImpl rotate(float angle, float xAxis, float yAxis, float zAxis, float xPoint, float yPoint, float zPoint) {
    return this.rotate(angle, mathFactory.getVector3f(xAxis, yAxis, zAxis), mathFactory.getVector3f(xPoint, yPoint, zPoint));
  }


  /**
   * {@inheritDoc}
   */
  public MatrixStackImpl rotate(float angle, Vector3f axis, Vector3f point) {
    return this.rotate(mathFactory.getQuaternionf().rotateAxis(angle, axis), point);
  }

  /**
   * {@inheritDoc}
   */
  public MatrixStackImpl rotate(Quaternionf rotation, Vector3f point) {
    this.translate(-point.x, -point.y, -point.z);
    this.rotate(rotation);
    this.translate(point.x, point.y, point.z);
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public MatrixStackImpl translate(Vector3f vector3f) {
    this.handle.translate(vector3f.x, vector3f.y, vector3f.z);
    this.updateVertexBuffer();
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public MatrixStackImpl translate(float x, float y, float z) {
    this.handle.translate(x, y, z);
    this.updateVertexBuffer();
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public MatrixStack mul(Matrix4f matrix4f) {
    if (matrix4f == null) return this;
    this.handle.getLast().getMatrix().mul(convert(matrix4f));
    this.handle.getLast().getNormal().mul(convert(matrix4f.get3x3(mathFactory.getMatrix3f())));
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
      HookedMatrix4f minecraftMatrix = (HookedMatrix4f) (Object) new net.minecraft.client.renderer.Matrix4f();

      minecraftMatrix.m00(matrix4f.m00());
      minecraftMatrix.m10(matrix4f.m01());
      minecraftMatrix.m20(matrix4f.m02());
      minecraftMatrix.m30(matrix4f.m03());
      minecraftMatrix.m01(matrix4f.m10());
      minecraftMatrix.m11(matrix4f.m11());
      minecraftMatrix.m21(matrix4f.m12());
      minecraftMatrix.m31(matrix4f.m13());
      minecraftMatrix.m02(matrix4f.m20());
      minecraftMatrix.m12(matrix4f.m21());
      minecraftMatrix.m22(matrix4f.m22());
      minecraftMatrix.m32(matrix4f.m23());
      minecraftMatrix.m03(matrix4f.m30());
      minecraftMatrix.m13(matrix4f.m31());
      minecraftMatrix.m23(matrix4f.m32());
      minecraftMatrix.m33(matrix4f.m33());
      return (net.minecraft.client.renderer.Matrix4f) (Object) minecraftMatrix;
    } catch (Throwable throwable) {
      throwable.printStackTrace();
    }
    return null;
  }

  private net.minecraft.client.renderer.Matrix3f convert(Matrix3f matrix3f) {
    try {
      HookedMatrix3f hookedMatrix3f = (HookedMatrix3f) (Object) new net.minecraft.client.renderer.Matrix3f();


      hookedMatrix3f.m00(matrix3f.m00());
      hookedMatrix3f.m10(matrix3f.m01());
      hookedMatrix3f.m20(matrix3f.m02());
      hookedMatrix3f.m01(matrix3f.m10());
      hookedMatrix3f.m11(matrix3f.m11());
      hookedMatrix3f.m21(matrix3f.m12());
      hookedMatrix3f.m02(matrix3f.m20());
      hookedMatrix3f.m12(matrix3f.m21());
      hookedMatrix3f.m22(matrix3f.m22());
      return (net.minecraft.client.renderer.Matrix3f) (Object) hookedMatrix3f;
    } catch (Throwable throwable) {
      throwable.printStackTrace();
    }
    return null;
  }


  private Matrix3f convert(net.minecraft.client.renderer.Matrix3f matrix3f) {
    try {
      HookedMatrix3f hookedMatrix3f = (HookedMatrix3f) ((Object) matrix3f);
      return mathFactory.getMatrix3f(
          hookedMatrix3f.m00(),
          hookedMatrix3f.m10(),
          hookedMatrix3f.m20(),
          hookedMatrix3f.m01(),
          hookedMatrix3f.m11(),
          hookedMatrix3f.m21(),
          hookedMatrix3f.m02(),
          hookedMatrix3f.m12(),
          hookedMatrix3f.m22()
      );
    } catch (Throwable throwable) {
      throwable.printStackTrace();
    }
    return null;
  }

  private Matrix4f convert(net.minecraft.client.renderer.Matrix4f matrix4f) {
    try {
      HookedMatrix4f hookedMatrix4f = (HookedMatrix4f) ((Object) matrix4f);
      return mathFactory.getMatrix4f(
          hookedMatrix4f.m00(),
          hookedMatrix4f.m10(),
          hookedMatrix4f.m20(),
          hookedMatrix4f.m30(),
          hookedMatrix4f.m01(),
          hookedMatrix4f.m11(),
          hookedMatrix4f.m21(),
          hookedMatrix4f.m31(),
          hookedMatrix4f.m02(),
          hookedMatrix4f.m12(),
          hookedMatrix4f.m22(),
          hookedMatrix4f.m32(),
          hookedMatrix4f.m03(),
          hookedMatrix4f.m13(),
          hookedMatrix4f.m23(),
          hookedMatrix4f.m33()
      );
    } catch (Throwable ex) {
      ex.printStackTrace();
    }
    return null;
  }

  /**
   * {@inheritDoc}
   */
  public MatrixStackImpl scale(Vector3f vector3f) {
    return this.scale(vector3f.x, vector3f.y, vector3f.z);
  }

  /**
   * {@inheritDoc}
   */
  public MatrixStackImpl scale(float scale) {
    return this.scale(scale, scale, scale);
  }

  /**
   * {@inheritDoc}
   */
  public MatrixStackImpl scale(float x, float y, float z) {
    this.handle.scale(x, y, z);
    this.updateVertexBuffer();
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public MatrixStackImpl pop() {
    this.handle.pop();
    this.updateVertexBuffer();
    return this;
  }


}
