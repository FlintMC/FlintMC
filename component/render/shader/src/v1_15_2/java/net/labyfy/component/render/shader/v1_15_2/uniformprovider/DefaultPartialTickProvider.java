package net.labyfy.component.render.shader.v1_15_2.uniformprovider;

import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.render.shader.ShaderUniform;
import net.labyfy.component.render.shader.uniformprovider.PartialTickProvider;
import net.minecraft.client.Minecraft;

import javax.inject.Singleton;

@Singleton
@Implement(PartialTickProvider.class)
public class DefaultPartialTickProvider implements PartialTickProvider {

  /** {@inheritDoc} */
  @Override
  public void apply(ShaderUniform uniform) {
    uniform.set1f(Minecraft.getInstance().getRenderPartialTicks());
  }
}
