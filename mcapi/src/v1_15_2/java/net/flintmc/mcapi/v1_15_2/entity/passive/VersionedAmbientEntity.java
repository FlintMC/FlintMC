package net.flintmc.mcapi.v1_15_2.entity.passive;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.entity.ai.EntitySenses;
import net.flintmc.mcapi.entity.mapper.EntityFoundationMapper;
import net.flintmc.mcapi.entity.passive.AmbientEntity;
import net.flintmc.mcapi.entity.render.EntityRenderContext;
import net.flintmc.mcapi.entity.type.EntityType;
import net.flintmc.mcapi.player.PlayerEntity;
import net.flintmc.mcapi.v1_15_2.entity.VersionedMobEntity;
import net.flintmc.mcapi.world.World;
import net.flintmc.render.model.ModelBox;

@Implement(value = AmbientEntity.class, version = "1.15.2")
public class VersionedAmbientEntity extends VersionedMobEntity implements AmbientEntity {

  @AssistedInject
  public VersionedAmbientEntity(
      @Assisted("entity") Object entity,
      @Assisted("entityType") EntityType entityType,
      World world,
      EntityFoundationMapper entityFoundationMapper,
      EntitySenses.Factory entitySensesFactory,
      EntityRenderContext.Factory entityRenderContextFactory) {
    super(entity, entityType, world, entityFoundationMapper, entitySensesFactory, entityRenderContextFactory);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean canBeLeashedTo(PlayerEntity playerEntity) {
    return false;
  }
}
