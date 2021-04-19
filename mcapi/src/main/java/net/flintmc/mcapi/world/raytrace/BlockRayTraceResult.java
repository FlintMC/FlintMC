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

package net.flintmc.mcapi.world.raytrace;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.world.math.BlockPosition;
import net.flintmc.mcapi.world.math.Direction;
import net.flintmc.mcapi.world.math.Vector3D;

/**
 * {@link RayTraceResult} implementation for the type {@link Type#BLOCK}.
 */
public interface BlockRayTraceResult extends RayTraceResult {

  /**
   * Retrieves the position of the block that has been hit by the ray.
   *
   * @return The non-null position of the hit block
   */
  BlockPosition getPosition();

  /**
   * Retrieves the face of the block that has been hit by the ray.
   *
   * @return The non-null face of the hit block
   */
  Direction getFace();

  /**
   * Factory for the {@link BlockRayTraceResult}.
   */
  @AssistedFactory(BlockRayTraceResult.class)
  interface Factory {

    /**
     * Creates a new {@link BlockRayTraceResult} for the given block position.
     *
     * @param hitVector The non-null hit vector
     * @param position  The non-null position of the block that has been hit
     * @param face      The non-null face that has been hit of the block
     * @return The new non-null {@link BlockRayTraceResult}
     */
    BlockRayTraceResult create(
        @Assisted Vector3D hitVector, @Assisted BlockPosition position, @Assisted Direction face);
  }
}
