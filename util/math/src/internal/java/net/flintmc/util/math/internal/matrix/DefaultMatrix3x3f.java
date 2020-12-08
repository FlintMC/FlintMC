package net.flintmc.util.math.internal.matrix;

import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.util.math.matrix.Matrix3x3f;
import org.joml.Math;

/**
 * {@inheritDoc}
 */
@Implement(Matrix3x3f.class)
public class DefaultMatrix3x3f extends BaseMatrix3x3<Float, Matrix3x3f> implements Matrix3x3f {

  @AssistedInject
  private DefaultMatrix3x3f() {
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

  /**
   * {@inheritDoc}
   */
  @Override
  public synchronized Matrix3x3f transpose(Matrix3x3f target) {
    return target.set(m00, m10, m20, m01, m11, m21, m02, m12, m22);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public synchronized Matrix3x3f copy(Matrix3x3f target) {
    return target.set(m00, m01, m02, m10, m11, m12, m20, m21, m22);
  }

  /**
   * {@inheritDoc}
   */
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

  /**
   * {@inheritDoc}
   */
  @Override
  public synchronized Matrix3x3f scale(Float factorX, Float factorY, Float factorZ, Matrix3x3f target) {
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

  /**
   * {@inheritDoc}
   */
  @Override
  public Matrix3x3f setIdentity() {
    return this.set(1f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 1f);
  }
}
