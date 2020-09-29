package net.labyfy.component.render.shader;

import net.labyfy.component.inject.primitive.InjectionHolder;
import net.labyfy.component.render.shader.uniformprovider.PartialTickProvider;
import net.labyfy.component.render.shader.uniformprovider.ProjectionMatrixProvider;
import net.labyfy.component.render.shader.uniformprovider.WorldMatrixProvider;

public enum UniformProvider implements ShaderUniformProvider {
  PARTIAL_TICKS(InjectionHolder.getInjectedInstance(PartialTickProvider.class)),
  PROJECTION_MATRIX(InjectionHolder.getInjectedInstance(ProjectionMatrixProvider.class)),
  WORLD_MATRIX(InjectionHolder.getInjectedInstance(WorldMatrixProvider.class));

  private final ShaderUniformProvider provider;

  UniformProvider(ShaderUniformProvider provider) {
    this.provider = provider;
  }

  @Override
  public void apply(ShaderUniform uniform) {
    provider.apply(uniform);
  }
}
