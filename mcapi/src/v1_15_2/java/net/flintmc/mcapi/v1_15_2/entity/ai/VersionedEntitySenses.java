package net.flintmc.mcapi.v1_15_2.entity.ai;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.entity.Entity;
import net.flintmc.mcapi.entity.MobEntity;
import net.flintmc.mcapi.entity.ai.EntitySenses;
import net.flintmc.mcapi.entity.mapper.EntityFoundationMapper;

/** 1.15.2 implementation of the {@link EntitySenses}. */
@Implement(value = EntitySenses.class, version = "1.15.2")
public class VersionedEntitySenses extends net.minecraft.entity.ai.EntitySenses
    implements EntitySenses {

  private final EntityFoundationMapper entityFoundationMapper;

  @AssistedInject
  private VersionedEntitySenses(
      EntityFoundationMapper entityFoundationMapper, @Assisted("mobEntity") MobEntity entity) {
    super(
        (net.minecraft.entity.MobEntity)
            entityFoundationMapper.getEntityMapper().toMinecraftMobEntity(entity));
    this.entityFoundationMapper = entityFoundationMapper;
  }

  /** {@inheritDoc} */
  @Override
  public boolean canSeeEntity(Entity entity) {
    return this.canSee(
        (net.minecraft.entity.Entity)
            this.entityFoundationMapper.getEntityMapper().toMinecraftEntity(entity));
  }
}
