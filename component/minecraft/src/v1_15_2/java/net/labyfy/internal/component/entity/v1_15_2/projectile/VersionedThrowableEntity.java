package net.labyfy.internal.component.entity.v1_15_2.projectile;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.labyfy.component.entity.Entity;
import net.labyfy.component.entity.mapper.EntityBaseMapper;
import net.labyfy.component.entity.LivingEntity;
import net.labyfy.component.entity.projectile.ThrowableEntity;
import net.labyfy.component.entity.type.EntityType;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.world.World;
import net.labyfy.internal.component.entity.v1_15_2.VersionedEntity;

@Implement(value = ThrowableEntity.class, version = "1.15.2")
public class VersionedThrowableEntity extends VersionedEntity implements ThrowableEntity {

  private final net.minecraft.entity.projectile.ThrowableEntity throwableEntity;

  @AssistedInject
  public VersionedThrowableEntity(
          @Assisted("entity") Object entity,
          @Assisted("entityType") EntityType entityType,
          World world,
          EntityBaseMapper entityBaseMapper
  ) {
    super(entity, entityType, world, entityBaseMapper);

    this.throwableEntity = (net.minecraft.entity.projectile.ThrowableEntity) entity;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void shoot(Entity thrower, float pitch, float yaw, float pitchOffset, float velocity, float inaccuracy) {
    this.throwableEntity.shoot(
            (net.minecraft.entity.Entity) this.getEntityBaseMapper().getEntityMapper().toMinecraftEntity(thrower),
            pitch,
            yaw,
            pitchOffset,
            velocity,
            inaccuracy
    );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public LivingEntity getThrower() {
    return this.getEntityBaseMapper().getEntityMapper().fromMinecraftLivingEntity(this.throwableEntity);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void shoot(double x, double y, double z, float velocity, float inaccuracy) {
    this.throwableEntity.shoot(x, y, z, velocity, inaccuracy);
  }
}
