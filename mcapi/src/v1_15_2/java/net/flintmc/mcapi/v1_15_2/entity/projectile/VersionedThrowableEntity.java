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

package net.flintmc.mcapi.v1_15_2.entity.projectile;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.entity.Entity;
import net.flintmc.mcapi.entity.LivingEntity;
import net.flintmc.mcapi.entity.mapper.EntityFoundationMapper;
import net.flintmc.mcapi.entity.projectile.ThrowableEntity;
import net.flintmc.mcapi.entity.type.EntityType;
import net.flintmc.mcapi.nbt.NBTCompound;
import net.flintmc.mcapi.v1_15_2.entity.VersionedEntity;
import net.flintmc.mcapi.world.World;
import net.minecraft.nbt.CompoundNBT;

@Implement(ThrowableEntity.class)
public class VersionedThrowableEntity extends VersionedEntity implements ThrowableEntity {

  @AssistedInject
  public VersionedThrowableEntity(
      @Assisted("entity") Object entity,
      @Assisted("entityType") EntityType entityType,
      World world,
      EntityFoundationMapper entityFoundationMapper) {
    super(entity, entityType, world, entityFoundationMapper);

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
      EntityFoundationMapper entityFoundationMapper) {
    this(entity, entityType, world, entityFoundationMapper);
    this.setPosition(x, y, z);
  }

  @AssistedInject
  public VersionedThrowableEntity(
      @Assisted("entity") Object entity,
      @Assisted("entityType") EntityType entityType,
      @Assisted("thrower") LivingEntity thrower,
      World world,
      EntityFoundationMapper entityFoundationMapper) {
    this(entity, entityType, world, entityFoundationMapper);
    this.setPosition(thrower.getPosX(), thrower.getPosYEye() - 0.1D, thrower.getPosZ());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected net.minecraft.entity.projectile.ThrowableEntity wrapped() {
    return (net.minecraft.entity.projectile.ThrowableEntity) super.wrapped();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void shoot(
      Entity thrower, float pitch, float yaw, float pitchOffset, float velocity, float inaccuracy) {
    this.wrapped().shoot(
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
        .fromMinecraftLivingEntity(this.wrapped());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void shoot(double x, double y, double z, float velocity, float inaccuracy) {
    this.wrapped().shoot(x, y, z, velocity, inaccuracy);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setMotion(double x, double y, double z) {
    this.wrapped().setMotion(x, y, z);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void readAdditional(NBTCompound compound) {
    this.wrapped().readAdditional(
        (CompoundNBT) this.getEntityFoundationMapper().getNbtMapper().fromMinecraftNBT(compound));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void writeAdditional(NBTCompound compound) {
    this.wrapped().writeAdditional(
        (CompoundNBT) this.getEntityFoundationMapper().getNbtMapper().fromMinecraftNBT(compound));
  }
}
