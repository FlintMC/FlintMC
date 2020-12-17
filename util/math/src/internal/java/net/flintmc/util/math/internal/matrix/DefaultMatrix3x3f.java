package net.flintmc.util.math.internal.matrix;

import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.util.math.matrix.Matrix3x3f;
import net.flintmc.util.math.matrix.Matrix4x4f;
import org.joml.Math;

import java.nio.FloatBuffer;

/** {@inheritDoc} */
@Implement(Matrix3x3f.class)
public class DefaultMatrix3x3f extends BaseMatrix3x3<Float, Matrix3x3f> implements Matrix3x3f {

  @AssistedInject
  private DefaultMatrix3x3f() {}


  /**
   * {@inheritDoc}
   */
  @Override
  public Matrix3x3f write(FloatBuffer floatBuffer) {
    floatBuffer.put(0, this.m00);
    floatBuffer.put(1, this.m01);
    floatBuffer.put(2, this.m02);

    floatBuffer.put(3, this.m10);
    floatBuffer.put(4, this.m11);
    floatBuffer.put(5, this.m12);

    floatBuffer.put(6, this.m20);
    floatBuffer.put(7, this.m21);
    floatBuffer.put(8, this.m22);

    return this;
  }


  /** {@inheritDoc} */
  @Override
  public synchronized Matrix3x3f invert(Matrix3x3f target) {
    float a = Math.fma(m00, m11, -m01 * m10);
    float b = Math.fma(m02, m10, -m00 * m12);
    float c = Math.fma(m01, m12, -m02 * m11);
    float d = Math.fma(a, m22, Math.fma(b, m21, c * m20));
    float s = 1.0f / d;

    float newM00 = Math.fma(m11, m22, -m21 * m12) * s;
    float newM01 = Math.fma(m21, m02, -m01 * m22) * s;
    float newM02 = c * s;

    float newM10 = Math.fma(m20, m12, -m10 * m22) * s;
    float newM11 = Math.fma(m00, m22, -m20 * m02) * s;
    float newM12 = b * s;

    float newM20 = Math.fma(m10, m21, -m20 * m11) * s;
    float newM21 = Math.fma(m20, m01, -m00 * m21) * s;
    float newM22 = a * s;

    return target.set(newM00, newM01, newM02, newM10, newM11, newM12, newM20, newM21, newM22);
  }

  @Override
  public Matrix3x3f rotate(float ang, float x, float y, float z) {
    return this.rotate(ang, x, y, z, (Matrix3x3f) this);
  }

  @Override
  public Matrix3x3f rotate(float ang, float x, float y, float z, Matrix3x3f target) {
    float s = Math.sin(ang);
    float c = Math.cosFromSin(s, ang);
    float C = 1.0f - c;

    // rotation matrix elements:
    // m30, m31, m32, m03, m13, m23 = 0
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

    // add temporaries for dependent values
    float nm00 = m00 * rm00 + m10 * rm01 + m20 * rm02;
    float nm01 = m01 * rm00 + m11 * rm01 + m21 * rm02;
    float nm02 = m02 * rm00 + m12 * rm01 + m22 * rm02;
    float nm10 = m00 * rm10 + m10 * rm11 + m20 * rm12;
    float nm11 = m01 * rm10 + m11 * rm11 + m21 * rm12;
    float nm12 = m02 * rm10 + m12 * rm11 + m22 * rm12;
    // set non-dependent values directly
    target.setM20(m00 * rm20 + m10 * rm21 + m20 * rm22);
    target.setM21(m01 * rm20 + m11 * rm21 + m21 * rm22);
    target.setM22(m02 * rm20 + m12 * rm21 + m22 * rm22);
    // set other values
    target.setM00(nm00);
    target.setM01(nm01);
    target.setM02(nm02);
    target.setM10(nm10);
    target.setM11(nm11);
    target.setM12(nm12);
    return target;
  }

  /** {@inheritDoc} */
  @Override
  public synchronized Matrix3x3f transpose(Matrix3x3f target) {
    return target.set(m00, m10, m20, m01, m11, m21, m02, m12, m22);
  }

  /** {@inheritDoc} */
  @Override
  public synchronized Matrix3x3f copy(Matrix3x3f target) {
    return target.set(m00, m01, m02, m10, m11, m12, m20, m21, m22);
  }

  /** {@inheritDoc} */
  @Override
  public synchronized Matrix3x3f mul(Matrix3x3f right, Matrix3x3f target) {
    float newM00 =
        Math.fma(m00, right.getM00(), Math.fma(m10, right.getM01(), m20 * right.getM02()));
    float newM01 =
        Math.fma(m01, right.getM00(), Math.fma(m11, right.getM01(), m21 * right.getM02()));
    float newM02 =
        Math.fma(m02, right.getM00(), Math.fma(m12, right.getM01(), m22 * right.getM02()));
    float newM10 =
        Math.fma(m00, right.getM10(), Math.fma(m10, right.getM11(), m20 * right.getM12()));
    float newM11 =
        Math.fma(m01, right.getM10(), Math.fma(m11, right.getM11(), m21 * right.getM12()));
    float newM12 =
        Math.fma(m02, right.getM10(), Math.fma(m12, right.getM11(), m22 * right.getM12()));
    float newM20 =
        Math.fma(m00, right.getM20(), Math.fma(m10, right.getM21(), m20 * right.getM22()));
    float newM21 =
        Math.fma(m01, right.getM20(), Math.fma(m11, right.getM21(), m21 * right.getM22()));
    float newM22 =
        Math.fma(m02, right.getM20(), Math.fma(m12, right.getM21(), m22 * right.getM22()));

    return target.set(newM00, newM01, newM02, newM10, newM11, newM12, newM20, newM21, newM22);
  }

  /** {@inheritDoc} */
  @Override
  public synchronized Matrix3x3f scale(
      Float factorX, Float factorY, Float factorZ, Matrix3x3f target) {
    return target.set(
        m00 * factorX,
        m01 * factorX,
        m02 * factorX,
        m10 * factorY,
        m11 * factorY,
        m12 * factorY,
        m20 * factorZ,
        m21 * factorZ,
        m22 * factorZ);
  }

  /** {@inheritDoc} */
  @Override
  public Matrix3x3f setIdentity() {
    return this.set(1f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 1f);
  }
}
