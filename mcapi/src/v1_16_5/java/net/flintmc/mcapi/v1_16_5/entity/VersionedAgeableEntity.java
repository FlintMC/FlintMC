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

package net.flintmc.mcapi.v1_16_5.entity;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.entity.AgeableEntity;
import net.flintmc.mcapi.entity.ai.EntitySenses;
import net.flintmc.mcapi.entity.mapper.EntityFoundationMapper;
import net.flintmc.mcapi.entity.type.EntityType;
import net.flintmc.mcapi.nbt.NBTCompound;
import net.flintmc.mcapi.player.PlayerEntity;
import net.flintmc.mcapi.player.type.hand.Hand;
import net.flintmc.mcapi.world.World;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;

/**
 * 1.16.5 implementation of the {@link AgeableEntity}.
 */
@Implement(AgeableEntity.class)
public class VersionedAgeableEntity extends VersionedCreatureEntity implements AgeableEntity {

  private final net.minecraft.entity.AgeableEntity ageableEntity;

  @AssistedInject
  public VersionedAgeableEntity(
      @Assisted("entity") Object entity,
      @Assisted("entityType") EntityType entityType,
      World world,
      EntityFoundationMapper entityFoundationMapper,
      EntitySenses.Factory entitySensesFactory) {
    super(entity, entityType, world, entityFoundationMapper, entitySensesFactory);

    if (!(entity instanceof net.minecraft.entity.AgeableEntity)) {
      throw new IllegalArgumentException(
          entity.getClass().getName()
              + " is not an instance of "
              + net.minecraft.entity.AgeableEntity.class.getName());
    }
    this.ageableEntity = (net.minecraft.entity.AgeableEntity) entity;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean processInteract(PlayerEntity entity, Hand hand) {
    ActionResultType actionResultType = this.ageableEntity.processInitialInteract(
        (net.minecraft.entity.player.PlayerEntity)
            this.getEntityFoundationMapper().getEntityMapper().toMinecraftPlayerEntity(entity),
        (net.minecraft.util.Hand)
            this.getEntityFoundationMapper().getHandMapper().toMinecraftHand(hand));
    return actionResultType.isSuccessOrConsume();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getGrowingAge() {
    return this.ageableEntity.getGrowingAge();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setGrowingAge(int age) {
    this.ageableEntity.setGrowingAge(age);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void ageUp(int growth, boolean updateForcedAge) {
    this.ageableEntity.ageUp(growth, updateForcedAge);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addGrowth(int growth) {
    this.ageableEntity.addGrowth(growth);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void readAdditional(NBTCompound compound) {
    this.ageableEntity.readAdditional(
        (CompoundNBT) this.getEntityFoundationMapper().getNbtMapper().fromMinecraftNBT(compound));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void writeAdditional(NBTCompound compound) {
    this.ageableEntity.writeAdditional(
        (CompoundNBT) this.getEntityFoundationMapper().getNbtMapper().fromMinecraftNBT(compound));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isChild() {
    return this.ageableEntity.isChild();
  }
}
