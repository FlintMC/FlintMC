package net.flintmc.render.shader.v1_16_4.uniformprovider;

import com.google.inject.Singleton;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.render.shader.ShaderUniform;
import net.flintmc.render.shader.uniformprovider.ProjectionMatrixProvider;

@Singleton
@Implement(value = ProjectionMatrixProvider.class, version = "1.16.4")
public class VersionedProjectionMatrixProvider implements ProjectionMatrixProvider {

  /** {@inheritDoc} */
  @Override
  public void apply(ShaderUniform uniform) {}
}
