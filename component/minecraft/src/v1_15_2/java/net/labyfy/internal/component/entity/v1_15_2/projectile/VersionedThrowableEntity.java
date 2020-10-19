package net.labyfy.internal.component.entity.v1_15_2.projectile;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.labyfy.component.entity.Entity;
import net.labyfy.component.entity.LivingEntity;
import net.labyfy.component.entity.mapper.EntityBaseMapper;
import net.labyfy.component.entity.projectile.ThrowableEntity;
import net.labyfy.component.entity.type.EntityType;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.nbt.NBTCompound;
import net.labyfy.component.nbt.mapper.NBTMapper;
import net.labyfy.component.world.World;
import net.labyfy.internal.component.entity.v1_15_2.VersionedEntity;
import net.minecraft.nbt.CompoundNBT;

@Implement(value = ThrowableEntity.class, version = "1.15.2")
public class VersionedThrowableEntity extends VersionedEntity implements ThrowableEntity {

  private final net.minecraft.entity.projectile.ThrowableEntity throwableEntity;
  protected final NBTMapper nbtMapper;

  @AssistedInject
  public VersionedThrowableEntity(
          @Assisted("entity") Object entity,
          @Assisted("entityType") EntityType entityType,
          World world,
          EntityBaseMapper entityBaseMapper,
          NBTMapper nbtMapper) {
    super(entity, entityType, world, entityBaseMapper);

    this.throwableEntity = (net.minecraft.entity.projectile.ThrowableEntity) entity;
    this.nbtMapper = nbtMapper;
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

  /**
   * {@inheritDoc}
   */
  @Override
  public void setMotion(double x, double y, double z) {
    this.throwableEntity.setMotion(x, y, z);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void readAdditional(NBTCompound compound) {
    this.throwableEntity.readAdditional((CompoundNBT) this.nbtMapper.fromMinecraftNBT(compound));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void writeAdditional(NBTCompound compound) {
    this.throwableEntity.writeAdditional((CompoundNBT) this.nbtMapper.fromMinecraftNBT(compound));
  }

}
