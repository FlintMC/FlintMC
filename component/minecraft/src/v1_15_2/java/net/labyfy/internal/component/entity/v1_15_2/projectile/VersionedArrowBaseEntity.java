package net.labyfy.internal.component.entity.v1_15_2.projectile;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.labyfy.component.entity.Entity;
import net.labyfy.component.entity.mapper.EntityBaseMapper;
import net.labyfy.component.entity.LivingEntity;
import net.labyfy.component.entity.projectile.ArrowBaseEntity;
import net.labyfy.component.entity.projectile.PickupStatus;
import net.labyfy.component.entity.type.EntityTypeRegister;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.items.ItemStack;
import net.labyfy.component.nbt.NBTCompound;
import net.labyfy.component.nbt.mapper.NBTMapper;
import net.labyfy.component.player.PlayerEntity;
import net.labyfy.component.player.type.sound.Sound;
import net.labyfy.component.world.World;
import net.labyfy.internal.component.entity.v1_15_2.VersionedEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.SoundEvent;

@Implement(value = ArrowBaseEntity.class, version = "1.15.2")
public class VersionedArrowBaseEntity extends VersionedEntity implements ArrowBaseEntity {

  private final AccessibleAbstractArrowEntity accessibleAbstractArrowEntity;
  private final AbstractArrowEntity arrowBaseEntity;
  protected final NBTMapper nbtMapper;
  private Sound hitSound;
  private PickupStatus pickupStatus;

  @AssistedInject
  public VersionedArrowBaseEntity(
          @Assisted("entity") Object entity,
          World world,
          EntityBaseMapper entityBaseMapper,
          EntityTypeRegister entityTypeRegister, NBTMapper nbtMapper) {
    super(entity, entityTypeRegister.getEntityType("arrow"), world, entityBaseMapper);
    this.nbtMapper = nbtMapper;


    if (!(entity instanceof net.minecraft.entity.projectile.AbstractArrowEntity)) {
      throw new IllegalArgumentException(entity.getClass().getName() + " is not an instance of " + net.minecraft.entity.projectile.AbstractArrowEntity.class.getName());
    }

    this.arrowBaseEntity = (AbstractArrowEntity) entity;
    this.accessibleAbstractArrowEntity = (AccessibleAbstractArrowEntity) arrowBaseEntity;
    this.pickupStatus = PickupStatus.DISALLOWED;
  }

  @AssistedInject
  public VersionedArrowBaseEntity(
          @Assisted("entity") Object entity,
          @Assisted("x") double x,
          @Assisted("y") double y,
          @Assisted("z") double z,
          World world,
          EntityBaseMapper entityBaseMapper,
          EntityTypeRegister entityTypeRegister,
          NBTMapper nbtMapper) {
    this(entity, world, entityBaseMapper, entityTypeRegister, nbtMapper);
    this.setPosition(x, y, z);
  }

  @AssistedInject
  public VersionedArrowBaseEntity(
          @Assisted("entity") Object entity,
          @Assisted("shooter") LivingEntity shooter,
          World world,
          EntityBaseMapper entityBaseMapper,
          EntityTypeRegister entityTypeRegister,
          NBTMapper nbtMapper) {
    this(
            entity,
            shooter.getPosX(),
            shooter.getPosYEye() - 0.1D,
            shooter.getPosZ(),
            world,
            entityBaseMapper,
            entityTypeRegister,
            nbtMapper);
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
    this.arrowBaseEntity.setHitSound((SoundEvent) this.getEntityBaseMapper().getSoundMapper().toMinecraftSoundEvent(this.hitSound));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void shoot(Entity shooter, float pitch, float yaw, float pitchOffset, float velocity, float inaccuracy) {
    this.arrowBaseEntity.shoot(
            (net.minecraft.entity.Entity) this.getEntityBaseMapper().getEntityMapper().fromMinecraftEntity(shooter),
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
  public Entity getShooter() {
    return this.getEntityBaseMapper().getEntityMapper().fromMinecraftEntity(this.arrowBaseEntity.getShooter());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setShooter(Entity shooter) {
    this.arrowBaseEntity.setShooter(
            (net.minecraft.entity.Entity) this.getEntityBaseMapper().getEntityMapper().toMinecraftEntity(shooter)
    );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ItemStack getArrowStack() {
    return this.getEntityBaseMapper().getItemMapper().fromMinecraft(this.accessibleAbstractArrowEntity.getArrowStack());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public double getDamage() {
    return this.arrowBaseEntity.getDamage();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setDamage(double damage) {
    this.arrowBaseEntity.setDamage(damage);
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
    this.arrowBaseEntity.setKnockbackStrength(knockbackStrength);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isCritical() {
    return this.arrowBaseEntity.getIsCritical();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setCritical(boolean critical) {
    this.arrowBaseEntity.setIsCritical(critical);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public byte getPierceLevel() {
    return this.arrowBaseEntity.getPierceLevel();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setPierceLevel(byte level) {
    this.arrowBaseEntity.setPierceLevel(level);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setEnchantmentEffectsFromEntity(LivingEntity entity, float damage) {
    this.arrowBaseEntity.setEnchantmentEffectsFromEntity(
            (net.minecraft.entity.LivingEntity) this.getEntityBaseMapper().getEntityMapper().fromMinecraftEntity(entity),
            damage
    );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isNoClip() {
    return this.arrowBaseEntity.getNoClip();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setNoClip(boolean noClip) {
    this.arrowBaseEntity.setNoClip(noClip);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isShotFromCrossbow() {
    return this.arrowBaseEntity.getShotFromCrossbow();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setShotFromCrossbow(boolean fromCrossbow) {
    this.arrowBaseEntity.setShotFromCrossbow(fromCrossbow);
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
    AbstractArrowEntity.PickupStatus pickupStatus = (AbstractArrowEntity.PickupStatus) this.accessibleAbstractArrowEntity.getPickupStatus();

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
    this.arrowBaseEntity.shoot(x, y, z, velocity, inaccuracy);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void readAdditional(NBTCompound compound) {
    this.arrowBaseEntity.readAdditional((CompoundNBT) this.nbtMapper.fromMinecraftNBT(compound));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void writeAdditional(NBTCompound compound) {
    this.arrowBaseEntity.writeAdditional((CompoundNBT) this.nbtMapper.fromMinecraftNBT(compound));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setMotion(double x, double y, double z) {
    this.arrowBaseEntity.setMotion(x, y, z);
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
