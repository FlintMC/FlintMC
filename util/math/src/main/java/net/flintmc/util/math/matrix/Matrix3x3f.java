package net.flintmc.util.math.matrix;

import net.flintmc.framework.inject.assisted.AssistedFactory;

/**
 * Float implementation of {@link Matrix3x3}
 */
public interface Matrix3x3f extends Matrix3x3<Float, Matrix3x3f> {

  /**
   * A factory class for {@link Matrix3x3f}.
   */
  @AssistedFactory(Matrix3x3f.class)
  interface Factory {
    /**
     * @return an identity matrix3x3
     */
    Matrix3x3f create();
  }

}
