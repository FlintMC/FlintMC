package net.flintmc.util.math.matrix;

import net.flintmc.framework.inject.assisted.AssistedFactory;

import java.nio.FloatBuffer;

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
