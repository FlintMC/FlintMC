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

import java.nio.FloatBuffer;
import net.flintmc.framework.inject.assisted.AssistedFactory;

/**
 * Float implementation of {@link Matrix4x4}
 */
public interface Matrix4x4f extends Matrix4x4<Float, Matrix4x4f> {

  /**
   * Write this matrix to a floatBuffer. Buffer index 0 will be row 0, column 0. Buffer index 1 will
   * be row 0, column 1. Buffer index 2 will be row 0, column 2. ...
   *
   * @param floatBuffer the floatBuffer to write to
   * @return this
   */
  Matrix4x4f write(FloatBuffer floatBuffer);

  /**
   * A factory class for {@link Matrix4x4f}.
   */
  @AssistedFactory(Matrix4x4f.class)
  interface Factory {

    /**
     * @return an identity matrix4x4
     */
    Matrix4x4f create();
  }
}
