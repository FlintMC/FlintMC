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
import net.flintmc.util.math.matrix.Matrix4x4;
import net.flintmc.util.math.rotation.Quaternion;

/**
 * {@inheritDoc}
 */
@SuppressWarnings("unchecked")
public abstract class BaseMatrix4x4<
    T_Number extends Number, T_Matrix4 extends Matrix4x4<T_Number, T_Matrix4>>
    implements Matrix4x4<T_Number, T_Matrix4> {

  protected T_Number m00, m01, m02, m03;
  protected T_Number m10, m11, m12, m13;
  protected T_Number m20, m21, m22, m23;
  protected T_Number m30, m31, m32, m33;

  @Override
  public T_Matrix4 rotate(Quaternion<T_Number, ?> quaternion) {
    return this.rotate(quaternion, (T_Matrix4) this);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public T_Matrix4 invert() {
    return this.invert((T_Matrix4) this);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public T_Matrix4 transpose() {
    return this.transpose((T_Matrix4) this);
  }

  /** {@inheritDoc} */
  @Override
  public T_Matrix4 mul(T_Matrix4 right) {
    return this.mul(right, (T_Matrix4) this);
  }

  /** {@inheritDoc} */
  @Override
  public T_Matrix4 scale(T_Number factor) {
    return this.scale(factor, (T_Matrix4) this);
  }

  /** {@inheritDoc} */
  @Override
  public T_Matrix4 scale(T_Number factorX, T_Number factorY, T_Number factorZ) {
    return this.scale(factorX, factorY, factorZ, (T_Matrix4) this);
  }

  /** {@inheritDoc} */
  @Override
  public T_Matrix4 translate(T_Number x, T_Number y, T_Number z) {
    return this.translate(x, y, z, (T_Matrix4) this);
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
  public synchronized T_Number getM03() {
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
  public synchronized T_Number getM13() {
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
  public synchronized T_Number getM23() {
    return m22;
  }

  /** {@inheritDoc} */
  @Override
  public synchronized T_Number getM30() {
    return m30;
  }

  /** {@inheritDoc} */
  @Override
  public synchronized T_Number getM31() {
    return m31;
  }

  /** {@inheritDoc} */
  @Override
  public synchronized T_Number getM32() {
    return m32;
  }

  /** {@inheritDoc} */
  @Override
  public synchronized T_Number getM33() {
    return m33;
  }

  /** {@inheritDoc} */
  @Override
  public synchronized T_Matrix4 setM00(T_Number m00) {
    this.m00 = m00;
    return (T_Matrix4) this;
  }

  /** {@inheritDoc} */
  @Override
  public synchronized T_Matrix4 setM01(T_Number m01) {
    this.m01 = m01;
    return (T_Matrix4) this;
  }

  /** {@inheritDoc} */
  @Override
  public synchronized T_Matrix4 setM02(T_Number m02) {
    this.m02 = m02;
    return (T_Matrix4) this;
  }

  /** {@inheritDoc} */
  @Override
  public synchronized T_Matrix4 setM03(T_Number m03) {
    this.m03 = m03;
    return (T_Matrix4) this;
  }

  /** {@inheritDoc} */
  @Override
  public synchronized T_Matrix4 setM10(T_Number m10) {
    this.m10 = m10;
    return (T_Matrix4) this;
  }

  /** {@inheritDoc} */
  @Override
  public synchronized T_Matrix4 setM11(T_Number m11) {
    this.m11 = m11;
    return (T_Matrix4) this;
  }

  /** {@inheritDoc} */
  @Override
  public synchronized T_Matrix4 setM12(T_Number m12) {
    this.m12 = m12;
    return (T_Matrix4) this;
  }

  /** {@inheritDoc} */
  @Override
  public synchronized T_Matrix4 setM13(T_Number m13) {
    this.m13 = m13;
    return (T_Matrix4) this;
  }

  /** {@inheritDoc} */
  @Override
  public synchronized T_Matrix4 setM20(T_Number m20) {
    this.m20 = m20;
    return (T_Matrix4) this;
  }

  /** {@inheritDoc} */
  @Override
  public synchronized T_Matrix4 setM21(T_Number m21) {
    this.m21 = m21;
    return (T_Matrix4) this;
  }

  /** {@inheritDoc} */
  @Override
  public synchronized T_Matrix4 setM22(T_Number m22) {
    this.m22 = m22;
    return (T_Matrix4) this;
  }

  /** {@inheritDoc} */
  @Override
  public synchronized T_Matrix4 setM23(T_Number m23) {
    this.m23 = m23;
    return (T_Matrix4) this;
  }

  /** {@inheritDoc} */
  @Override
  public synchronized T_Matrix4 setM30(T_Number m30) {
    this.m30 = m30;
    return (T_Matrix4) this;
  }

  /** {@inheritDoc} */
  @Override
  public synchronized T_Matrix4 setM31(T_Number m31) {
    this.m31 = m31;
    return (T_Matrix4) this;
  }

  /** {@inheritDoc} */
  @Override
  public synchronized T_Matrix4 setM32(T_Number m32) {
    this.m32 = m32;
    return (T_Matrix4) this;
  }

  /** {@inheritDoc} */
  @Override
  public synchronized T_Matrix4 setM33(T_Number m33) {
    this.m33 = m33;
    return (T_Matrix4) this;
  }

  /** {@inheritDoc} */
  @Override
  public synchronized T_Matrix4 set(
      T_Number m00,
      T_Number m01,
      T_Number m02,
      T_Number m03,
      T_Number m10,
      T_Number m11,
      T_Number m12,
      T_Number m13,
      T_Number m20,
      T_Number m21,
      T_Number m22,
      T_Number m23,
      T_Number m30,
      T_Number m31,
      T_Number m32,
      T_Number m33) {

    this.m00 = m00;
    this.m01 = m01;
    this.m02 = m02;
    this.m03 = m03;

    this.m10 = m10;
    this.m11 = m11;
    this.m12 = m12;
    this.m13 = m13;

    this.m20 = m20;
    this.m21 = m21;
    this.m22 = m22;
    this.m23 = m23;

    this.m30 = m30;
    this.m31 = m31;
    this.m32 = m32;
    this.m33 = m33;
    return (T_Matrix4) this;
  }

  @Override
  public T_Matrix4 set3x3(Matrix3x3<T_Number, ?> matrix3x3f) {
    return this.set3x3(matrix3x3f, (T_Matrix4) this);
  }

  @Override
  public T_Matrix4 set3x3(Matrix3x3<T_Number, ?> matrix3x3f, T_Matrix4 target) {
    return target
        .setM00(matrix3x3f.getM00())
        .setM01(matrix3x3f.getM01())
        .setM02(matrix3x3f.getM02())
        .setM10(matrix3x3f.getM10())
        .setM11(matrix3x3f.getM11())
        .setM12(matrix3x3f.getM12())
        .setM20(matrix3x3f.getM20())
        .setM21(matrix3x3f.getM21())
        .setM22(matrix3x3f.getM22());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public T_Matrix4 scale(T_Number factor, T_Matrix4 target) {
    return this.scale(factor, factor, factor, target);
  }
}
