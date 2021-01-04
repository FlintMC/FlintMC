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

package net.flintmc.mcapi.v1_16_4.entity.item;

import java.util.UUID;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.entity.item.ItemEntity;
import net.flintmc.mcapi.entity.mapper.EntityFoundationMapper;
import net.flintmc.mcapi.entity.type.EntityTypeRegister;
import net.flintmc.mcapi.items.ItemStack;
import net.flintmc.mcapi.nbt.NBTCompound;
import net.flintmc.mcapi.v1_16_4.entity.VersionedEntity;
import net.flintmc.mcapi.world.World;
import net.minecraft.nbt.CompoundNBT;

@Implement(value = ItemEntity.class, version = "1.16.4")
public class VersionedItemEntity extends VersionedEntity implements ItemEntity {

  private final net.minecraft.entity.item.ItemEntity itemEntity;

  @AssistedInject
  private VersionedItemEntity(
      @Assisted("entity") Object entity,
      EntityTypeRegister entityTypeRegister,
      World world,
      EntityFoundationMapper entityFoundationMapper) {
    super(entity, entityTypeRegister.getEntityType("item"), world, entityFoundationMapper);

    if (!(entity instanceof net.minecraft.entity.item.ItemEntity)) {
      throw new IllegalArgumentException("");
    }

    this.itemEntity = (net.minecraft.entity.item.ItemEntity) entity;
  }

  @AssistedInject
  private VersionedItemEntity(
      @Assisted("entity") Object entity,
      EntityTypeRegister entityTypeRegister,
      World world,
      EntityFoundationMapper entityFoundationMapper,
      @Assisted("x") double x,
      @Assisted("y") double y,
      @Assisted("z") double z) {
    this(entity, entityTypeRegister, world, entityFoundationMapper);
    this.setPosition(x, y, z);
    this.setYaw(this.getRandom().nextFloat() * 360.0F);
    this.setMotion(
        this.getRandom().nextDouble() * 0.2D - 0.1D,
        0.2D,
        this.getRandom().nextDouble() * 0.2D - 0.1D);
  }

  @AssistedInject
  private VersionedItemEntity(
      @Assisted("entity") Object entity,
      EntityTypeRegister entityTypeRegister,
      World world,
      EntityFoundationMapper entityFoundationMapper,
      @Assisted("x") double x,
      @Assisted("y") double y,
      @Assisted("z") double z,
      @Assisted("itemStack") ItemStack itemStack) {
    this(entity, entityTypeRegister, world, entityFoundationMapper, x, y, z);
    this.setItemStack(itemStack);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ItemStack getItemStack() {
    return this.getEntityFoundationMapper()
        .getItemMapper()
        .fromMinecraft(this.itemEntity.getItem());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setItemStack(ItemStack itemStack) {
    this.itemEntity.setItem(
        (net.minecraft.item.ItemStack)
            this.getEntityFoundationMapper().getItemMapper().toMinecraft(itemStack));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public UUID getOwnerIdentifier() {
    return this.itemEntity.getOwnerId();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setOwnerIdentifier(UUID ownerIdentifier) {
    this.itemEntity.setOwnerId(ownerIdentifier);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public UUID getThrowerIdentifier() {
    return this.itemEntity.getThrowerId();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setThrowerIdentifier(UUID throwerIdentifier) {
    this.itemEntity.setThrowerId(throwerIdentifier);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getAge() {
    return this.itemEntity.getAge();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setDefaultPickupDelay() {
    this.itemEntity.setDefaultPickupDelay();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setNoPickupDelay() {
    this.itemEntity.setNoPickupDelay();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setInfinitePickupDelay() {
    this.itemEntity.setInfinitePickupDelay();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setPickupDelay(int ticks) {
    this.itemEntity.setPickupDelay(ticks);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean cannotPickup() {
    return this.itemEntity.cannotPickup();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setNoDespawn() {
    this.itemEntity.setNoDespawn();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void makeFakeItem() {
    this.itemEntity.makeFakeItem();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void readAdditional(NBTCompound compound) {
    this.itemEntity.readAdditional(
        (CompoundNBT) this.getEntityFoundationMapper().getNbtMapper().toMinecraftNBT(compound));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void writeAdditional(NBTCompound compound) {
    this.itemEntity.writeAdditional(
        (CompoundNBT) this.getEntityFoundationMapper().getNbtMapper().toMinecraftNBT(compound));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ChatComponent getName() {
    return this.getEntityFoundationMapper()
        .getComponentMapper()
        .fromMinecraft(this.itemEntity.getName());
  }
}
