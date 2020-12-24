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

package net.flintmc.mcapi.v1_15_2.entity.mapper;

import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.chat.MinecraftComponentMapper;
import net.flintmc.mcapi.entity.mapper.EntityFoundationMapper;
import net.flintmc.mcapi.entity.mapper.EntityMapper;
import net.flintmc.mcapi.entity.reason.MoverType;
import net.flintmc.mcapi.entity.type.EntityPose;
import net.flintmc.mcapi.items.inventory.EquipmentSlotType;
import net.flintmc.mcapi.items.mapper.MinecraftItemMapper;
import net.flintmc.mcapi.nbt.mapper.NBTMapper;
import net.flintmc.mcapi.player.type.GameMode;
import net.flintmc.mcapi.player.type.hand.HandMapper;
import net.flintmc.mcapi.player.type.sound.SoundMapper;
import net.flintmc.mcapi.potion.mapper.PotionMapper;
import net.flintmc.mcapi.resources.ResourceLocationProvider;
import net.minecraft.entity.Pose;
import net.minecraft.world.GameType;

import javax.inject.Inject;
import javax.inject.Singleton;

/** 1.15.2 implementation of the {@link EntityFoundationMapper}. */
@Singleton
@Implement(value = EntityFoundationMapper.class, version = "1.15.2")
public class VersionedEntityFoundationMapper implements EntityFoundationMapper {

  private final EntityMapper entityMapper;
  private final HandMapper handMapper;
  private final MinecraftItemMapper itemMapper;
  private final MinecraftComponentMapper componentMapper;
  private final PotionMapper potionMapper;
  private final NBTMapper nbtMapper;
  private final ResourceLocationProvider resourceLocationProvider;
  private final SoundMapper soundMapper;

  @Inject
  private VersionedEntityFoundationMapper(
          EntityMapper entityMapper,
          HandMapper handMapper,
          MinecraftItemMapper itemMapper,
          MinecraftComponentMapper componentMapper,
          PotionMapper potionMapper,
          NBTMapper nbtMapper,
          ResourceLocationProvider resourceLocationProvider,
          SoundMapper soundMapper) {
    this.itemMapper = itemMapper;
    this.componentMapper = componentMapper;
    this.potionMapper = potionMapper;
    this.nbtMapper = nbtMapper;
    this.resourceLocationProvider = resourceLocationProvider;
    this.soundMapper = soundMapper;
    this.handMapper = handMapper;
    this.entityMapper = entityMapper;
  }

  /** {@inheritDoc} */
  @Override
  public EquipmentSlotType fromMinecraftEquipmentSlotType(Object handle) {
    if (!(handle instanceof net.minecraft.inventory.EquipmentSlotType)) {
      throw new IllegalArgumentException(
          handle.getClass().getName()
              + " is not an instance of "
              + net.minecraft.inventory.EquipmentSlotType.class.getName());
    }

    net.minecraft.inventory.EquipmentSlotType equipmentSlotType =
        (net.minecraft.inventory.EquipmentSlotType) handle;

    switch (equipmentSlotType) {
      case MAINHAND:
        return EquipmentSlotType.MAIN_HAND;
      case OFFHAND:
        return EquipmentSlotType.OFF_HAND;
      case FEET:
        return EquipmentSlotType.FEET;
      case LEGS:
        return EquipmentSlotType.LEGS;
      case CHEST:
        return EquipmentSlotType.CHEST;
      case HEAD:
        return EquipmentSlotType.HEAD;
      default:
        throw new IllegalStateException("Unexpected value: " + equipmentSlotType);
    }
  }

  /** {@inheritDoc} */
  @Override
  public Object toMinecraftEquipmentSlotType(EquipmentSlotType equipmentSlotType) {
    switch (equipmentSlotType) {
      case MAIN_HAND:
        return net.minecraft.inventory.EquipmentSlotType.MAINHAND;
      case OFF_HAND:
        return net.minecraft.inventory.EquipmentSlotType.OFFHAND;
      case FEET:
        return net.minecraft.inventory.EquipmentSlotType.FEET;
      case LEGS:
        return net.minecraft.inventory.EquipmentSlotType.LEGS;
      case CHEST:
        return net.minecraft.inventory.EquipmentSlotType.CHEST;
      case HEAD:
        return net.minecraft.inventory.EquipmentSlotType.HEAD;
      default:
        throw new IllegalStateException("Unexpected value: " + equipmentSlotType);
    }
  }

  /** {@inheritDoc} */
  @Override
  public GameMode fromMinecraftGameType(Object handle) {
    if (!(handle instanceof GameType)) {
      throw new IllegalArgumentException(
          handle.getClass().getName() + " is not an instance of " + GameType.class.getName());
    }

    GameType gameType = (GameType) handle;

    switch (gameType) {
      case NOT_SET:
      case SURVIVAL:
        return GameMode.SURVIVAL;
      case CREATIVE:
        return GameMode.CREATIVE;
      case ADVENTURE:
        return GameMode.ADVENTURE;
      case SPECTATOR:
        return GameMode.SPECTATOR;
      default:
        throw new IllegalStateException("Unexpected value: " + gameType);
    }
  }

  /** {@inheritDoc} */
  @Override
  public Object toMinecraftGameType(GameMode mode) {
    switch (mode) {
      case SURVIVAL:
        return GameType.SURVIVAL;
      case CREATIVE:
        return GameType.CREATIVE;
      case ADVENTURE:
        return GameType.ADVENTURE;
      case SPECTATOR:
        return GameType.SPECTATOR;
      default:
        return GameType.NOT_SET;
    }
  }

  /** {@inheritDoc} */
  @Override
  public MoverType fromMinecraftMoverType(Object handle) {
    if (!(handle instanceof net.minecraft.entity.MoverType)) {
      throw new IllegalArgumentException(
          handle.getClass().getName()
              + " is not an instance of "
              + net.minecraft.entity.MoverType.class.getName());
    }

    net.minecraft.entity.MoverType moverType = (net.minecraft.entity.MoverType) handle;

    switch (moverType) {
      case SELF:
        return MoverType.SELF;
      case PLAYER:
        return MoverType.PLAYER;
      case PISTON:
        return MoverType.PISTON;
      case SHULKER_BOX:
        return MoverType.SHULKER_BOX;
      case SHULKER:
        return MoverType.SHULKER;
      default:
        throw new IllegalStateException("Unexpected value: " + moverType);
    }
  }

  /** {@inheritDoc} */
  @Override
  public Object toMinecraftMoverType(MoverType mode) {
    switch (mode) {
      case PLAYER:
        return net.minecraft.entity.MoverType.PLAYER;
      case PISTON:
        return net.minecraft.entity.MoverType.PISTON;
      case SHULKER_BOX:
        return net.minecraft.entity.MoverType.SHULKER_BOX;
      case SHULKER:
        return net.minecraft.entity.MoverType.SHULKER;
      default:
        return net.minecraft.entity.MoverType.SELF;
    }
  }

  /** {@inheritDoc} */
  @Override
  public EntityPose fromMinecraftPose(Object handle) {
    if (!(handle instanceof Pose)) {
      throw new IllegalArgumentException(
          handle.getClass().getName() + " is not an instance of " + Pose.class.getName());
    }

    Pose pose = (Pose) handle;

    switch (pose) {
      case STANDING:
        return EntityPose.STANDING;
      case FALL_FLYING:
        return EntityPose.FALL_FLYING;
      case SLEEPING:
        return EntityPose.SLEEPING;
      case SWIMMING:
        return EntityPose.SWIMMING;
      case SPIN_ATTACK:
        return EntityPose.SPIN_ATTACK;
      case CROUCHING:
        return EntityPose.CROUCHING;
      case DYING:
        return EntityPose.DYING;
      default:
        throw new IllegalStateException("Unexpected value: " + pose);
    }
  }

  /** {@inheritDoc} */
  @Override
  public Object toMinecraftPose(EntityPose pose) {
    switch (pose) {
      case STANDING:
        return Pose.STANDING;
      case FALL_FLYING:
        return Pose.FALL_FLYING;
      case SLEEPING:
        return Pose.SLEEPING;
      case SWIMMING:
        return Pose.SWIMMING;
      case SPIN_ATTACK:
        return Pose.SPIN_ATTACK;
      case CROUCHING:
        return Pose.CROUCHING;
      case DYING:
        return Pose.DYING;
      default:
        throw new IllegalStateException("Unexpected value: " + pose);
    }
  }

  /** {@inheritDoc} */
  @Override
  public HandMapper getHandMapper() {
    return this.handMapper;
  }

  /** {@inheritDoc} */
  @Override
  public SoundMapper getSoundMapper() {
    return this.soundMapper;
  }

  /** {@inheritDoc} */
  @Override
  public MinecraftComponentMapper getComponentMapper() {
    return this.componentMapper;
  }

  /** {@inheritDoc} */
  @Override
  public MinecraftItemMapper getItemMapper() {
    return this.itemMapper;
  }

  /** {@inheritDoc} */
  @Override
  public ResourceLocationProvider getResourceLocationProvider() {
    return this.resourceLocationProvider;
  }

  /** {@inheritDoc} */
  @Override
  public EntityMapper getEntityMapper() {
    return this.entityMapper;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public PotionMapper getPotionMapper() {
    return this.potionMapper;
  }

  /** {@inheritDoc} */
  @Override
  public NBTMapper getNbtMapper() {
    return this.nbtMapper;
  }
}
