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
import net.flintmc.mcapi.entity.Entity;
import net.flintmc.mcapi.world.block.Block;
import net.flintmc.mcapi.world.math.Vector3D;

/**
 * Represents the result of a ray trace in a world.
 */
public interface RayTraceResult {

  /**
   * Retrieves the hit vector of the result of this ray trace.
   *
   * @return The hit vector of this result or {@code null} if the {@link #getType() type} is {@link
   * Type#MISS}
   */
  Vector3D getHitVector();

  /**
   * Retrieves the type of the result of this ray trace.
   *
   * @return The non-null type
   */
  Type getType();

  /**
   * Available types for the {@link RayTraceResult}.
   */
  enum Type {

    /**
     * The ray was interrupted by an {@link Entity}.
     */
    ENTITY,

    /**
     * The ray was interrupted by a {@link Block}.
     */
    BLOCK,

    /**
     * The ray hit nothing.
     */
    MISS
  }

  /**
   * Factory for the {@link RayTraceResult}.
   */
  @AssistedFactory(RayTraceResult.class)
  interface Factory {

    /**
     * Creates a new {@link RayTraceResult}  with the given type and hit vector.
     *
     * @param type      The non-null type
     * @param hitVector The non-null hit vector
     * @return The new non-null {@link RayTraceResult}
     */
    RayTraceResult create(@Assisted Type type, @Assisted Vector3D hitVector);

    /**
     * Creates a new {@link RayTraceResult}  with the type {@link Type#MISS}.
     *
     * @return The new non-null {@link RayTraceResult}
     */
    RayTraceResult createMiss();
  }

}
