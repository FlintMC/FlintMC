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

package net.flintmc.mcapi.internal.world.raytrace;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.world.math.BlockPosition;
import net.flintmc.mcapi.world.math.Direction;
import net.flintmc.mcapi.world.math.Vector3D;
import net.flintmc.mcapi.world.raytrace.BlockRayTraceResult;

@Implement(BlockRayTraceResult.class)
public class DefaultBlockRayTraceResult implements BlockRayTraceResult {

  private final Vector3D hitVector;
  private final BlockPosition position;
  private final Direction face;

  @AssistedInject
  private DefaultBlockRayTraceResult(
      @Assisted Vector3D hitVector, @Assisted BlockPosition position, @Assisted Direction face) {
    this.hitVector = hitVector;
    this.position = position;
    this.face = face;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Vector3D getHitVector() {
    return this.hitVector;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Type getType() {
    return Type.BLOCK;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BlockPosition getPosition() {
    return this.position;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Direction getFace() {
    return this.face;
  }
}
