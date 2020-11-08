package net.flintmc.render.shader;

/** A class that is able to update a shader uniform when needed. */
public interface ShaderUniformProvider {

  /**
   * Automatically updates a given uniform's value accordingly.
   *
   * @param uniform the uniform whose value to update
   */
  void apply(ShaderUniform uniform);
}
