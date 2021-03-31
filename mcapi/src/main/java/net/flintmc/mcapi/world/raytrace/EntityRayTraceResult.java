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
import net.flintmc.mcapi.world.math.Vector3D;

/**
 * {@link RayTraceResult} implementation for the type {@link Type#ENTITY}.
 */
public interface EntityRayTraceResult extends RayTraceResult {

  /**
   * Retrieves the entity that has been hit by the ray.
   *
   * @return The non-null hit entity
   */
  Entity getEntity();

  /**
   * Factory for the {@link EntityRayTraceResult}.
   */
  @AssistedFactory(EntityRayTraceResult.class)
  interface Factory {

    /**
     * Creates a new {@link EntityRayTraceResult} for the given entity.
     *
     * @param hitVector The non-null hit vector
     * @param entity    The non-null entity that has been hit
     * @return The new non-null {@link EntityRayTraceResult}
     */
    EntityRayTraceResult create(@Assisted Vector3D hitVector, @Assisted Entity entity);
  }
}
