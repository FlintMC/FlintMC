package net.flintmc.util.math.internal.matrix;

import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.util.math.matrix.Matrix4x4f;
import org.joml.Math;

@Implement(Matrix4x4f.class)
public class DefaultMatrix4x4f extends BaseMatrix4x4<Float, Matrix4x4f> implements Matrix4x4f {

  @AssistedInject
  private DefaultMatrix4x4f() {
  }

  @SuppressWarnings("DuplicatedCode")
  public Matrix4x4f invert(Matrix4x4f target) {
    float a = m00 * m11 - m01 * m10;
    float b = m00 * m12 - m02 * m10;
    float c = m00 * m13 - m03 * m10;
    float d = m01 * m12 - m02 * m11;
    float e = m01 * m13 - m03 * m11;
    float f = m02 * m13 - m03 * m12;
    float g = m20 * m31 - m21 * m30;
    float h = m20 * m32 - m22 * m30;
    float i = m20 * m33 - m23 * m30;
    float j = m21 * m32 - m22 * m31;
    float k = m21 * m33 - m23 * m31;
    float l = m22 * m33 - m23 * m32;
    float det = a * l - b * k + c * j + d * i - e * h + f * g;
    det = 1.0f / det;
    return target
        .setM00(Math.fma(m11, l, Math.fma(-m12, k, m13 * j)) * det)
        .setM01(Math.fma(-m01, l, Math.fma(m02, k, -m03 * j)) * det)
        .setM02(Math.fma(m31, f, Math.fma(-m32, e, m33 * d)) * det)
        .setM03(Math.fma(-m21, f, Math.fma(m22, e, -m23 * d)) * det)
        .setM10(Math.fma(-m10, l, Math.fma(m12, i, -m13 * h)) * det)
        .setM11(Math.fma(m00, l, Math.fma(-m02, i, m03 * h)) * det)
        .setM12(Math.fma(-m30, f, Math.fma(m32, c, -m33 * b)) * det)
        .setM13(Math.fma(m20, f, Math.fma(-m22, c, m23 * b)) * det)
        .setM20(Math.fma(m10, k, Math.fma(-m11, i, m13 * g)) * det)
        .setM21(Math.fma(-m00, k, Math.fma(m01, i, -m03 * g)) * det)
        .setM22(Math.fma(m30, e, Math.fma(-m31, c, m33 * a)) * det)
        .setM23(Math.fma(-m20, e, Math.fma(m21, c, -m23 * a)) * det)
        .setM30(Math.fma(-m10, j, Math.fma(m11, h, -m12 * g)) * det)
        .setM31(Math.fma(m00, j, Math.fma(-m01, h, m02 * g)) * det)
        .setM32(Math.fma(-m30, d, Math.fma(m31, b, -m32 * a)) * det)
        .setM33(Math.fma(m20, d, Math.fma(-m21, b, m22 * a)) * det);
  }

  public synchronized Matrix4x4f transpose(Matrix4x4f target) {
    return target.set(
        this.m00, this.m10, this.m20, this.m30, this.m01, this.m11, this.m21, this.m31, this.m02,
        this.m12, this.m22, this.m32, this.m03, this.m13, this.m23, this.m33);
  }

  public synchronized Matrix4x4f copy(Matrix4x4f target) {
    return target.set(
        this.m00, this.m01, this.m02, this.m03, this.m10, this.m11, this.m12, this.m13, this.m20,
        this.m21, this.m22, this.m23, this.m30, this.m31, this.m32, this.m33);
  }

  @SuppressWarnings("DuplicatedCode")
  public synchronized Matrix4x4f mul(Matrix4x4f right, Matrix4x4f target) {
    float nm00 =
        Math.fma(
            m00,
            right.getM00(),
            Math.fma(m10, right.getM01(), Math.fma(m20, right.getM02(), m30 * right.getM03())));
    float nm01 =
        Math.fma(
            m01,
            right.getM00(),
            Math.fma(m11, right.getM01(), Math.fma(m21, right.getM02(), m31 * right.getM03())));
    float nm02 =
        Math.fma(
            m02,
            right.getM00(),
            Math.fma(m12, right.getM01(), Math.fma(m22, right.getM02(), m32 * right.getM03())));
    float nm03 =
        Math.fma(
            m03,
            right.getM00(),
            Math.fma(m13, right.getM01(), Math.fma(m23, right.getM02(), m33 * right.getM03())));
    float nm10 =
        Math.fma(
            m00,
            right.getM10(),
            Math.fma(m10, right.getM11(), Math.fma(m20, right.getM12(), m30 * right.getM13())));
    float nm11 =
        Math.fma(
            m01,
            right.getM10(),
            Math.fma(m11, right.getM11(), Math.fma(m21, right.getM12(), m31 * right.getM13())));
    float nm12 =
        Math.fma(
            m02,
            right.getM10(),
            Math.fma(m12, right.getM11(), Math.fma(m22, right.getM12(), m32 * right.getM13())));
    float nm13 =
        Math.fma(
            m03,
            right.getM10(),
            Math.fma(m13, right.getM11(), Math.fma(m23, right.getM12(), m33 * right.getM13())));
    float nm20 =
        Math.fma(
            m00,
            right.getM20(),
            Math.fma(m10, right.getM21(), Math.fma(m20, right.getM22(), m30 * right.getM23())));
    float nm21 =
        Math.fma(
            m01,
            right.getM20(),
            Math.fma(m11, right.getM21(), Math.fma(m21, right.getM22(), m31 * right.getM23())));
    float nm22 =
        Math.fma(
            m02,
            right.getM20(),
            Math.fma(m12, right.getM21(), Math.fma(m22, right.getM22(), m32 * right.getM23())));
    float nm23 =
        Math.fma(
            m03,
            right.getM20(),
            Math.fma(m13, right.getM21(), Math.fma(m23, right.getM22(), m33 * right.getM23())));
    float nm30 =
        Math.fma(
            m00,
            right.getM30(),
            Math.fma(m10, right.getM31(), Math.fma(m20, right.getM32(), m30 * right.getM33())));
    float nm31 =
        Math.fma(
            m01,
            right.getM30(),
            Math.fma(m11, right.getM31(), Math.fma(m21, right.getM32(), m31 * right.getM33())));
    float nm32 =
        Math.fma(
            m02,
            right.getM30(),
            Math.fma(m12, right.getM31(), Math.fma(m22, right.getM32(), m32 * right.getM33())));
    float nm33 =
        Math.fma(
            m03,
            right.getM30(),
            Math.fma(m13, right.getM31(), Math.fma(m23, right.getM32(), m33 * right.getM33())));
    return target.set(
        nm00, nm01, nm02, nm03, nm10, nm11, nm12, nm13, nm20, nm21, nm22, nm23, nm30, nm31, nm32,
        nm33);
  }

  public synchronized Matrix4x4f scale(
      Float factorX, Float factorY, Float factorZ, Matrix4x4f target) {
    return target.set(
        this.m00 * factorX,
        this.m01 * factorX,
        this.m02 * factorX,
        this.m03 * factorY,
        this.m10 * factorY,
        this.m11 * factorY,
        this.m12 * factorY,
        this.m13 * factorY,
        this.m20 * factorZ,
        this.m21 * factorZ,
        this.m22 * factorZ,
        this.m23 * factorZ,
        this.m30,
        this.m31,
        this.m32,
        this.m33);
  }

  public Matrix4x4f translate(Float x, Float y, Float z, Matrix4x4f target) {
    return target.set(
        this.m00,
        this.m01,
        this.m02,
        this.m03,
        this.m10,
        this.m11,
        this.m12,
        this.m13,
        this.m20,
        this.m21,
        this.m22,
        this.m23,
        Math.fma(m00, x, Math.fma(m10, y, Math.fma(m20, z, m30))),
        Math.fma(m01, x, Math.fma(m11, y, Math.fma(m21, z, m31))),
        Math.fma(m02, x, Math.fma(m12, y, Math.fma(m22, z, m32))),
        Math.fma(m03, x, Math.fma(m13, y, Math.fma(m23, z, m33))));
  }

  public Matrix4x4f setIdentity() {
    return this.set(1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1f);
  }
}
