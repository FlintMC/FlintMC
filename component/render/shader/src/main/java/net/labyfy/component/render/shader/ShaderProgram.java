package net.labyfy.component.render.shader;

import net.labyfy.component.inject.assisted.AssistedFactory;

import java.io.InputStream;

public interface ShaderProgram {

  void addVertexShader(InputStream shader) throws ShaderException;

  void addFragmentShader(InputStream shader) throws ShaderException;

  void addVertexShader(String shader) throws ShaderException;

  void addFragmentShader(String shader) throws ShaderException;

  void link() throws ShaderException;

  void useShader();

  void stopShader();

  void updateProvidedUniforms();

  void addProvidedUniform(ShaderUniform uniform);

  int getProgramID();

  int getVertexShaderID();

  int getFragmentShaderID();

  boolean isLinked();

  @AssistedFactory(ShaderProgram.class)
  interface Factory {

    ShaderProgram create();
  }
}
