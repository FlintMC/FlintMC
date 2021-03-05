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

package net.flintmc.mcapi.v1_16_5.entity.item;

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
import net.flintmc.mcapi.v1_16_5.entity.VersionedEntity;
import net.flintmc.mcapi.world.World;
import net.minecraft.nbt.CompoundNBT;

@Implement(value = ItemEntity.class, version = "1.16.5")
public class VersionedItemEntity extends VersionedEntity implements ItemEntity {

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
    this(
        entity,
        entityTypeRegister,
        world,
        entityFoundationMapper,
        x,
        y,
        z);
    this.setItemStack(itemStack);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected net.minecraft.entity.item.ItemEntity wrapped() {
    return (net.minecraft.entity.item.ItemEntity) super.wrapped();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ItemStack getItemStack() {
    return this.getEntityFoundationMapper()
        .getItemMapper()
        .fromMinecraft(this.wrapped().getItem());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setItemStack(ItemStack itemStack) {
    this.wrapped()
        .setItem(
            (net.minecraft.item.ItemStack)
                this.getEntityFoundationMapper().getItemMapper().toMinecraft(itemStack));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public UUID getOwnerIdentifier() {
    return this.wrapped().getOwnerId();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setOwnerIdentifier(UUID ownerIdentifier) {
    this.wrapped().setOwnerId(ownerIdentifier);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public UUID getThrowerIdentifier() {
    return this.wrapped().getThrowerId();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setThrowerIdentifier(UUID throwerIdentifier) {
    this.wrapped().setThrowerId(throwerIdentifier);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getAge() {
    return this.wrapped().getAge();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setDefaultPickupDelay() {
    this.wrapped().setDefaultPickupDelay();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setNoPickupDelay() {
    this.wrapped().setNoPickupDelay();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setInfinitePickupDelay() {
    this.wrapped().setInfinitePickupDelay();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setPickupDelay(int ticks) {
    this.wrapped().setPickupDelay(ticks);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean cannotPickup() {
    return this.wrapped().cannotPickup();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setNoDespawn() {
    this.wrapped().setNoDespawn();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void makeFakeItem() {
    this.wrapped().makeFakeItem();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void readAdditional(NBTCompound compound) {
    this.wrapped()
        .readAdditional(
            (CompoundNBT) this.getEntityFoundationMapper().getNbtMapper().toMinecraftNBT(compound));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void writeAdditional(NBTCompound compound) {
    this.wrapped()
        .writeAdditional(
            (CompoundNBT) this.getEntityFoundationMapper().getNbtMapper().toMinecraftNBT(compound));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ChatComponent getName() {
    return this.getEntityFoundationMapper()
        .getComponentMapper()
        .fromMinecraft(this.wrapped().getName());
  }
}
