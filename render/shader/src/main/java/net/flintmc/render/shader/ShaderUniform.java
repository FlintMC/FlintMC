package net.flintmc.render.shader;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;

import java.nio.FloatBuffer;

/** Represents a shader uniform and provides proxy methods to the respective OpenGL calls. */
public interface ShaderUniform {

  /** @return the uniform location according to OpenGL */
  int getLocation();

  /** Calls the corresponding OpenGL function. */
  void set1f(float v0);

  /** Calls the corresponding OpenGL function. */
  void set2f(float v0, float v1);

  /** Calls the corresponding OpenGL function. */
  void set3f(float v0, float v1, float v2);

  /** Calls the corresponding OpenGL function. */
  void set4f(float v0, float v1, float v2, float v3);

  /** Calls the corresponding OpenGL function. */
  void set1i(int v0);

  /** Calls the corresponding OpenGL function. */
  void set2i(int v0, int v1);

  /** Calls the corresponding OpenGL function. */
  void set3i(int v0, int v1, int v2);

  /** Calls the corresponding OpenGL function. */
  void set4i(int v0, int v1, int v2, int v3);

  /** Calls the corresponding OpenGL function. */
  void set1fv(float[] value);

  /** Calls the corresponding OpenGL function. */
  void set1fv(FloatBuffer value);

  /** Calls the corresponding OpenGL function. */
  void set2fv(float[] value);

  /** Calls the corresponding OpenGL function. */
  void set2fv(FloatBuffer value);

  /** Calls the corresponding OpenGL function. */
  void set3fv(float[] value);

  /** Calls the corresponding OpenGL function. */
  void set3fv(FloatBuffer value);

  /** Calls the corresponding OpenGL function. */
  void set4fv(float[] value);

  /** Calls the corresponding OpenGL function. */
  void set4fv(FloatBuffer value);

  /** Calls the corresponding OpenGL function. */
  void setMatrix2fv(boolean transpose, float[] value);

  /** Calls the corresponding OpenGL function. */
  void setMatrix2fv(boolean transpose, FloatBuffer value);

  /** Calls the corresponding OpenGL function. */
  void setMatrix3fv(boolean transpose, float[] value);

  /** Calls the corresponding OpenGL function. */
  void setMatrix3fv(boolean transpose, FloatBuffer value);

  /** Calls the corresponding OpenGL function. */
  void setMatrix4fv(boolean transpose, float[] value);

  /** Calls the corresponding OpenGL function. */
  void setMatrix4fv(boolean transpose, FloatBuffer value);

  /** Calls the uniform value provider to update own value. */
  void updateFromValueProvider();

  @AssistedFactory(ShaderUniform.class)
  interface Factory {

    /**
     * Creates a new shader uniform.
     *
     * @param name the name of this uniform as used in the GLSL source
     * @param shaderProgram the shader program this uniform refers to
     * @return a new uniform
     */
    ShaderUniform create(@Assisted String name, @Assisted ShaderProgram shaderProgram);

    /**
     * Creates a new shader uniform.
     *
     * @param name the name of this uniform as used in the GLSL source
     * @param shaderProgram the shader program this uniform refers to
     * @param valueProvider the value provider that should be used to automatically update the new
     *     uniform's value
     * @return a new uniform that gets updated automatically
     */
    ShaderUniform create(
            @Assisted String name, @Assisted ShaderProgram shaderProgram, @Assisted ShaderUniformProvider valueProvider);
  }
}
