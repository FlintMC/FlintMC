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

package net.flintmc.render.shader.v1_16_5;

import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniform1f;
import static org.lwjgl.opengl.GL20.glUniform1fv;
import static org.lwjgl.opengl.GL20.glUniform1i;
import static org.lwjgl.opengl.GL20.glUniform2f;
import static org.lwjgl.opengl.GL20.glUniform2fv;
import static org.lwjgl.opengl.GL20.glUniform2i;
import static org.lwjgl.opengl.GL20.glUniform3f;
import static org.lwjgl.opengl.GL20.glUniform3fv;
import static org.lwjgl.opengl.GL20.glUniform3i;
import static org.lwjgl.opengl.GL20.glUniform4f;
import static org.lwjgl.opengl.GL20.glUniform4fv;
import static org.lwjgl.opengl.GL20.glUniform4i;
import static org.lwjgl.opengl.GL20.glUniformMatrix2fv;
import static org.lwjgl.opengl.GL20.glUniformMatrix3fv;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;

import java.nio.FloatBuffer;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.render.shader.ShaderProgram;
import net.flintmc.render.shader.ShaderUniform;
import net.flintmc.render.shader.ShaderUniformProvider;

/**
 * {@inheritDoc}
 */
@Implement(value = ShaderUniform.class, version = "1.16.5")
public class VersionedShaderUniform implements ShaderUniform {

  private final String name;
  private final ShaderProgram shaderProgram;
  private final ShaderUniformProvider shaderUniformProvider;

  private int location;

  @AssistedInject
  private VersionedShaderUniform(@Assisted String name, @Assisted ShaderProgram shaderProgram) {
    this.name = name;
    this.shaderProgram = shaderProgram;
    this.shaderUniformProvider = null;
    this.setup();
  }

  @AssistedInject
  private VersionedShaderUniform(
      @Assisted String name,
      @Assisted ShaderProgram shaderProgram,
      @Assisted ShaderUniformProvider uniformProvider) {
    this.name = name;
    this.shaderProgram = shaderProgram;
    this.shaderUniformProvider = uniformProvider;
    this.setup();
  }

  private void setup() {
    this.location = glGetUniformLocation(this.shaderProgram.getProgramID(), this.name);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getLocation() {
    return this.location;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void set1f(float v0) {
    glUniform1f(this.location, v0);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void set2f(float v0, float v1) {
    glUniform2f(this.location, v0, v1);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void set3f(float v0, float v1, float v2) {
    glUniform3f(this.location, v0, v1, v2);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void set4f(float v0, float v1, float v2, float v3) {
    glUniform4f(this.location, v0, v1, v2, v3);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void set1i(int v0) {
    glUniform1i(this.location, v0);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void set2i(int v0, int v1) {
    glUniform2i(this.location, v0, v1);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void set3i(int v0, int v1, int v2) {
    glUniform3i(this.location, v0, v1, v2);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void set4i(int v0, int v1, int v2, int v3) {
    glUniform4i(this.location, v0, v1, v2, v3);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void set1fv(float[] value) {
    glUniform1fv(this.location, value);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void set1fv(FloatBuffer value) {
    glUniform1fv(this.location, value);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void set2fv(float[] value) {
    glUniform2fv(this.location, value);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void set2fv(FloatBuffer value) {
    glUniform2fv(this.location, value);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void set3fv(float[] value) {
    glUniform3fv(this.location, value);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void set3fv(FloatBuffer value) {
    glUniform3fv(this.location, value);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void set4fv(float[] value) {
    glUniform4fv(this.location, value);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void set4fv(FloatBuffer value) {
    glUniform4fv(this.location, value);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setMatrix2fv(boolean transpose, float[] value) {
    glUniformMatrix2fv(this.location, transpose, value);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setMatrix2fv(boolean transpose, FloatBuffer value) {
    glUniformMatrix2fv(this.location, transpose, value);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setMatrix3fv(boolean transpose, float[] value) {
    glUniformMatrix3fv(this.location, transpose, value);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setMatrix3fv(boolean transpose, FloatBuffer value) {
    glUniformMatrix3fv(this.location, transpose, value);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setMatrix4fv(boolean transpose, float[] value) {
    glUniformMatrix4fv(this.location, transpose, value);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setMatrix4fv(boolean transpose, FloatBuffer value) {
    glUniformMatrix4fv(this.location, transpose, value);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void updateFromValueProvider() {
    if (this.shaderUniformProvider != null) {
      this.shaderUniformProvider.apply(this);
    }
  }
}
