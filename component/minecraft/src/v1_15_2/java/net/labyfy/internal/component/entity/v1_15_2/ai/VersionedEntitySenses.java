package net.labyfy.internal.component.entity.v1_15_2.ai;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.labyfy.component.entity.Entity;
import net.labyfy.component.entity.EntityMapper;
import net.labyfy.component.entity.ai.EntitySenses;
import net.labyfy.component.entity.mob.MobEntity;
import net.labyfy.component.inject.implement.Implement;

/**
 * 1.15.2 implementation of the {@link EntitySenses}.
 */
@Implement(value = EntitySenses.class, version = "1.15.2")
public class VersionedEntitySenses extends net.minecraft.entity.ai.EntitySenses implements EntitySenses {

  private final EntityMapper entityMapper;

  @AssistedInject
  private VersionedEntitySenses(EntityMapper entityMapper, @Assisted("mobEntity") MobEntity entity) {
    super((net.minecraft.entity.MobEntity) entityMapper.toMinecraftMobEntity(entity));
    this.entityMapper = entityMapper;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean canSeeEntity(Entity entity) {
    return this.canSee((net.minecraft.entity.Entity) this.entityMapper.toMinecraftEntity(entity));
  }
}
