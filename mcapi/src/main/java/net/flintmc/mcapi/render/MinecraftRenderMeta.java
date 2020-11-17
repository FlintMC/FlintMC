package net.flintmc.mcapi.render;

import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.util.math.matrix.Matrix3x3f;
import net.flintmc.util.math.matrix.Matrix4x4f;

public interface MinecraftRenderMeta {

  Matrix3x3f getNormal();

  Matrix4x4f getWorld();

  @AssistedFactory(MinecraftRenderMeta.class)
  interface Factory {
    MinecraftRenderMeta create();
  }

}
