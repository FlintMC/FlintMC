package net.flintmc.util.math.internal.matrix;

import java.nio.FloatBuffer;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.util.math.matrix.Matrix4x4f;
import net.flintmc.util.math.rotation.Quaternion;
import org.joml.Math;

/**
 * {@inheritDoc}
 */
@Implement(Matrix4x4f.class)
public class DefaultMatrix4x4f extends BaseMatrix4x4<Float, Matrix4x4f> implements Matrix4x4f {

  @AssistedInject
  private DefaultMatrix4x4f() {
    this.setIdentity();
  }

  /** {@inheritDoc} */
  @Override
  public Matrix4x4f rotate(float radians, float x, float y, float z) {
    return this.rotate(radians, x, y, z, this);
  }

  /** {@inheritDoc} */
  @Override
  public Matrix4x4f rotate(float ang, float x, float y, float z, Matrix4x4f target) {
    float s = Math.sin(ang);
    float c = Math.cosFromSin(s, ang);
    float C = 1.0f - c;
    float xx = x * x, xy = x * y, xz = x * z;
    float yy = y * y, yz = y * z;
    float zz = z * z;
    float rm00 = xx * C + c;
    float rm01 = xy * C + z * s;
    float rm02 = xz * C - y * s;
    float rm10 = xy * C - z * s;
    float rm11 = yy * C + c;
    float rm12 = yz * C + x * s;
    float rm20 = xz * C + y * s;
    float rm21 = yz * C - x * s;
    float rm22 = zz * C + c;
    float nm00 = m00 * rm00 + m10 * rm01 + m20 * rm02;
    float nm01 = m01 * rm00 + m11 * rm01 + m21 * rm02;
    float nm02 = m02 * rm00 + m12 * rm01 + m22 * rm02;
    float nm03 = m03 * rm00 + m13 * rm01 + m23 * rm02;
    float nm10 = m00 * rm10 + m10 * rm11 + m20 * rm12;
    float nm11 = m01 * rm10 + m11 * rm11 + m21 * rm12;
    float nm12 = m02 * rm10 + m12 * rm11 + m22 * rm12;
    float nm13 = m03 * rm10 + m13 * rm11 + m23 * rm12;
    return target
        .setM20(m00 * rm20 + m10 * rm21 + m20 * rm22)
        .setM21(m01 * rm20 + m11 * rm21 + m21 * rm22)
        .setM22(m02 * rm20 + m12 * rm21 + m22 * rm22)
        .setM23(m03 * rm20 + m13 * rm21 + m23 * rm22)
        .setM00(nm00)
        .setM01(nm01)
        .setM02(nm02)
        .setM03(nm03)
        .setM10(nm10)
        .setM11(nm11)
        .setM12(nm12)
        .setM13(nm13)
        .setM30(m30)
        .setM31(m31)
        .setM32(m32)
        .setM33(m33);
  }

  @Override
  public Matrix4x4f rotate(Quaternion<Float, ?> quaternion, Matrix4x4f target) {
    float w2 = quaternion.getW() * quaternion.getW(), x2 = quaternion.getX() * quaternion.getX();
    float y2 = quaternion.getY() * quaternion.getY(), z2 = quaternion.getZ() * quaternion.getZ();
    float zw = quaternion.getZ() * quaternion.getW(),
        dzw = zw + zw,
        xy = quaternion.getX() * quaternion.getY(),
        dxy = xy + xy;
    float xz = quaternion.getX() * quaternion.getZ(),
        dxz = xz + xz,
        yw = quaternion.getY() * quaternion.getW(),
        dyw = yw + yw;
    float yz = quaternion.getY() * quaternion.getZ(),
        dyz = yz + yz,
        xw = quaternion.getX() * quaternion.getW(),
        dxw = xw + xw;
    float rm00 = w2 + x2 - z2 - y2;
    float rm01 = dxy + dzw;
    float rm02 = dxz - dyw;
    float rm10 = -dzw + dxy;
    float rm11 = y2 - z2 + w2 - x2;
    float rm12 = dyz + dxw;
    float rm20 = dyw + dxz;
    float rm21 = dyz - dxw;
    float rm22 = z2 - y2 - x2 + w2;
    float nm00 = m00 * rm00 + m10 * rm01 + m20 * rm02;
    float nm01 = m01 * rm00 + m11 * rm01 + m21 * rm02;
    float nm02 = m02 * rm00 + m12 * rm01 + m22 * rm02;
    float nm03 = m03 * rm00 + m13 * rm01 + m23 * rm02;
    float nm10 = m00 * rm10 + m10 * rm11 + m20 * rm12;
    float nm11 = m01 * rm10 + m11 * rm11 + m21 * rm12;
    float nm12 = m02 * rm10 + m12 * rm11 + m22 * rm12;
    float nm13 = m03 * rm10 + m13 * rm11 + m23 * rm12;
    return target
        .setM20(m00 * rm20 + m10 * rm21 + m20 * rm22)
        .setM21(m01 * rm20 + m11 * rm21 + m21 * rm22)
        .setM22(m02 * rm20 + m12 * rm21 + m22 * rm22)
        .setM23(m03 * rm20 + m13 * rm21 + m23 * rm22)
        .setM00(nm00)
        .setM01(nm01)
        .setM02(nm02)
        .setM03(nm03)
        .setM10(nm10)
        .setM11(nm11)
        .setM12(nm12)
        .setM13(nm13)
        .setM30(m30)
        .setM31(m31)
        .setM32(m32)
        .setM33(m33);
  }

  @SuppressWarnings("DuplicatedCode")
  /** {@inheritDoc} */
  @Override
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

  /** {@inheritDoc} */
  @Override
  public synchronized Matrix4x4f transpose(Matrix4x4f target) {
    return target.set(
        this.m00, this.m10, this.m20, this.m30, this.m01, this.m11, this.m21, this.m31, this.m02,
        this.m12, this.m22, this.m32, this.m03, this.m13, this.m23, this.m33);
  }

  /** {@inheritDoc} */
  @Override
  public synchronized Matrix4x4f copy(Matrix4x4f target) {
    return target.set(
        this.m00, this.m01, this.m02, this.m03, this.m10, this.m11, this.m12, this.m13, this.m20,
        this.m21, this.m22, this.m23, this.m30, this.m31, this.m32, this.m33);
  }

  @SuppressWarnings("DuplicatedCode")
  /** {@inheritDoc} */
  @Override
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

  /** {@inheritDoc} */
  @Override
  public synchronized Matrix4x4f scale(
      Float factorX, Float factorY, Float factorZ, Matrix4x4f target) {
    return target.set(
        this.m00 * factorX,
        this.m01 * factorX,
        this.m02 * factorX,
        this.m03 * factorX,
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

  /** {@inheritDoc} */
  @Override
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

  /** {@inheritDoc} */
  @Override
  public Matrix4x4f setIdentity() {
    return this.set(1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1f);
  }

  /** {@inheritDoc} */
  @Override
  public Matrix4x4f write(FloatBuffer floatBuffer) {
    floatBuffer.put(0, this.m00);
    floatBuffer.put(1, this.m01);
    floatBuffer.put(2, this.m02);
    floatBuffer.put(3, this.m03);

    floatBuffer.put(4, this.m10);
    floatBuffer.put(5, this.m11);
    floatBuffer.put(6, this.m12);
    floatBuffer.put(7, this.m13);

    floatBuffer.put(8, this.m20);
    floatBuffer.put(9, this.m21);
    floatBuffer.put(10, this.m22);
    floatBuffer.put(11, this.m23);

    floatBuffer.put(12, this.m30);
    floatBuffer.put(13, this.m31);
    floatBuffer.put(14, this.m32);
    floatBuffer.put(15, this.m33);
    return this;
  }
}
