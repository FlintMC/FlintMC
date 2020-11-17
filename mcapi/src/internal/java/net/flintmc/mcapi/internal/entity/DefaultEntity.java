package net.flintmc.mcapi.internal.entity;

import net.flintmc.mcapi.entity.Entity;
import net.flintmc.mcapi.entity.mapper.EntityFoundationMapper;
import net.flintmc.mcapi.entity.render.EntityRenderContext;
import net.flintmc.mcapi.entity.type.EntityType;
import net.flintmc.mcapi.world.World;
import net.flintmc.mcapi.world.scoreboad.team.Team;
import net.flintmc.render.model.ModelBoxHolder;

import java.util.Map;

public abstract class DefaultEntity<H> implements Entity {

  private final H handle;
  private final EntityType entityType;
  private final World world;
  private final EntityFoundationMapper entityFoundationMapper;
  private EntityRenderContext.Factory entityRenderContextFactory;
  protected EntityRenderContext entityRenderContext;

  protected DefaultEntity(H handle, EntityType entityType, World world, EntityFoundationMapper entityFoundationMapper, EntityRenderContext.Factory entityRenderContextFactory) {
    this.handle = handle;
    this.entityType = entityType;
    this.world = world;
    this.entityFoundationMapper = entityFoundationMapper;
    this.entityRenderContextFactory = entityRenderContextFactory;
  }

  protected abstract Map<String, ModelBoxHolder<Entity, EntityRenderContext>> createModelRenderers();

  public EntityRenderContext getRenderContext() {
    return this.entityRenderContext;
  }

  protected EntityRenderContext createRenderContext() {
    return this.entityRenderContextFactory.create(this);
  }

  public H getHandle() {
    return handle;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public EntityType getType() {
    return this.entityType;
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public World getWorld() {
    return this.world;
  }

  /**
   * {@inheritDoc}
   */
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

  public EntityFoundationMapper getEntityFoundationMapper() {
    return this.entityFoundationMapper;
  }
}
