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

public interface Matrix3x3<T_Number extends Number, T_Matrix3 extends Matrix3x3<T_Number, T_Matrix3>> {

  /**
   * Rotate this matrix around a specified axis
   *
   * @param ang rotation in radians
   * @param x x axis
   * @param y y axis
   * @param z z axis
   * @return this
   */
  T_Matrix3 rotate(float ang, float x, float y, float z);

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
  T_Matrix3 rotate(float ang, float x, float y, float z, T_Matrix3 target);

  /**
   * Inverts this matrix
   *
   * @return this
   */
  T_Matrix3 invert();

  /**
   * Inverts this matrix and writes the output to a target
   *
   * @param target the target to write to
   * @return target
   */
  T_Matrix3 invert(T_Matrix3 target);

  /**
   * Transposes this matrix
   *
   * @return this
   */
  T_Matrix3 transpose();

  /**
   * Transposes this matrix and writes the output to a target
   *
   * @param target the target to write to
   * @return target
   */
  T_Matrix3 transpose(T_Matrix3 target);

  /**
   * Copies this matrix to a target
   *
   * @param target the target to write to
   * @return target
   */
  T_Matrix3 copy(T_Matrix3 target);

  /**
   * Multiplies this matrix with a second one
   *
   * @param right the right matrix to multiply this with
   * @return this
   */
  T_Matrix3 mul(T_Matrix3 right);

  /**
   * Multiplies this matrix with a second one
   *
   * @param right  the right matrix to multiply this with
   * @param target the target to write to
   * @return target
   */
  T_Matrix3 mul(T_Matrix3 right, T_Matrix3 target);

  /**
   * Scale this matrix linear with a factor
   *
   * @param factor the factor to scale this with
   * @return this
   */
  T_Matrix3 scale(T_Number factor);

  /**
   * Scale this matrix linear with a factor
   *
   * @param factor the factor to scale this with
   * @param target the target to write to
   * @return target
   */
  T_Matrix3 scale(T_Number factor, T_Matrix3 target);

  /**
   * Scale this matrix linear with a factor
   *
   * @param factorX the x factor to scale this with
   * @param factorY the y factor to scale this with
   * @param factorZ the z factor to scale this with
   * @return this
   */
  T_Matrix3 scale(T_Number factorX, T_Number factorY, T_Number factorZ);

  /**
   * Scale this matrix linear with a factor
   *
   * @param factorX the x factor to scale this with
   * @param factorY the y factor to scale this with
   * @param factorZ the z factor to scale this with
   * @param target  the target to write to
   * @return target
   */
  T_Matrix3 scale(T_Number factorX, T_Number factorY, T_Number factorZ, T_Matrix3 target);

  /**
   * @return value of row 0, column 0
   */
  T_Number getM00();

  /**
   * Sets the value of row 0, column 0
   *
   * @param m00 new value
   * @return this
   */
  T_Matrix3 setM00(T_Number m00);

  /**
   * @return value of row 0, column 1
   */
  T_Number getM01();

  /**
   * Sets the value of row 0, column 1
   *
   * @param m01 new value
   * @return this
   */
  T_Matrix3 setM01(T_Number m01);

  /**
   * @return value of row 0, column 2
   */
  T_Number getM02();

  /**
   * Sets the value of row 0, column 2
   *
   * @param m02 new value
   * @return this
   */
  T_Matrix3 setM02(T_Number m02);

  /**
   * @return value of row 1, column 0
   */
  T_Number getM10();

  /**
   * Sets the value of row 1, column 0
   *
   * @param m10 new value
   * @return this
   */
  T_Matrix3 setM10(T_Number m10);

  /**
   * @return value of row 1, column 1
   */
  T_Number getM11();

  /**
   * Sets the value of row 1, column 1
   *
   * @param m11 new value
   * @return this
   */
  T_Matrix3 setM11(T_Number m11);

  /**
   * @return value of row 1, column 2
   */
  T_Number getM12();

  /**
   * Sets the value of row 1, column 2
   *
   * @param m12 new value
   * @return this
   */
  T_Matrix3 setM12(T_Number m12);

  /**
   * @return value of row 2, column 0
   */
  T_Number getM20();

  /**
   * Sets the value of row 2, column 0
   *
   * @param m20 new value
   * @return this
   */
  T_Matrix3 setM20(T_Number m20);

  /**
   * @return value of row 2, column 1
   */
  T_Number getM21();

  /**
   * Sets the value of row 2, column 1
   *
   * @param m21 new value
   * @return this
   */
  T_Matrix3 setM21(T_Number m21);

  /**
   * @return value of row 2, column 2
   */
  T_Number getM22();

  /**
   * Sets the value of row 2, column 2
   *
   * @param m22 new value
   * @return this
   */
  T_Matrix3 setM22(T_Number m22);

  /**
   * Sets this matrix as an identity matrix
   *
   * @return this
   */
  T_Matrix3 setIdentity();

  /**
   * Sets all values of this matrix
   *
   * @param m00 value of row 0, column 0
   * @param m01 value of row 0, column 1
   * @param m02 value of row 0, column 2
   * @param m10 value of row 1, column 0
   * @param m11 value of row 1, column 1
   * @param m12 value of row 1, column 2
   * @param m20 value of row 2, column 0
   * @param m21 value of row 2, column 1
   * @param m22 value of row 2, column 2
   * @return this
   */
  T_Matrix3 set(
      T_Number m00,
      T_Number m01,
      T_Number m02,
      T_Number m10,
      T_Number m11,
      T_Number m12,
      T_Number m20,
      T_Number m21,
      T_Number m22);

  T_Matrix3 set(Matrix3x3<T_Number, ?> matrix3x3);
}
