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

package net.flintmc.util.math.rotation;

public interface Quaternion<
    T_Number extends Number, T_Quaternion extends Quaternion<T_Number, T_Quaternion>> {

  T_Number getX();

  T_Quaternion setX(T_Number x);

  T_Number getY();

  T_Quaternion setY(T_Number y);

  T_Number getZ();

  T_Quaternion setZ(T_Number z);

  T_Number getW();

  T_Quaternion setW(T_Number w);

  T_Quaternion rotateLocalX(T_Number ang);

  T_Quaternion rotateLocalX(T_Number ang, T_Quaternion target);

  T_Quaternion rotateLocalY(T_Number ang);

  T_Quaternion rotateLocalY(T_Number ang, T_Quaternion target);

  T_Quaternion rotateLocalZ(T_Number ang);

  T_Quaternion rotateLocalZ(T_Number ang, T_Quaternion target);

  T_Quaternion set(T_Number x, T_Number y, T_Number z, T_Number w);
}
