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

package net.flintmc.mcapi.player.type.sound;

/**
 * Mapper between the Minecraft sound events, sound categories and Flint {@link Sound}, {@link
 * SoundCategory}.
 */
public interface SoundMapper {

  /**
   * Retrieves a {@link Sound} by using the given Minecraft sound event.
   *
   * @param soundEvent The non-null minecraft sound event.
   * @return The sound.
   * @throws IllegalArgumentException If the given object is not a Minecraft sound event.
   */
  Sound fromMinecraftSoundEvent(Object soundEvent);

  /**
   * Retrieves a Minecraft sound event by using the given {@link Sound}.
   *
   * @param sound The non-null sound.
   * @return The sound event.
   */
  Object toMinecraftSoundEvent(Sound sound);

  /**
   * Retrieves a {@link SoundCategory} constant by using the given Minecraft sound category.
   *
   * @param soundCategory The non-null minecraft sound category.
   * @return The {@link SoundCategory} constant.
   * @throws IllegalArgumentException If the given object is not a Minecraft sound category.
   */
  SoundCategory fromMinecraftSoundCategory(Object soundCategory);

  /**
   * Retrieves a {@link Sound} by using the given Minecraft sound event.
   *
   * @param soundCategory The non-null minecraft sound event.
   * @return The sound category constant.
   */
  Object toMinecraftSoundCategory(SoundCategory soundCategory);
}
