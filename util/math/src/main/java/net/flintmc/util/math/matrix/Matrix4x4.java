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

package net.flintmc.util.math.matrix;

import net.flintmc.util.math.rotation.Quaternion;

/**
 * Generic 4x4 Matrix. Will be extended by many features and optimized in the feature.
 *
 * @param <T_Number>  Number type to use in this matrix
 * @param <T_Matrix4> Self reference. Just used for generic locking
 */
public interface Matrix4x4<
    T_Number extends Number, T_Matrix4 extends Matrix4x4<T_Number, T_Matrix4>> {

  /**
   * Rotate this matrix around a specified axis
   *
   * @param ang rotation in radians
   * @param x x axis
   * @param y y axis
   * @param z z axis
   * @return this
   */
  T_Matrix4 rotate(float ang, float x, float y, float z);

  /**
   * Rotate this matrix around a specified axis and writes the output to a target
   *
   * @param ang rotation in radians
   * @param x x axis
   * @param y y axis
   * @param z z axis
   * @param target the target to write to
   * @return target
   */
  T_Matrix4 rotate(float ang, float x, float y, float z, T_Matrix4 target);

  T_Matrix4 rotate(Quaternion<T_Number, ?> quaternion);

  T_Matrix4 rotate(Quaternion<T_Number, ?> quaternion, T_Matrix4 target);

  /**
   * Inverts this matrix
   *
   * @return this
   */
  T_Matrix4 invert();

  /**
   * Inverts this matrix and writes the output to a target
   *
   * @param target the target to write to
   * @return target
   */
  T_Matrix4 invert(T_Matrix4 target);

  /**
   * Transposes this matrix
   *
   * @return this
   */
  T_Matrix4 transpose();

  /**
   * Transposes this matrix and writes the output to a target
   *
   * @param target the target to write to
   * @return target
   */
  T_Matrix4 transpose(T_Matrix4 target);

  /**
   * Copies this matrix to a target
   *
   * @param target the target to write to
   * @return target
   */
  T_Matrix4 copy(T_Matrix4 target);

  /**
   * Multiplies this matrix with a second one
   *
   * @param right the right matrix to multiply this with
   * @return this
   */
  T_Matrix4 mul(T_Matrix4 right);

  /**
   * Multiplies this matrix with a second one
   *
   * @param right the right matrix to multiply this with
   * @param target the target to write to
   * @return target
   */
  T_Matrix4 mul(T_Matrix4 right, T_Matrix4 target);

  /**
   * Scale this matrix linear with a factor
   *
   * @param factor the factor to scale this with
   * @return this
   */
  T_Matrix4 scale(T_Number factor);

  /**
   * Scale this matrix linear with a factor
   *
   * @param factor the factor to scale this with
   * @param target the target to write to
   * @return target
   */
  T_Matrix4 scale(T_Number factor, T_Matrix4 target);

  /**
   * Scale this matrix linear with a factor
   *
   * @param factorX the x factor to scale this with
   * @param factorY the y factor to scale this with
   * @param factorZ the z factor to scale this with
   * @return this
   */
  T_Matrix4 scale(T_Number factorX, T_Number factorY, T_Number factorZ);

  /**
   * Scale this matrix linear with a factor
   *
   * @param factorX the x factor to scale this with
   * @param factorY the y factor to scale this with
   * @param factorZ the z factor to scale this with
   * @param target the target to write to
   * @return target
   */
  T_Matrix4 scale(T_Number factorX, T_Number factorY, T_Number factorZ, T_Matrix4 target);

  /**
   * Translates this matrix
   *
   * @param x the x factor to translate this with
   * @param y the y factor to translate this with
   * @param z the z factor to translate this with
   * @return this
   */
  T_Matrix4 translate(T_Number x, T_Number y, T_Number z);

  /**
   * Translates this matrix
   *
   * @param x the x factor to translate this with
   * @param y the y factor to translate this with
   * @param z the z factor to translate this with
   * @param target the target to write to
   * @return this
   */
  T_Matrix4 translate(T_Number x, T_Number y, T_Number z, T_Matrix4 target);

  /** @return value of row 0, column 0 */
  T_Number getM00();

  /**
   * Sets the value of row 0, column 0
   *
   * @param m00 new value
   * @return this
   */
  T_Matrix4 setM00(T_Number m00);

  /** @return value of row 0, column 1 */
  T_Number getM01();

  /**
   * Sets the value of row 0, column 1
   *
   * @param m01 new value
   * @return this
   */
  T_Matrix4 setM01(T_Number m01);

  /** @return value of row 0, column 2 */
  T_Number getM02();

  /**
   * Sets the value of row 0, column 2
   *
   * @param m02 new value
   * @return this
   */
  T_Matrix4 setM02(T_Number m02);

  /** @return value of row 0, column 3 */
  T_Number getM03();

  /**
   * Sets the value of row 0, column 3
   *
   * @param m03 new value
   * @return this
   */
  T_Matrix4 setM03(T_Number m03);

  /** @return value of row 1, column 0 */
  T_Number getM10();

  /**
   * Sets the value of row 1, column 0
   *
   * @param m10 new value
   * @return this
   */
  T_Matrix4 setM10(T_Number m10);

  /** @return value of row 1, column 1 */
  T_Number getM11();

  /**
   * Sets the value of row 1, column 1
   *
   * @param m11 new value
   * @return this
   */
  T_Matrix4 setM11(T_Number m11);

  /** @return value of row 1, column 2 */
  T_Number getM12();

  /**
   * Sets the value of row 1, column 2
   *
   * @param m12 new value
   * @return this
   */
  T_Matrix4 setM12(T_Number m12);

  /** @return value of row 1, column 3 */
  T_Number getM13();

  /**
   * Sets the value of row 1, column 3
   *
   * @param m13 new value
   * @return this
   */
  T_Matrix4 setM13(T_Number m13);

  /** @return value of row 2, column 0 */
  T_Number getM20();

  /**
   * Sets the value of row 2, column 0
   *
   * @param m20 new value
   * @return this
   */
  T_Matrix4 setM20(T_Number m20);

  /** @return value of row 2, column 1 */
  T_Number getM21();

  /**
   * Sets the value of row 2, column 1
   *
   * @param m21 new value
   * @return this
   */
  T_Matrix4 setM21(T_Number m21);

  /** @return value of row 2, column 2 */
  T_Number getM22();

  /**
   * Sets the value of row 2, column 2
   *
   * @param m22 new value
   * @return this
   */
  T_Matrix4 setM22(T_Number m22);

  /** @return value of row 2, column 3 */
  T_Number getM23();

  /**
   * Sets the value of row 2, column 3
   *
   * @param m23 new value
   * @return this
   */
  T_Matrix4 setM23(T_Number m23);

  /** @return value of row 3, column 0 */
  T_Number getM30();

  /**
   * Sets the value of row 3, column 0
   *
   * @param m30 new value
   * @return this
   */
  T_Matrix4 setM30(T_Number m30);

  /** @return value of row 3, column 1 */
  T_Number getM31();

  /**
   * Sets the value of row 3, column 1
   *
   * @param m31 new value
   * @return this
   */
  T_Matrix4 setM31(T_Number m31);

  /** @return value of row 3, column 2 */
  T_Number getM32();

  /**
   * Sets the value of row 3, column 2
   *
   * @param m32 new value
   * @return this
   */
  T_Matrix4 setM32(T_Number m32);

  /** @return value of row 3, column 3 */
  T_Number getM33();

  /**
   * Sets the value of row 3, column 3
   *
   * @param m33 new value
   * @return this
   */
  T_Matrix4 setM33(T_Number m33);

  /**
   * Sets this matrix as an identity matrix
   *
   * @return this
   */
  T_Matrix4 setIdentity();

  /**
   * Sets all values of this matrix
   *
   * @param m00 value of row 0, column 0
   * @param m01 value of row 0, column 1
   * @param m02 value of row 0, column 2
   * @param m03 value of row 0, column 3
   * @param m10 value of row 1, column 0
   * @param m11 value of row 1, column 1
   * @param m12 value of row 1, column 2
   * @param m13 value of row 1, column 3
   * @param m20 value of row 2, column 0
   * @param m21 value of row 2, column 1
   * @param m22 value of row 2, column 2
   * @param m23 value of row 2, column 3
   * @param m30 value of row 3, column 0
   * @param m31 value of row 3, column 1
   * @param m32 value of row 3, column 2
   * @param m33 value of row 3, column 3
   * @return this
   */
  T_Matrix4 set(
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
      T_Number m33);

  T_Matrix4 set3x3(Matrix3x3<T_Number, ?> matrix3x3f);

  T_Matrix4 set3x3(Matrix3x3<T_Number, ?> matrix3x3f, T_Matrix4 target);
}
