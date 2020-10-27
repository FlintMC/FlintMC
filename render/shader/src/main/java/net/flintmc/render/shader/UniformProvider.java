package net.flintmc.render.shader;

import net.flintmc.framework.inject.primitive.InjectionHolder;
import net.flintmc.render.shader.uniformprovider.PartialTickProvider;
import net.flintmc.render.shader.uniformprovider.ProjectionMatrixProvider;
import net.flintmc.render.shader.uniformprovider.WorldMatrixProvider;

/** Lists automatic uniform value provider implemented in Labyfy. */
public enum UniformProvider implements ShaderUniformProvider {
  /** Updates a uniform with the current partial tick. */
  PARTIAL_TICKS(InjectionHolder.getInjectedInstance(PartialTickProvider.class)),
  /** Updates a uniform with the current projection matrix. */
  PROJECTION_MATRIX(InjectionHolder.getInjectedInstance(ProjectionMatrixProvider.class)),
  /** Updates a uniform with the current world matrix. */
  WORLD_MATRIX(InjectionHolder.getInjectedInstance(WorldMatrixProvider.class));

  private final ShaderUniformProvider provider;

  UniformProvider(ShaderUniformProvider provider) {
    this.provider = provider;
  }

  /** {@inheritDoc} */
  @Override
  public void apply(ShaderUniform uniform) {
    provider.apply(uniform);
  }
}
