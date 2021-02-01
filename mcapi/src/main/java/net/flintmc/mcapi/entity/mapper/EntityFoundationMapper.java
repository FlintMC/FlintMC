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

package net.flintmc.mcapi.entity.mapper;

import net.flintmc.mcapi.chat.MinecraftComponentMapper;
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

/**
 * Mapper between Minecraft entity and Flint entity.
 */
public interface EntityFoundationMapper {

  /**
   * Creates a new {@link EquipmentSlotType} by using the given Minecraft equipment slot type as the
   * base.
   *
   * @param handle The non-null Minecraft equipment slot type.
   * @return The new Flint {@link EquipmentSlotType} or {@code null}, if the given equipment slot
   * type was invalid.
   * @throws IllegalArgumentException If the given object is no Minecraft equipment slot type.
   */
  EquipmentSlotType fromMinecraftEquipmentSlotType(Object handle);

  /**
   * Creates a new Minecraft equipment slot type by using the Flint {@link EquipmentSlotType} as the
   * base.
   *
   * @param equipmentSlotType The non-null Flint {@link EquipmentSlotType}.
   * @return The new Minecraft equipment slot type or {@code null}, if the given equipment slot type
   * was invalid.
   */
  Object toMinecraftEquipmentSlotType(EquipmentSlotType equipmentSlotType);

  /**
   * Creates a new {@link GameMode} by using the given Minecraft game type as the base.
   *
   * @param handle The non-null Minecraft game type.
   * @return The new Flint {@link GameMode} or {@code null}, if the given game type was invalid.
   * @throws IllegalArgumentException If the given object is no Minecraft game type type.
   */
  GameMode fromMinecraftGameType(Object handle);

  /**
   * Creates a new Minecraft game type by using the Flint {@link GameMode} as the base.
   *
   * @param mode The non-null Flint {@link GameMode}.
   * @return The new Minecraft game type or {@code null}, if the given game type was invalid.
   */
  Object toMinecraftGameType(GameMode mode);

  /**
   * Creates a new {@link GameMode} by using the given Minecraft mover type as the base.
   *
   * @param handle The non-null Minecraft mover type.
   * @return The new Flint {@link GameMode} or {@code null}, if the given mover type was invalid.
   * @throws IllegalArgumentException If the given object is no Minecraft mover type type.
   */
  MoverType fromMinecraftMoverType(Object handle);

  /**
   * Creates a new Minecraft mover type by using the Flint {@link MoverType} as the base.
   *
   * @param mode The non-null Flint {@link MoverType}.
   * @return The new Minecraft mover type or {@code null}, if the given mover type was invalid.
   */
  Object toMinecraftMoverType(MoverType mode);

  /**
   * Creates a new {@link EntityPose} by using the given Minecraft entity pose as the base.
   *
   * @param handle The non-null Minecraft entity pose.
   * @return The new Flint {@link EntityPose} or {@code null}, if the given entity pose was invalid.
   * @throws IllegalArgumentException If the given object is no Minecraft entity pose.
   */
  EntityPose fromMinecraftPose(Object handle);

  /**
   * Creates a new Minecraft entity pose by using the Flint {@link EntityPose} as the base.
   *
   * @param pose The non-null Flint {@link EntityPose}.
   * @return The new Minecraft entity pose or {@code null}, if the given entity pose was invalid.
   */
  Object toMinecraftPose(EntityPose pose);

  /**
   * Retrieves the hand mapper.
   *
   * @return The hand mapper.
   */
  HandMapper getHandMapper();

  /**
   * Retrieves the sound mapper.
   *
   * @return The sound mapper.
   */
  SoundMapper getSoundMapper();

  /**
   * Retrieves the component mapper.
   *
   * @return The component mapper.
   */
  MinecraftComponentMapper getComponentMapper();

  /**
   * Retrieves the item mapper.
   *
   * @return The item mapper.
   */
  MinecraftItemMapper getItemMapper();

  /**
   * Retrieves the resource location provider.
   *
   * @return The resource location provider.
   */
  ResourceLocationProvider getResourceLocationProvider();

  /**
   * Retrieves the entity mapper.
   *
   * @return The entity mapper.
   */
  EntityMapper getEntityMapper();

  /**
   * Retrieves the potion mapper.
   *
   * @return The potion mapper.
   */
  PotionMapper getPotionMapper();

  /**
   * Retrieves the nbt mapper.
   *
   * @return The nbt mapper.
   */
  NBTMapper getNbtMapper();
}
