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

package net.flintmc.mcapi.v1_16_4.entity;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.entity.EntitySize;

/**
 * 1.16.4 implementation of the {@link EntitySize}.
 */
@Implement(value = EntitySize.class, version = "1.16.4")
public class VersionedEntitySize implements EntitySize {

  private float width;
  private float height;
  private boolean fixed;

  @AssistedInject
  private VersionedEntitySize(
      @Assisted("width") float width,
      @Assisted("height") float height,
      @Assisted("fixed") boolean fixed) {
    this.width = width;
    this.height = height;
    this.fixed = fixed;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public EntitySize scale(float factor) {
    return this.scale(factor, factor);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public EntitySize scale(float widthFactor, float heightFactor) {
    if (!this.fixed && (widthFactor != 1.0F || heightFactor != 1.0F)) {
      this.width *= widthFactor;
      this.height *= heightFactor;
      this.fixed = false;
    }

    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getWidth() {
    return this.width;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getHeight() {
    return this.height;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isFixed() {
    return this.fixed;
  }
}
