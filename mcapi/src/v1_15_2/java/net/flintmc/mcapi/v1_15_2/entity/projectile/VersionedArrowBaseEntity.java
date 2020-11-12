package net.flintmc.mcapi.v1_15_2.entity.projectile;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.entity.Entity;
import net.flintmc.mcapi.entity.LivingEntity;
import net.flintmc.mcapi.entity.mapper.EntityFoundationMapper;
import net.flintmc.mcapi.entity.projectile.ArrowBaseEntity;
import net.flintmc.mcapi.entity.projectile.type.PickupStatus;
import net.flintmc.mcapi.entity.render.EntityRenderContext;
import net.flintmc.mcapi.entity.type.EntityTypeRegister;
import net.flintmc.mcapi.items.ItemStack;
import net.flintmc.mcapi.nbt.NBTCompound;
import net.flintmc.mcapi.player.PlayerEntity;
import net.flintmc.mcapi.player.type.sound.Sound;
import net.flintmc.mcapi.v1_15_2.entity.VersionedEntity;
import net.flintmc.mcapi.world.World;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.SoundEvent;

@Implement(value = ArrowBaseEntity.class, version = "1.15.2")
public class VersionedArrowBaseEntity extends VersionedEntity<AbstractArrowEntity> implements ArrowBaseEntity {

  private final AccessibleAbstractArrowEntity accessibleAbstractArrowEntity;
  private Sound hitSound;
  private PickupStatus pickupStatus;

  @AssistedInject
  public VersionedArrowBaseEntity(
      @Assisted("entity") Object entity,
      World world,
      EntityFoundationMapper entityFoundationMapper,
      EntityTypeRegister entityTypeRegister,
      EntityRenderContext.Factory entityRenderContextFactory) {
    super(entity, entityTypeRegister.getEntityType("arrow"), world, entityFoundationMapper, entityRenderContextFactory);

    if (!(entity instanceof net.minecraft.entity.projectile.AbstractArrowEntity)) {
      throw new IllegalArgumentException(
          entity.getClass().getName()
              + " is not an instance of "
              + net.minecraft.entity.projectile.AbstractArrowEntity.class.getName());
    }

    this.accessibleAbstractArrowEntity = (AccessibleAbstractArrowEntity) this.getHandle();
    this.pickupStatus = PickupStatus.DISALLOWED;
  }

  @AssistedInject
  public VersionedArrowBaseEntity(
      @Assisted("entity") Object entity,
      @Assisted("x") double x,
      @Assisted("y") double y,
      @Assisted("z") double z,
      World world,
      EntityFoundationMapper entityFoundationMapper,
      EntityTypeRegister entityTypeRegister,
      EntityRenderContext.Factory entityRenderContextFactory) {
    this(entity, world, entityFoundationMapper, entityTypeRegister, entityRenderContextFactory);
    this.setPosition(x, y, z);
  }

  @AssistedInject
  public VersionedArrowBaseEntity(
      @Assisted("entity") Object entity,
      @Assisted("shooter") LivingEntity shooter,
      World world,
      EntityFoundationMapper entityFoundationMapper,
      EntityTypeRegister entityTypeRegister,
      EntityRenderContext.Factory entityRenderContextFactory) {
    this(
        entity,
        shooter.getPosX(),
        shooter.getPosYEye() - 0.1D,
        shooter.getPosZ(),
        world,
        entityFoundationMapper,
        entityTypeRegister,
        entityRenderContextFactory
    );
    this.setShooter(shooter);

    if (shooter instanceof PlayerEntity) {
      this.pickupStatus = PickupStatus.ALLOWED;
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Sound getHitSound() {
    return this.hitSound;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setHitSound(Sound sound) {
    this.hitSound = sound;
    this.getHandle().setHitSound(
        (SoundEvent)
            this.getEntityFoundationMapper().getSoundMapper().toMinecraftSoundEvent(this.hitSound));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void shoot(
      Entity shooter, float pitch, float yaw, float pitchOffset, float velocity, float inaccuracy) {
    this.getHandle().shoot(
        (net.minecraft.entity.Entity)
            this.getEntityFoundationMapper().getEntityMapper().fromMinecraftEntity(shooter),
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
  public Entity getShooter() {
    return this.getEntityFoundationMapper()
        .getEntityMapper()
        .fromMinecraftEntity(this.getHandle().getShooter());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setShooter(Entity shooter) {
    this.getHandle().setShooter(
        (net.minecraft.entity.Entity)
            this.getEntityFoundationMapper().getEntityMapper().toMinecraftEntity(shooter));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ItemStack getArrowStack() {
    return this.getEntityFoundationMapper()
        .getItemMapper()
        .fromMinecraft(this.accessibleAbstractArrowEntity.getArrowStack());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public double getDamage() {
    return this.getHandle().getDamage();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setDamage(double damage) {
    this.getHandle().setDamage(damage);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getKnockbackStrength() {
    return this.accessibleAbstractArrowEntity.getKnockbackStrength();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setKnockbackStrength(int knockbackStrength) {
    this.getHandle().setKnockbackStrength(knockbackStrength);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isCritical() {
    return this.getHandle().getIsCritical();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setCritical(boolean critical) {
    this.getHandle().setIsCritical(critical);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public byte getPierceLevel() {
    return this.getHandle().getPierceLevel();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setPierceLevel(byte level) {
    this.getHandle().setPierceLevel(level);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setEnchantmentEffectsFromEntity(LivingEntity entity, float damage) {
    this.getHandle().setEnchantmentEffectsFromEntity(
        (net.minecraft.entity.LivingEntity)
            this.getEntityFoundationMapper().getEntityMapper().fromMinecraftEntity(entity),
        damage);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isNoClip() {
    return this.getHandle().getNoClip();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setNoClip(boolean noClip) {
    this.getHandle().setNoClip(noClip);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isShotFromCrossbow() {
    return this.getHandle().getShotFromCrossbow();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setShotFromCrossbow(boolean fromCrossbow) {
    this.getHandle().setShotFromCrossbow(fromCrossbow);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getWaterDrag() {
    return this.accessibleAbstractArrowEntity.getWaterDrag();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public PickupStatus getPickupStatus() {
    AbstractArrowEntity.PickupStatus pickupStatus =
        (AbstractArrowEntity.PickupStatus) this.accessibleAbstractArrowEntity.getPickupStatus();

    if (pickupStatus.ordinal() != this.pickupStatus.getIdentifier()) {
      this.pickupStatus = this.fromMinecraftPickupStatus(pickupStatus);
    }

    return this.pickupStatus;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setPickupStatus(PickupStatus pickupStatus) {
    this.pickupStatus = pickupStatus;
    this.accessibleAbstractArrowEntity.setPickupStatus(this.toMinecraftPickupStatus(pickupStatus));
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

  /**
   * {@inheritDoc}
   */
  @Override
  public void setMotion(double x, double y, double z) {
    this.getHandle().setMotion(x, y, z);
  }

  protected PickupStatus fromMinecraftPickupStatus(AbstractArrowEntity.PickupStatus pickupStatus) {
    switch (pickupStatus) {
      case ALLOWED:
        return PickupStatus.ALLOWED;
      case CREATIVE_ONLY:
        return PickupStatus.CREATIVE_ONLY;
      default:
        return PickupStatus.DISALLOWED;
    }
  }

  protected AbstractArrowEntity.PickupStatus toMinecraftPickupStatus(PickupStatus pickupStatus) {
    return AbstractArrowEntity.PickupStatus.getByOrdinal(pickupStatus.getIdentifier());
  }
}
