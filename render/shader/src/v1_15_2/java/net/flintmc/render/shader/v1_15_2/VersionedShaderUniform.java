package net.flintmc.render.shader.v1_15_2;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.render.shader.ShaderProgram;
import net.flintmc.render.shader.ShaderUniform;
import net.flintmc.render.shader.ShaderUniformProvider;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL20.*;

/** {@inheritDoc} */
@Implement(ShaderUniform.class)
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

  /** {@inheritDoc} */
  @Override
  public int getLocation() {
    return this.location;
  }

  /** {@inheritDoc} */
  @Override
  public void set1f(float v0) {
    glUniform1f(this.location, v0);
  }

  /** {@inheritDoc} */
  @Override
  public void set2f(float v0, float v1) {
    glUniform2f(this.location, v0, v1);
  }

  /** {@inheritDoc} */
  @Override
  public void set3f(float v0, float v1, float v2) {
    glUniform3f(this.location, v0, v1, v2);
  }

  /** {@inheritDoc} */
  @Override
  public void set4f(float v0, float v1, float v2, float v3) {
    glUniform4f(this.location, v0, v1, v2, v3);
  }

  /** {@inheritDoc} */
  @Override
  public void set1i(int v0) {
    glUniform1i(this.location, v0);
  }

  /** {@inheritDoc} */
  @Override
  public void set2i(int v0, int v1) {
    glUniform2i(this.location, v0, v1);
  }

  /** {@inheritDoc} */
  @Override
  public void set3i(int v0, int v1, int v2) {
    glUniform3i(this.location, v0, v1, v2);
  }

  /** {@inheritDoc} */
  @Override
  public void set4i(int v0, int v1, int v2, int v3) {
    glUniform4i(this.location, v0, v1, v2, v3);
  }

  /** {@inheritDoc} */
  @Override
  public void set1fv(float[] value) {
    glUniform1fv(this.location, value);
  }

  /** {@inheritDoc} */
  @Override
  public void set1fv(FloatBuffer value) {
    glUniform1fv(this.location, value);
  }

  /** {@inheritDoc} */
  @Override
  public void set2fv(float[] value) {
    glUniform2fv(this.location, value);
  }

  /** {@inheritDoc} */
  @Override
  public void set2fv(FloatBuffer value) {
    glUniform2fv(this.location, value);
  }

  /** {@inheritDoc} */
  @Override
  public void set3fv(float[] value) {
    glUniform3fv(this.location, value);
  }

  /** {@inheritDoc} */
  @Override
  public void set3fv(FloatBuffer value) {
    glUniform3fv(this.location, value);
  }

  /** {@inheritDoc} */
  @Override
  public void set4fv(float[] value) {
    glUniform4fv(this.location, value);
  }

  /** {@inheritDoc} */
  @Override
  public void set4fv(FloatBuffer value) {
    glUniform4fv(this.location, value);
  }

  /** {@inheritDoc} */
  @Override
  public void setMatrix2fv(boolean transpose, float[] value) {
    glUniformMatrix2fv(this.location, transpose, value);
  }

  /** {@inheritDoc} */
  @Override
  public void setMatrix2fv(boolean transpose, FloatBuffer value) {
    glUniformMatrix2fv(this.location, transpose, value);
  }

  /** {@inheritDoc} */
  @Override
  public void setMatrix3fv(boolean transpose, float[] value) {
    glUniformMatrix3fv(this.location, transpose, value);
  }

  /** {@inheritDoc} */
  @Override
  public void setMatrix3fv(boolean transpose, FloatBuffer value) {
    glUniformMatrix3fv(this.location, transpose, value);
  }

  /** {@inheritDoc} */
  @Override
  public void setMatrix4fv(boolean transpose, float[] value) {
    glUniformMatrix4fv(this.location, transpose, value);
  }

  /** {@inheritDoc} */
  @Override
  public void setMatrix4fv(boolean transpose, FloatBuffer value) {
    glUniformMatrix4fv(this.location, transpose, value);
  }

  /** {@inheritDoc} */
  @Override
  public void updateFromValueProvider() {
    if (this.shaderUniformProvider != null) this.shaderUniformProvider.apply(this);
  }
}
