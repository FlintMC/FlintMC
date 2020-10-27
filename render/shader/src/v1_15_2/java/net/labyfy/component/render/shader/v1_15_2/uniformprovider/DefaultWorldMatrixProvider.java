package net.labyfy.component.render.shader.v1_15_2.uniformprovider;

import com.google.inject.Singleton;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.render.shader.ShaderUniform;
import net.labyfy.component.render.shader.uniformprovider.WorldMatrixProvider;

@Singleton
@Implement(WorldMatrixProvider.class)
public class DefaultWorldMatrixProvider implements WorldMatrixProvider {

  /** {@inheritDoc} */
  @Override
  public void apply(ShaderUniform uniform) {}
}
