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

package net.flintmc.mcapi.entity;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;

/**
 * Represents the size of an entity.
 */
public interface EntitySize {

  /**
   * Multiply an entity by the given factor.
   *
   * @param factor The factory to multiply.
   * @return The scaled entity size.
   */
  EntitySize scale(float factor);

  /**
   * Multiply an entity size by the given parameters.
   *
   * @param widthFactor  The width factor to multiply.
   * @param heightFactor The height factor to multiply.
   * @return The scaled entity size.
   */
  EntitySize scale(float widthFactor, float heightFactor);

  /**
   * Retrieves the entity width.
   *
   * @return The entity width.
   */
  float getWidth();

  /**
   * Retrieves the entity height.
   *
   * @return The entity height.
   */
  float getHeight();

  /**
   * Whether the entity size is fixed.
   *
   * @return {@code true} if the entity size is fixed, otherwise {@code false}.
   */
  boolean isFixed();

  /**
   * A factory class for {@link EntitySize}.
   */
  @AssistedFactory(EntitySize.class)
  interface Factory {

    /**
     * Creates a new {@link EntitySize} with the given parameters.
     *
     * @param width  The width of an entity.
     * @param height The height of an entity.
     * @param fixed  Whether the entity is fixed.
     * @return a created entity size.
     */
    EntitySize create(
        @Assisted("width") float width,
        @Assisted("height") float height,
        @Assisted("fixed") boolean fixed);
  }
}
