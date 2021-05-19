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

package net.flintmc.mcapi.v1_16_5.entity.projectile;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.entity.Entity;
import net.flintmc.mcapi.entity.LivingEntity;
import net.flintmc.mcapi.entity.mapper.EntityFoundationMapper;
import net.flintmc.mcapi.entity.projectile.ArrowBaseEntity;
import net.flintmc.mcapi.entity.projectile.type.PickupStatus;
import net.flintmc.mcapi.entity.type.EntityTypeRegister;
import net.flintmc.mcapi.items.ItemStack;
import net.flintmc.mcapi.nbt.NBTCompound;
import net.flintmc.mcapi.player.PlayerEntity;
import net.flintmc.mcapi.player.type.sound.Sound;
import net.flintmc.mcapi.v1_16_5.entity.VersionedEntity;
import net.flintmc.mcapi.world.World;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.SoundEvent;

@Implement(ArrowBaseEntity.class)
public class VersionedArrowBaseEntity extends VersionedEntity implements ArrowBaseEntity {

  private final AccessibleAbstractArrowEntity accessibleAbstractArrowEntity;
  private final AbstractArrowEntity arrowBaseEntity;
  private Sound hitSound;
  private PickupStatus pickupStatus;

  @AssistedInject
  public VersionedArrowBaseEntity(
      @Assisted("entity") Object entity,
      World world,
      EntityFoundationMapper entityFoundationMapper,
      EntityTypeRegister entityTypeRegister) {
    super(entity, entityTypeRegister.getEntityType("arrow"), world, entityFoundationMapper);

    if (!(entity instanceof AbstractArrowEntity)) {
      throw new IllegalArgumentException(
          entity.getClass().getName()
              + " is not an instance of "
              + AbstractArrowEntity.class.getName());
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
      EntityFoundationMapper entityFoundationMapper,
      EntityTypeRegister entityTypeRegister) {
    this(entity, world, entityFoundationMapper, entityTypeRegister);
    this.setPosition(x, y, z);
  }

  @AssistedInject
  public VersionedArrowBaseEntity(
      @Assisted("entity") Object entity,
      @Assisted("shooter") LivingEntity shooter,
      World world,
      EntityFoundationMapper entityFoundationMapper,
      EntityTypeRegister entityTypeRegister) {
    this(
        entity,
        shooter.getPosX(),
        shooter.getPosYEye() - 0.1D,
        shooter.getPosZ(),
        world,
        entityFoundationMapper,
        entityTypeRegister);
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
    this.arrowBaseEntity.setHitSound(
        (SoundEvent)
            this.getEntityFoundationMapper().getSoundMapper().toMinecraftSoundEvent(this.hitSound));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void shoot(
      Entity shooter, float pitch, float yaw, float pitchOffset, float velocity, float inaccuracy) {
    this.arrowBaseEntity.shoot(
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
        .fromMinecraftEntity(this.arrowBaseEntity.getShooter());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setShooter(Entity shooter) {
    this.arrowBaseEntity.setShooter(
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
        (net.minecraft.entity.LivingEntity)
            this.getEntityFoundationMapper().getEntityMapper().fromMinecraftEntity(entity),
        damage);
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
    this.arrowBaseEntity.shoot(x, y, z, velocity, inaccuracy);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void readAdditional(NBTCompound compound) {
    this.arrowBaseEntity.readAdditional(
        (CompoundNBT) this.getEntityFoundationMapper().getNbtMapper().fromMinecraftNBT(compound));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void writeAdditional(NBTCompound compound) {
    this.arrowBaseEntity.writeAdditional(
        (CompoundNBT) this.getEntityFoundationMapper().getNbtMapper().fromMinecraftNBT(compound));
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
