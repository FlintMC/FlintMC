package net.flintmc.util.math.matrix;

import net.flintmc.framework.inject.assisted.AssistedFactory;

public interface Matrix4x4f extends Matrix4x4<Float, Matrix4x4f> {

  @AssistedFactory(Matrix4x4f.class)
  interface Factory {
    Matrix4x4f create();
  }

}
