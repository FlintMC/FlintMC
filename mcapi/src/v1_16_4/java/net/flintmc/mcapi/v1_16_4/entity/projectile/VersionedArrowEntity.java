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

package net.flintmc.mcapi.v1_16_4.entity.projectile;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.entity.LivingEntity;
import net.flintmc.mcapi.entity.mapper.EntityFoundationMapper;
import net.flintmc.mcapi.entity.projectile.ArrowEntity;
import net.flintmc.mcapi.entity.type.EntityTypeRegister;
import net.flintmc.mcapi.items.ItemStack;
import net.flintmc.mcapi.nbt.NBTCompound;
import net.flintmc.mcapi.world.World;
import net.minecraft.nbt.CompoundNBT;

@Implement(value = ArrowEntity.class, version = "1.15.2")
public class VersionedArrowEntity extends VersionedArrowBaseEntity implements ArrowEntity {

  private final net.minecraft.entity.projectile.ArrowEntity arrowEntity;

  @AssistedInject
  public VersionedArrowEntity(
      @Assisted("entity") Object entity,
      World world,
      EntityFoundationMapper entityFoundationMapper,
      EntityTypeRegister entityTypeRegister) {
    super(entity, world, entityFoundationMapper, entityTypeRegister);

    if (!(entity instanceof net.minecraft.entity.projectile.ArrowEntity)) {
      throw new IllegalArgumentException(
          entity.getClass().getName()
              + " is not an instance of "
              + net.minecraft.entity.projectile.ArrowEntity.class.getName());
    }

    this.arrowEntity = (net.minecraft.entity.projectile.ArrowEntity) entity;
  }

  @AssistedInject
  public VersionedArrowEntity(
      @Assisted("entity") Object entity,
      @Assisted("x") double x,
      @Assisted("y") double y,
      @Assisted("z") double z,
      World world,
      EntityFoundationMapper entityFoundationMapper,
      EntityTypeRegister entityTypeRegister) {
    super(entity, x, y, z, world, entityFoundationMapper, entityTypeRegister);

    if (!(entity instanceof net.minecraft.entity.projectile.ArrowEntity)) {
      throw new IllegalArgumentException(
          entity.getClass().getName()
              + " is not an instance of "
              + net.minecraft.entity.projectile.ArrowEntity.class.getName());
    }

    this.arrowEntity = (net.minecraft.entity.projectile.ArrowEntity) entity;
  }

  @AssistedInject
  public VersionedArrowEntity(
      @Assisted("entity") Object entity,
      @Assisted("shooter") LivingEntity shooter,
      World world,
      EntityFoundationMapper entityFoundationMapper,
      EntityTypeRegister entityTypeRegister) {
    super(entity, shooter, world, entityFoundationMapper, entityTypeRegister);

    if (!(entity instanceof net.minecraft.entity.projectile.ArrowEntity)) {
      throw new IllegalArgumentException(
          entity.getClass().getName()
              + " is not an instance of "
              + net.minecraft.entity.projectile.ArrowEntity.class.getName());
    }

    this.arrowEntity = (net.minecraft.entity.projectile.ArrowEntity) entity;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setPotionEffect(ItemStack itemStack) {
    this.arrowEntity.setPotionEffect(
        (net.minecraft.item.ItemStack)
            this.getEntityFoundationMapper().getItemMapper().toMinecraft(itemStack));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getColor() {
    return this.arrowEntity.getColor();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void readAdditional(NBTCompound compound) {
    this.arrowEntity.readAdditional(
        (CompoundNBT) this.getEntityFoundationMapper().getNbtMapper().fromMinecraftNBT(compound));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void writeAdditional(NBTCompound compound) {
    this.arrowEntity.writeAdditional(
        (CompoundNBT) this.getEntityFoundationMapper().getNbtMapper().fromMinecraftNBT(compound));
  }
}
