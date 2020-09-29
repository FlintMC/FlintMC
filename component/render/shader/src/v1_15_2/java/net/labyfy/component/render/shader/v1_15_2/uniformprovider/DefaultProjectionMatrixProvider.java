package net.labyfy.component.render.shader.v1_15_2.uniformprovider;

import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.render.shader.ShaderUniform;
import net.labyfy.component.render.shader.uniformprovider.ProjectionMatrixProvider;

import javax.inject.Singleton;

@Singleton
@Implement(ProjectionMatrixProvider.class)
public class DefaultProjectionMatrixProvider implements ProjectionMatrixProvider {

  /** {@inheritDoc} */
  @Override
  public void apply(ShaderUniform uniform) {}
}
