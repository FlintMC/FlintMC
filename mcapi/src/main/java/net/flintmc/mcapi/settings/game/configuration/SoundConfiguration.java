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

package net.flintmc.mcapi.settings.game.configuration;

import net.flintmc.framework.config.annotation.implemented.ImplementedConfig;
import net.flintmc.mcapi.chat.annotation.Component;
import net.flintmc.mcapi.player.type.sound.SoundCategory;
import net.flintmc.mcapi.settings.flint.annotation.ui.DefineCategory;
import net.flintmc.mcapi.settings.flint.options.numeric.Range;
import net.flintmc.mcapi.settings.flint.options.numeric.SliderSetting;

/**
 * Represents the sound configuration.
 */
@DefineCategory(
    name = "minecraft.settings.sounds",
    displayName = @Component(value = "options.sounds", translate = true))
@ImplementedConfig
public interface SoundConfiguration {

  /**
   * Retrieves the volume of the given sound category.
   *
   * @param soundCategory The sound category to get the volume.
   * @return The sound volume of the sound category in percent
   */
  @SliderSetting(@Range(max = 100))
  // percent
  float getSoundVolume(SoundCategory soundCategory);

  /**
   * Changes the volume of the given sound category.
   *
   * @param soundCategory The sound category to be changed in percent
   * @param volume        The new sound volume for the category
   */
  void setSoundVolume(SoundCategory soundCategory, float volume);
}
