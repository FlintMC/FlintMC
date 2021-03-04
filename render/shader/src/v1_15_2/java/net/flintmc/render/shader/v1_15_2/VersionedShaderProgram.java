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

package net.flintmc.render.shader.v1_15_2;

import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_LINK_STATUS;
import static org.lwjgl.opengl.GL20.GL_TRUE;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glDeleteShader;
import static org.lwjgl.opengl.GL20.glGetProgrami;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glGetShaderi;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glUseProgram;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.render.shader.ShaderException;
import net.flintmc.render.shader.ShaderProgram;
import net.flintmc.render.shader.ShaderUniform;
import org.apache.commons.io.IOUtils;

/**
 * {@inheritDoc}
 */
@Implement(value = ShaderProgram.class)
public class VersionedShaderProgram implements ShaderProgram {

  private final List<ShaderUniform> providedShaderUniforms;
  private int shaderProgram;
  private int vertexShader;
  private int fragmentShader;
  private boolean linked;

  @AssistedInject
  private VersionedShaderProgram() {
    this.providedShaderUniforms = new ArrayList<>();
    this.shaderProgram = 0;
    this.vertexShader = 0;
    this.fragmentShader = 0;
    this.linked = true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addVertexShader(InputStream shader) throws ShaderException {
    try {
      addVertexShader(IOUtils.toString(shader, StandardCharsets.UTF_8));
    } catch (IOException e) {
      throw new ShaderException("Couldn't load shader source.", e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addFragmentShader(InputStream shader) throws ShaderException {
    try {
      addFragmentShader(IOUtils.toString(shader, StandardCharsets.UTF_8));
    } catch (IOException e) {
      throw new ShaderException("Couldn't load shader source.", e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addVertexShader(String shader) throws ShaderException {
    if (this.vertexShader != 0) {
      throw new ShaderException("Only one vertex shader can be added to a shader program.");
    }
    this.vertexShader = addShader(shader, GL_VERTEX_SHADER);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addFragmentShader(String shader) throws ShaderException {
    if (this.fragmentShader != 0) {
      throw new ShaderException("Only one fragment shader can be added to a shader program.");
    }
    this.fragmentShader = addShader(shader, GL_FRAGMENT_SHADER);
  }

  private int addShader(String shaderSource, int type) throws ShaderException {
    int shaderId = glCreateShader(type);
    glShaderSource(shaderId, shaderSource);
    glCompileShader(shaderId);

    if (glGetShaderi(shaderId, GL_COMPILE_STATUS) != GL_TRUE) {
      throw new ShaderException(glGetShaderInfoLog(shaderId));
    }

    return shaderId;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void link() throws ShaderException {
    this.shaderProgram = glCreateProgram();

    if (this.vertexShader != 0) {
      glAttachShader(this.shaderProgram, this.vertexShader);
    }
    if (this.fragmentShader != 0) {
      glAttachShader(this.shaderProgram, this.fragmentShader);
    }

    glLinkProgram(this.shaderProgram);

    if (glGetProgrami(this.shaderProgram, GL_LINK_STATUS) != GL_TRUE) {
      throw new ShaderException(glGetShaderInfoLog(this.vertexShader));
    }

    this.linked = true;

    if (this.vertexShader != 0) {
      glDeleteShader(this.vertexShader);
    }
    if (this.fragmentShader != 0) {
      glDeleteShader(this.fragmentShader);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void useShader() {
    if (this.shaderProgram <= 0) {
      throw new IllegalStateException("Shader program has not been successfully linked yet.");
    }
    glUseProgram(shaderProgram);
    this.updateProvidedUniforms();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void stopShader() {
    glUseProgram(0);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void updateProvidedUniforms() {
    providedShaderUniforms.forEach(ShaderUniform::updateFromValueProvider);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addProvidedUniform(ShaderUniform uniform) {
    this.providedShaderUniforms.add(uniform);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getProgramID() {
    return this.shaderProgram;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getVertexShaderID() {
    return this.vertexShader;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getFragmentShaderID() {
    return this.fragmentShader;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isLinked() {
    return this.linked;
  }
}
