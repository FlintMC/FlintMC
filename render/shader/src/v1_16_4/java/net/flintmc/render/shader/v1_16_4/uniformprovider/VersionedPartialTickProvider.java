package net.flintmc.render.shader.v1_16_4.uniformprovider;

import com.google.inject.Singleton;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.render.shader.ShaderUniform;
import net.flintmc.render.shader.uniformprovider.PartialTickProvider;
import net.minecraft.client.Minecraft;

@Singleton
@Implement(PartialTickProvider.class)
public class VersionedPartialTickProvider implements PartialTickProvider {

  /** {@inheritDoc} */
  @Override
  public void apply(ShaderUniform uniform) {
    uniform.set1f(Minecraft.getInstance().getRenderPartialTicks());
  }
}
