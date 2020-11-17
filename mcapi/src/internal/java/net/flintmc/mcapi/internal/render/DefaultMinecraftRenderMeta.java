package net.flintmc.mcapi.internal.render;

import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.render.MinecraftRenderMeta;
import net.flintmc.util.math.matrix.Matrix3x3f;
import net.flintmc.util.math.matrix.Matrix4x4f;

@Implement(MinecraftRenderMeta.class)
public class DefaultMinecraftRenderMeta implements MinecraftRenderMeta {

  private final Matrix3x3f normal;
  private final Matrix4x4f world;

  @AssistedInject
  private DefaultMinecraftRenderMeta(
      Matrix3x3f.Factory matrix3x3Factory, Matrix4x4f.Factory matrix4x4Factory) {
    this.normal = matrix3x3Factory.create();
    this.world = matrix4x4Factory.create();
  }

  public Matrix3x3f getNormal() {
    return this.normal;
  }

  public Matrix4x4f getWorld() {
    return this.world;
  }
}
