package net.flintmc.util.math.matrix;

import net.flintmc.framework.inject.assisted.AssistedFactory;

public interface Matrix3x3f extends Matrix3x3<Float, Matrix3x3f> {

  @AssistedFactory(Matrix3x3f.class)
  interface Factory {
    Matrix3x3f create();
  }

}
