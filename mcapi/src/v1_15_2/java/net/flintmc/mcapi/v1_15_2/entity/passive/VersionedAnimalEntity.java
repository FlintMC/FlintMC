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

package net.flintmc.mcapi.v1_15_2.entity.passive;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.entity.ai.EntitySenses;
import net.flintmc.mcapi.entity.mapper.EntityFoundationMapper;
import net.flintmc.mcapi.entity.passive.AnimalEntity;
import net.flintmc.mcapi.entity.passive.PassiveEntityMapper;
import net.flintmc.mcapi.entity.type.EntityType;
import net.flintmc.mcapi.items.ItemStack;
import net.flintmc.mcapi.nbt.NBTCompound;
import net.flintmc.mcapi.player.PlayerEntity;
import net.flintmc.mcapi.player.type.hand.Hand;
import net.flintmc.mcapi.v1_15_2.entity.VersionedAgeableEntity;
import net.flintmc.mcapi.world.World;
import net.minecraft.nbt.CompoundNBT;

@Implement(value = AnimalEntity.class, version = "1.15.2")
public class VersionedAnimalEntity extends VersionedAgeableEntity implements AnimalEntity {

  private final net.minecraft.entity.passive.AnimalEntity animalEntity;
  private final PassiveEntityMapper passiveEntityMapper;

  @AssistedInject
  public VersionedAnimalEntity(
      @Assisted("entity") Object entity,
      @Assisted("entityType") EntityType entityType,
      World world,
      EntityFoundationMapper entityFoundationMapper,
      EntitySenses.Factory entitySensesFactory,
      PassiveEntityMapper passiveEntityMapper) {
    super(entity, entityType, world, entityFoundationMapper, entitySensesFactory);
    this.passiveEntityMapper = passiveEntityMapper;

    if (!(entity instanceof net.minecraft.entity.passive.AnimalEntity)) {
      throw new IllegalArgumentException(
          entity.getClass().getName()
              + " is not an instance of "
              + net.minecraft.entity.passive.AnimalEntity.class.getName());
    }

    this.animalEntity = (net.minecraft.entity.passive.AnimalEntity) entity;
  }

  /** {@inheritDoc} */
  @Override
  public boolean isBreedingItem(ItemStack breedingItem) {
    return this.animalEntity.isBreedingItem(
        (net.minecraft.item.ItemStack)
            this.getEntityFoundationMapper().getItemMapper().toMinecraft(breedingItem));
  }

  /** {@inheritDoc} */
  @Override
  public boolean canBreed() {
    return this.animalEntity.canBreed();
  }

  /** {@inheritDoc} */
  @Override
  public boolean isInLove() {
    return this.animalEntity.isInLove();
  }

  /** {@inheritDoc} */
  @Override
  public void setInLove(int ticks) {
    this.animalEntity.setInLove(ticks);
  }

  /** {@inheritDoc} */
  @Override
  public void setInLove(PlayerEntity player) {
    this.animalEntity.setInLove(
        (net.minecraft.entity.player.PlayerEntity)
            this.getEntityFoundationMapper().getEntityMapper().toMinecraftPlayerEntity(player));
  }

  /** {@inheritDoc} */
  @Override
  public void resetInLove() {
    this.animalEntity.resetInLove();
  }

  /** {@inheritDoc} */
  @Override
  public boolean canMateWith(AnimalEntity entity) {
    return this.animalEntity.canMateWith(
        (net.minecraft.entity.passive.AnimalEntity)
            this.passiveEntityMapper.toMinecraftAnimalEntity(entity));
  }

  /** {@inheritDoc} */
  @Override
  public int getTalkInterval() {
    return this.animalEntity.getTalkInterval();
  }

  /** {@inheritDoc} */
  @Override
  public boolean canDespawn(double distanceToClosestPlayer) {
    return this.animalEntity.canDespawn(distanceToClosestPlayer);
  }

  /** {@inheritDoc} */
  @Override
  public boolean processInteract(PlayerEntity entity, Hand hand) {
    return this.animalEntity.processInteract(
        (net.minecraft.entity.player.PlayerEntity)
            this.getEntityFoundationMapper().getEntityMapper().toMinecraftPlayerEntity(entity),
        (net.minecraft.util.Hand)
            this.getEntityFoundationMapper().getHandMapper().toMinecraftHand(hand));
  }

  /** {@inheritDoc} */
  @Override
  public void readAdditional(NBTCompound compound) {
    this.animalEntity.readAdditional(
        (CompoundNBT) this.getEntityFoundationMapper().getNbtMapper().toMinecraftNBT(compound));
  }

  /** {@inheritDoc} */
  @Override
  public void writeAdditional(NBTCompound compound) {
    this.animalEntity.writeAdditional(
        (CompoundNBT) this.getEntityFoundationMapper().getNbtMapper().toMinecraftNBT(compound));
  }
}
