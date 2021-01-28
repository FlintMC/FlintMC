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

package net.flintmc.mcapi.v1_16_5.entity.passive.farmanimal;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.entity.ai.EntitySenses;
import net.flintmc.mcapi.entity.mapper.EntityFoundationMapper;
import net.flintmc.mcapi.entity.passive.PassiveEntityMapper;
import net.flintmc.mcapi.entity.passive.farmanimal.PigEntity;
import net.flintmc.mcapi.entity.type.EntityTypeRegister;
import net.flintmc.mcapi.items.ItemStack;
import net.flintmc.mcapi.nbt.NBTCompound;
import net.flintmc.mcapi.player.PlayerEntity;
import net.flintmc.mcapi.player.type.hand.Hand;
import net.flintmc.mcapi.v1_16_5.entity.passive.VersionedAnimalEntity;
import net.flintmc.mcapi.v1_16_5.entity.passive.farmanimal.shadow.AccessiblePigEntity;
import net.flintmc.mcapi.world.World;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;

@Implement(value = PigEntity.class, version = "1.16.5")
public class VersionedPigEntity extends VersionedAnimalEntity implements PigEntity {

  private final net.minecraft.entity.passive.PigEntity pigEntity;

  @AssistedInject
  public VersionedPigEntity(
      @Assisted("entity") Object entity,
      EntityTypeRegister entityTypeRegister,
      World world,
      EntityFoundationMapper entityFoundationMapper,
      EntitySenses.Factory entitySensesFactory,
      PassiveEntityMapper passiveEntityMapper) {
    super(
        entity,
        entityTypeRegister.getEntityType("pig"),
        world,
        entityFoundationMapper,
        entitySensesFactory,
        passiveEntityMapper);

    if (!(entity instanceof net.minecraft.entity.passive.PigEntity)) {
      throw new IllegalArgumentException(
          entity.getClass().getName()
              + " is not an instance of "
              + net.minecraft.entity.passive.PigEntity.class.getName());
    }
    this.pigEntity = (net.minecraft.entity.passive.PigEntity) entity;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isSaddled() {
    return this.pigEntity.isHorseSaddled();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setSaddled(boolean saddled) {
    AccessiblePigEntity accessiblePigEntity = (AccessiblePigEntity) this.pigEntity;
    accessiblePigEntity.getBoostHelper().setSaddledFromBoolean(saddled);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean boost() {
    return this.pigEntity.boost();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean processInteract(PlayerEntity entity, Hand hand) {
    ActionResultType actionResultType =
        this.pigEntity.processInitialInteract(
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
  public boolean isBreedingItem(ItemStack breedingItem) {
    return this.pigEntity.isBreedingItem(
        (net.minecraft.item.ItemStack)
            this.getEntityFoundationMapper().getItemMapper().toMinecraft(breedingItem));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void readAdditional(NBTCompound compound) {
    this.pigEntity.readAdditional(
        (CompoundNBT) this.getEntityFoundationMapper().getNbtMapper().toMinecraftNBT(compound));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void writeAdditional(NBTCompound compound) {
    this.pigEntity.writeAdditional(
        (CompoundNBT) this.getEntityFoundationMapper().getNbtMapper().toMinecraftNBT(compound));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean canBeSteered() {
    return this.pigEntity.canBeSteered();
  }
}
