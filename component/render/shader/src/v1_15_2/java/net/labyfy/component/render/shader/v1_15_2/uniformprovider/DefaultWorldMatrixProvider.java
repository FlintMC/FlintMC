package net.labyfy.component.render.shader.v1_15_2.uniformprovider;

import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.render.shader.ShaderUniform;
import net.labyfy.component.render.shader.uniformprovider.WorldMatrixProvider;

import javax.inject.Singleton;

@Singleton
@Implement(WorldMatrixProvider.class)
public class DefaultWorldMatrixProvider implements WorldMatrixProvider {
  @Override
  public void apply(ShaderUniform uniform) {}
}
