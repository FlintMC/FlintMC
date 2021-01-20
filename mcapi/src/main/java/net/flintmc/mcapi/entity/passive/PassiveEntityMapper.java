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

package net.flintmc.mcapi.entity.passive;

import net.flintmc.mcapi.entity.passive.farmanimal.PigEntity;

/**
 * Mapper between the Minecraft's passive entities and Flint's passive entities.
 */
public interface PassiveEntityMapper {

  /**
   * Creates a new {@link AmbientEntity} by using the given Minecraft ambient entity as the base.
   *
   * @param handle The non-null Minecraft ambient entity.
   * @return The new Flint {@link AmbientEntity} or {@code null}, if the given ambient entity was
   * invalid.
   * @throws IllegalArgumentException If the given object is no Minecraft ambient entity.
   */
  AmbientEntity fromMinecraftAmbientEntity(Object handle);

  /**
   * Creates a new Minecraft ambient entity by using the Flint {@link AmbientEntity} as the base.
   *
   * @param ambientEntity The non-null Flint {@link AmbientEntity}.
   * @return The new Minecraft ambient entity or {@code null}, if the given ambient entity was
   * invalid.
   */
  Object toMinecraftAmbientEntity(AmbientEntity ambientEntity);

  /**
   * Creates a new {@link AnimalEntity} by using the given Minecraft animal entity as the base.
   *
   * @param handle The non-null Minecraft animal entity.
   * @return The new Flint {@link AnimalEntity} or {@code null}, if the given animal entity was
   * invalid.
   * @throws IllegalArgumentException If the given object is no Minecraft animal entity.
   */
  AnimalEntity fromMinecraftAnimalEntity(Object handle);

  /**
   * Creates a new Minecraft animal entity by using the Flint {@link AnimalEntity} as the base.
   *
   * @param animalEntity The non-null Flint {@link AnimalEntity}.
   * @return The new Minecraft animal entity or {@code null}, if the given animal entity was invalid.
   */
  Object toMinecraftAnimalEntity(AnimalEntity animalEntity);

  /**
   * Creates a new {@link PigEntity} by using the given Minecraft pig entity as the base.
   *
   * @param handle The non-null Minecraft pig entity.
   * @return The new Flint {@link PigEntity} or {@code null}, if the given pig entity was invalid.
   * @throws IllegalArgumentException If the given object is no Minecraft pig entity.
   */
  PigEntity fromMinecraftPigEntity(Object handle);

  /**
   * Creates a new Minecraft pig entity by using the Flint {@link PigEntity} as the base.
   *
   * @param pigEntity The non-null Flint {@link PigEntity}.
   * @return The new Minecraft pig entity or {@code null}, if the given pig entity was invalid.
   */
  Object toMinecraftPigEntity(PigEntity pigEntity);
}
