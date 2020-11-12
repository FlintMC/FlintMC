package net.flintmc.mcapi.v1_15_2.entity.projectile;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.entity.Entity;
import net.flintmc.mcapi.entity.LivingEntity;
import net.flintmc.mcapi.entity.mapper.EntityFoundationMapper;
import net.flintmc.mcapi.entity.projectile.ThrowableEntity;
import net.flintmc.mcapi.entity.render.EntityRenderContext;
import net.flintmc.mcapi.entity.type.EntityType;
import net.flintmc.mcapi.nbt.NBTCompound;
import net.flintmc.mcapi.v1_15_2.entity.VersionedEntity;
import net.flintmc.mcapi.world.World;
import net.minecraft.nbt.CompoundNBT;

@Implement(value = ThrowableEntity.class, version = "1.15.2")
public class VersionedThrowableEntity extends VersionedEntity<net.minecraft.entity.projectile.ThrowableEntity> implements ThrowableEntity {

  @AssistedInject
  public VersionedThrowableEntity(
      @Assisted("entity") Object entity,
      @Assisted("entityType") EntityType entityType,
      World world,
      EntityFoundationMapper entityFoundationMapper,
      EntityRenderContext.Factory entityRenderContextFactory) {
    super(entity, entityType, world, entityFoundationMapper, entityRenderContextFactory);

    if (!(entity instanceof net.minecraft.entity.projectile.ThrowableEntity)) {
      throw new IllegalArgumentException(
          entity.getClass().getName()
              + " is not an instance of "
              + net.minecraft.entity.projectile.ThrowableEntity.class.getName());
    }

  }

  @AssistedInject
  public VersionedThrowableEntity(
      @Assisted("entity") Object entity,
      @Assisted("entityType") EntityType entityType,
      @Assisted("x") double x,
      @Assisted("y") double y,
      @Assisted("z") double z,
      World world,
      EntityFoundationMapper entityFoundationMapper,
      EntityRenderContext.Factory entityRenderContextFactory) {
    this(entity, entityType, world, entityFoundationMapper, entityRenderContextFactory);
    this.setPosition(x, y, z);
  }

  @AssistedInject
  public VersionedThrowableEntity(
      @Assisted("entity") Object entity,
      @Assisted("entityType") EntityType entityType,
      @Assisted("thrower") LivingEntity thrower,
      World world,
      EntityFoundationMapper entityFoundationMapper,
      EntityRenderContext.Factory entityRenderContextFactory) {
    this(entity, entityType, world, entityFoundationMapper, entityRenderContextFactory);
    this.setPosition(thrower.getPosX(), thrower.getPosYEye() - 0.1D, thrower.getPosZ());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void shoot(
      Entity thrower, float pitch, float yaw, float pitchOffset, float velocity, float inaccuracy) {
    this.getHandle().shoot(
        (net.minecraft.entity.Entity)
            this.getEntityFoundationMapper().getEntityMapper().toMinecraftEntity(thrower),
        pitch,
        yaw,
        pitchOffset,
        velocity,
        inaccuracy);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public LivingEntity getThrower() {
    return this.getEntityFoundationMapper()
        .getEntityMapper()
        .fromMinecraftLivingEntity(this.getHandle());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void shoot(double x, double y, double z, float velocity, float inaccuracy) {
    this.getHandle().shoot(x, y, z, velocity, inaccuracy);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setMotion(double x, double y, double z) {
    this.getHandle().setMotion(x, y, z);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void readAdditional(NBTCompound compound) {
    this.getHandle().readAdditional(
        (CompoundNBT) this.getEntityFoundationMapper().getNbtMapper().fromMinecraftNBT(compound));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void writeAdditional(NBTCompound compound) {
    this.getHandle().writeAdditional(
        (CompoundNBT) this.getEntityFoundationMapper().getNbtMapper().fromMinecraftNBT(compound));
  }
}
