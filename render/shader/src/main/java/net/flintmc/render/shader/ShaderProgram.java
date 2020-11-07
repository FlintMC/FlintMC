package net.flintmc.render.shader;

import net.flintmc.framework.inject.assisted.AssistedFactory;

import java.io.InputStream;

/** Represents an OpenGL shader program and its current state. */
public interface ShaderProgram {

  /**
   * Adds a vertex shader to the shader program.
   *
   * @param shader an {@link InputStream} via which the GLSL source can be read
   * @throws ShaderException an exception that occurred while loading or compiling the GLSL source
   */
  void addVertexShader(InputStream shader) throws ShaderException;

  /**
   * Adds a fragment shader to the shader program.
   *
   * @param shader an {@link InputStream} via which the GLSL source can be read
   * @throws ShaderException an exception that occurred while loading or compiling the GLSL source
   */
  void addFragmentShader(InputStream shader) throws ShaderException;

  /**
   * Adds a vertex shader to the shader program.
   *
   * @param shader the GLSL source
   * @throws ShaderException an exception that occurred while compiling the GLSL source
   */
  void addVertexShader(String shader) throws ShaderException;

  /**
   * Adds a fragment shader to the shader program.
   *
   * @param shader the GLSL source
   * @throws ShaderException an exception that occurred while compiling the GLSL source
   */
  void addFragmentShader(String shader) throws ShaderException;

  /**
   * Links the shader program. After this call, vertex/fragment shaders can't be added anymore.
   *
   * @throws ShaderException an exception that occurred while linking the shader program
   */
  void link() throws ShaderException;

  /** Enable this shader program and updates provided uniforms. */
  void useShader();

  /** Disable this shader program. No shader will be enabled after this call. */
  void stopShader();

  /** Updates provided uniforms. */
  void updateProvidedUniforms();

  /**
   * Adds a uniform that gets called when it's value should be recalculated.
   *
   * @param uniform the uniform to add
   */
  void addProvidedUniform(ShaderUniform uniform);

  /**
   * @return the OpenGL ID of this shader program. 0, if the program has not been linked
   *     successfully (yet).
   */
  int getProgramID();

  /**
   * @return the OpenGL ID of this shader program's vertex shader. 0, if no vertex shader was added.
   */
  int getVertexShaderID();

  /**
   * @return the OpenGL ID of this shader program's fragment shader. 0, if no fragment shader was
   *     added.
   */
  int getFragmentShaderID();

  /** @return true, if the has been linked successfully, false if not */
  boolean isLinked();

  @AssistedFactory(ShaderProgram.class)
  interface Factory {

    /** @return a new shader program */
    ShaderProgram create();
  }
}
