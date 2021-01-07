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

package net.flintmc.mcapi.internal.entity.event;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.entity.Entity;
import net.flintmc.mcapi.entity.event.EntityRenderNameEvent;

/**
 * {@inheritDoc}
 */
@Implement(EntityRenderNameEvent.class)
public class DefaultEntityRenderNameEvent implements EntityRenderNameEvent {

  private final Entity entity;
  private final String displayName;
  private final Object matrix;
  private final Object buffer;
  private final int textBackgroundColor;
  private final boolean notSneaking;
  private final int packedLight;
  private final int y;

  @AssistedInject
  public DefaultEntityRenderNameEvent(
      @Assisted Entity entity,
      @Assisted String displayName,
      @Assisted("matrix") Object matrix,
      @Assisted("buffer") Object buffer,
      @Assisted boolean notSneaking,
      @Assisted("textBackgroundColor") int textBackgroundColor,
      @Assisted("packedLight") int packedLight,
      @Assisted("y") int y) {
    this.entity = entity;
    this.displayName = displayName;
    this.matrix = matrix;
    this.buffer = buffer;
    this.textBackgroundColor = textBackgroundColor;
    this.notSneaking = notSneaking;
    this.packedLight = packedLight;
    this.y = y;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Entity getEntity() {
    return this.entity;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getDisplayName() {
    return this.displayName;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object getMatrix() {
    return this.matrix;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object getBuffer() {
    return this.buffer;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getTextBackgroundColor() {
    return this.textBackgroundColor;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isNotSneaking() {
    return this.notSneaking;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getPackedLight() {
    return this.packedLight;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getY() {
    return this.y;
  }
}
