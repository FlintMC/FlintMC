/*
 * FlintMC
 * Copyright (C) 2020-2021 LabyMedia GmbH and contributors
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package net.flintmc.util.math.internal.matrix;

import net.flintmc.util.math.matrix.Matrix3x3;

/**
 * {@inheritDoc}
 */
@SuppressWarnings("unchecked")
public abstract class BaseMatrix3x3<
    T_Number extends Number, T_Matrix3 extends Matrix3x3<T_Number, T_Matrix3>>
    implements Matrix3x3<T_Number, T_Matrix3> {

  protected T_Number m00, m01, m02;
  protected T_Number m10, m11, m12;
  protected T_Number m20, m21, m22;

  @Override
  public T_Matrix3 rotate(float ang, float x, float y, float z) {
    return this.rotate(ang, x, y, z, (T_Matrix3) this);
  }

  /** {@inheritDoc} */
  @Override
  public T_Matrix3 invert() {
    return this.invert((T_Matrix3) this);
  }

  /** {@inheritDoc} */
  @Override
  public T_Matrix3 transpose() {
    return this.transpose((T_Matrix3) this);
  }

  /** {@inheritDoc} */
  @Override
  public T_Matrix3 mul(T_Matrix3 right) {
    return this.mul(right, (T_Matrix3) this);
  }

  /** {@inheritDoc} */
  @Override
  public T_Matrix3 scale(T_Number factor) {
    return this.scale(factor, (T_Matrix3) this);
  }

  /** {@inheritDoc} */
  @Override
  public T_Matrix3 scale(T_Number factorX, T_Number factorY, T_Number factorZ) {
    return this.scale(factorX, factorY, factorZ, (T_Matrix3) this);
  }

  /** {@inheritDoc} */
  @Override
  public synchronized T_Number getM00() {
    return m00;
  }

  /** {@inheritDoc} */
  @Override
  public synchronized T_Number getM01() {
    return m01;
  }

  /** {@inheritDoc} */
  @Override
  public synchronized T_Number getM02() {
    return m02;
  }

  /** {@inheritDoc} */
  @Override
  public synchronized T_Number getM10() {
    return m10;
  }

  /** {@inheritDoc} */
  @Override
  public synchronized T_Number getM11() {
    return m11;
  }

  /** {@inheritDoc} */
  @Override
  public synchronized T_Number getM12() {
    return m12;
  }

  /** {@inheritDoc} */
  @Override
  public synchronized T_Number getM20() {
    return m20;
  }

  /** {@inheritDoc} */
  @Override
  public synchronized T_Number getM21() {
    return m21;
  }

  /** {@inheritDoc} */
  @Override
  public synchronized T_Number getM22() {
    return m22;
  }

  /** {@inheritDoc} */
  @Override
  public synchronized T_Matrix3 setM00(T_Number m00) {
    this.m00 = m00;
    return (T_Matrix3) this;
  }

  /** {@inheritDoc} */
  @Override
  public synchronized T_Matrix3 setM01(T_Number m01) {
    this.m01 = m01;
    return (T_Matrix3) this;
  }

  /** {@inheritDoc} */
  @Override
  public synchronized T_Matrix3 setM02(T_Number m02) {
    this.m02 = m02;
    return (T_Matrix3) this;
  }

  /** {@inheritDoc} */
  @Override
  public synchronized T_Matrix3 setM10(T_Number m10) {
    this.m10 = m10;
    return (T_Matrix3) this;
  }

  /** {@inheritDoc} */
  @Override
  public synchronized T_Matrix3 setM11(T_Number m11) {
    this.m11 = m11;
    return (T_Matrix3) this;
  }

  /** {@inheritDoc} */
  @Override
  public synchronized T_Matrix3 setM12(T_Number m12) {
    this.m12 = m12;
    return (T_Matrix3) this;
  }

  /** {@inheritDoc} */
  @Override
  public synchronized T_Matrix3 setM20(T_Number m20) {
    this.m20 = m20;
    return (T_Matrix3) this;
  }

  /** {@inheritDoc} */
  @Override
  public synchronized T_Matrix3 setM21(T_Number m21) {
    this.m21 = m21;
    return (T_Matrix3) this;
  }

  /** {@inheritDoc} */
  @Override
  public synchronized T_Matrix3 setM22(T_Number m22) {
    this.m22 = m22;
    return (T_Matrix3) this;
  }

  /** {@inheritDoc} */
  @Override
  public synchronized T_Matrix3 set(
      T_Number m00,
      T_Number m01,
      T_Number m02,
      T_Number m10,
      T_Number m11,
      T_Number m12,
      T_Number m20,
      T_Number m21,
      T_Number m22) {
    this.m00 = m00;
    this.m01 = m01;
    this.m02 = m02;

    this.m10 = m10;
    this.m11 = m11;
    this.m12 = m12;

    this.m20 = m20;
    this.m21 = m21;
    this.m22 = m22;
    return (T_Matrix3) this;
  }

  @Override
  public T_Matrix3 set(Matrix3x3<T_Number, ?> matrix3x3) {
    return this.set(
        matrix3x3.getM00(),
        matrix3x3.getM01(),
        matrix3x3.getM02(),
        matrix3x3.getM10(),
        matrix3x3.getM11(),
        matrix3x3.getM12(),
        matrix3x3.getM20(),
        matrix3x3.getM21(),
        matrix3x3.getM22());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public T_Matrix3 scale(T_Number factor, T_Matrix3 target) {
    return this.scale(factor, factor, factor, target);
  }
}
