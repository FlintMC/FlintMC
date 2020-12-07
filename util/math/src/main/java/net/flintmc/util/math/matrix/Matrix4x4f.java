package net.flintmc.util.math.matrix;

import net.flintmc.framework.inject.assisted.AssistedFactory;

import java.nio.FloatBuffer;

public interface Matrix4x4f extends Matrix4x4<Float, Matrix4x4f> {

  Matrix4x4f write(FloatBuffer floatBuffer);

  @AssistedFactory(Matrix4x4f.class)
  interface Factory {
    Matrix4x4f create();
  }

}
