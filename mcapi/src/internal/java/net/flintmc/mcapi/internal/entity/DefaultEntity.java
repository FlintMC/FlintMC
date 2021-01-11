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

package net.flintmc.mcapi.internal.entity;

import java.util.Map;
import net.flintmc.mcapi.entity.Entity;
import net.flintmc.mcapi.entity.mapper.EntityFoundationMapper;
import net.flintmc.mcapi.entity.render.EntityRenderContext;
import net.flintmc.mcapi.entity.type.EntityType;
import net.flintmc.mcapi.world.World;
import net.flintmc.mcapi.world.scoreboad.team.Team;
import net.flintmc.render.model.ModelBoxHolder;

public abstract class DefaultEntity<H> implements Entity {

  private final H handle;
  private final EntityType entityType;
  private final World world;
  private final EntityFoundationMapper entityFoundationMapper;
  private final EntityRenderContext.Factory entityRenderContextFactory;
  protected EntityRenderContext entityRenderContext;

  protected DefaultEntity(
      H handle,
      EntityType entityType,
      World world,
      EntityFoundationMapper entityFoundationMapper,
      EntityRenderContext.Factory entityRenderContextFactory) {
    this.handle = handle;
    this.entityType = entityType;
    this.world = world;
    this.entityFoundationMapper = entityFoundationMapper;
    this.entityRenderContextFactory = entityRenderContextFactory;
  }

  protected abstract Map<String, ModelBoxHolder<Entity, EntityRenderContext>>
  createModelRenderers();

  /**
   * {@inheritDoc}
   */
  @Override
  public EntityRenderContext getRenderContext() {
    return this.entityRenderContext;
  }

  protected EntityRenderContext createRenderContext() {
    return this.entityRenderContextFactory.create(this);
  }

  public H getHandle() {
    return handle;
  }

  /** {@inheritDoc} */
  @Override
  public EntityType getType() {
    return this.entityType;
  }

  /** {@inheritDoc} */
  @Override
  public World getWorld() {
    return this.world;
  }

  /** {@inheritDoc} */
  @Override
  public boolean isInSameTeam(Entity entity) {
    return this.isInScoreboardTeam(entity.getTeam());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isInScoreboardTeam(Team team) {
    return this.getTeam() != null && this.getTeam().isSameTeam(team);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public EntityFoundationMapper getEntityFoundationMapper() {
    return this.entityFoundationMapper;
  }
}
