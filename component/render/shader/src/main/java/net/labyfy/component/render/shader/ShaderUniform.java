package net.labyfy.component.render.shader;

import net.labyfy.component.inject.assisted.AssistedFactory;

import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;

public interface ShaderUniform {

  int getLocation();

  void set1f(float v0);

  void set2f(float v0, float v1);

  void set3f(float v0, float v1, float v2);

  void set4f(float v0, float v1, float v2, float v3);

  void set1i(int v0);

  void set2i(int v0, int v1);

  void set3i(int v0, int v1, int v2);

  void set4i(int v0, int v1, int v2, int v3);

  void set1fv(float[] value);

  void set1fv(FloatBuffer value);

  void set2fv(float[] value);

  void set2fv(FloatBuffer value);

  void set3fv(float[] value);

  void set3fv(FloatBuffer value);

  void set4fv(float[] value);

  void set4fv(FloatBuffer value);

  void setMatrix2fv(boolean transpose, float[] value);

  void setMatrix2fv(boolean transpose, FloatBuffer value);

  void setMatrix3fv(boolean transpose, float[] value);

  void setMatrix3fv(boolean transpose, FloatBuffer value);

  void setMatrix4fv(boolean transpose, float[] value);

  void setMatrix4fv(boolean transpose, FloatBuffer value);

  void updateFromValueProvider();

  @AssistedFactory(ShaderUniform.class)
  interface Factory {

    ShaderUniform create(String name, ShaderProgram shaderProgram);

    ShaderUniform create(
        String name, ShaderProgram shaderProgram, ShaderUniformProvider valueProvider);
  }
}
