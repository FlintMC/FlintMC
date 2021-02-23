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

import net.flintmc.mcapi.chat.annotation.Component;
import net.flintmc.mcapi.settings.flint.annotation.ui.DisplayName;
import net.flintmc.mcapi.settings.flint.annotation.ui.ForceFullWidth;
import net.flintmc.mcapi.settings.flint.annotation.version.VersionOnly;

/**
 * An enumeration of all available sound categories.
 */
public enum SoundCategory {

  /**
   * The master sound category.
   */
  @ForceFullWidth
  @DisplayName(@Component(value = "soundCategory.master", translate = true))
  MASTER,
  /**
   * The music sound category.
   */
  @DisplayName(@Component(value = "soundCategory.music", translate = true))
  MUSIC,
  /**
   * The record sound category.
   */
  @DisplayName(@Component(value = "soundCategory.record", translate = true))
  RECORD,
  /**
   * The weather sound category.
   */
  @DisplayName(@Component(value = "soundCategory.weather", translate = true))
  WEATHER,
  /**
   * The block sound category.
   */
  @DisplayName(@Component(value = "soundCategory.block", translate = true))
  BLOCK,
  /**
   * The hostile sound category.
   */
  @DisplayName(@Component(value = "soundCategory.hostile", translate = true))
  HOSTILE,
  /**
   * The neutral sound category.
   */
  @DisplayName(@Component(value = "soundCategory.neutral", translate = true))
  NEUTRAL,
  /**
   * The player sound category.
   */
  @DisplayName(@Component(value = "soundCategory.player", translate = true))
  PLAYER,
  /**
   * The ambient sound category.
   */
  @DisplayName(@Component(value = "soundCategory.ambient", translate = true))
  AMBIENT,
  /**
   * The voice sound category.
   */
  @DisplayName(@Component(value = "soundCategory.voice", translate = true))
  @VersionOnly({"1.15.2", "1.16.5"})
  VOICE;

  /**
   * Retrieves the name of this sound category.
   *
   * @return The name of this sound category.
   */
  public String getName() {
    return this.name().toLowerCase();
  }
}
