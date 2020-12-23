package net.flintmc.mcapi.render;

import java.util.UUID;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.util.math.matrix.Matrix3x3f;
import net.flintmc.util.math.matrix.Matrix4x4f;

public interface MinecraftRenderMeta {

  Matrix3x3f getNormal();

  Matrix4x4f getWorld();

  MinecraftRenderMeta setPackedLight(int packedLight);

  MinecraftRenderMeta setPartialTick(float partialTick);

  float getPartialTick();

  int getPackedLight();

  MinecraftRenderMeta push();

  MinecraftRenderMeta pop();

  UUID getTargetUUID();

  MinecraftRenderMeta rotateToPlayersCamera();

  /**
   * Rotate this matrix around a specified axis
   *
   * @param ang rotation in radians
   * @param x   x axis
   * @param y   y axis
   * @param z   z axis
   * @return this
   */
  MinecraftRenderMeta rotate(float ang, float x, float y, float z);

  /**
   * Scale this matrix linear with a factor
   *
   * @param factor the factor to scale this with
   * @return this
   */
  MinecraftRenderMeta scale(float factor);

  /**
   * Scale this matrix linear with a factor
   *
   * @param factorX the x factor to scale this with
   * @param factorY the y factor to scale this with
   * @param factorZ the z factor to scale this with
   * @return this
   */
  MinecraftRenderMeta scale(float factorX, float factorY, float factorZ);

  /**
   * Translates this matrix
   *
   * @param x the x factor to translate this with
   * @param y the y factor to translate this with
   * @param z the z factor to translate this with
   * @return this
   */
  MinecraftRenderMeta translate(float x, float y, float z);

  MinecraftRenderMeta setTargetUuid(UUID uuid);

  @AssistedFactory(MinecraftRenderMeta.class)
  interface Factory {
    MinecraftRenderMeta create();
  }

}
