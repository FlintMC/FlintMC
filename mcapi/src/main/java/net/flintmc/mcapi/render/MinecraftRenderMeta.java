/*
 * FlintMC
 * Copyright (C) 2020-2021 LabyMedia GmbH and contributors
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package net.flintmc.mcapi.render;

import java.util.UUID;

import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.util.math.matrix.Matrix3x3f;
import net.flintmc.util.math.matrix.Matrix4x4f;

/**
 * Represents the current rendering state of Minecraft.
 * This contains the whole matrix context and render date like current sky light etc.
 */
public interface MinecraftRenderMeta {

  /**
   * Should not be modified without modifying {@link MinecraftRenderMeta#getWorld()} the same way.
   *
   * @return the 3x3 matrix that represents the normal transformations
   */
  Matrix3x3f getNormal();

  /**
   * Should not be modified without modifying {@link MinecraftRenderMeta#getNormal()} ()} the same way.
   *
   * @return the 4x4 matrix that represents the world vertex transformations
   */
  Matrix4x4f getWorld();

  /**
   * Sets the packed light value. Should in most cases only be called by flint internals.
   *
   * @param packedLight the packedLight value to set
   * @return this
   */
  MinecraftRenderMeta setPackedLight(int packedLight);

  /**
   * Sets the partialTick value. Should in most cases only be called by flint internals.
   *
   * @param partialTick the partialTick value to set
   * @return this
   */
  MinecraftRenderMeta setPartialTick(float partialTick);

  /**
   * @return the minecraft current partialTick value
   */
  float getPartialTick();

  /**
   * @return the minecraft packedLight value
   */
  int getPackedLight();

  /**
   * Pushes the current matrix stack. Compare to OpenGL pushMatrix or minecraft MatrixStack push.
   *
   * @return this
   */
  MinecraftRenderMeta push();

  /**
   * Pushes the current matrix stack. Compare to OpenGL popMatrix or minecraft MatrixStack pop
   *
   * @return this
   */
  MinecraftRenderMeta pop();

  /**
   * @return the uuid of the object to render
   */
  UUID getTargetUUID();

  /**
   * Rotates the current context to the players camera.
   *
   * @return this
   */
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

  /**
   * Sets the rendered objects uuid. Should only be called by Flint internals.
   *
   * @param uuid the uuid to set
   * @return this
   */
  MinecraftRenderMeta setTargetUuid(UUID uuid);

  /**
   * Factory for {@link MinecraftRenderMeta}
   */
  @AssistedFactory(MinecraftRenderMeta.class)
  interface Factory {

    /**
     * @return new instance of {@link MinecraftRenderMeta}
     */
    MinecraftRenderMeta create();
  }

}
