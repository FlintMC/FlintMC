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

package net.flintmc.mcapi.v1_16_5.settings.game.configuration;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.config.annotation.implemented.ConfigImplementation;
import net.flintmc.mcapi.player.type.sound.SoundCategory;
import net.flintmc.mcapi.player.type.sound.SoundMapper;
import net.flintmc.mcapi.settings.game.configuration.SoundConfiguration;
import net.minecraft.client.Minecraft;

/**
 * 1.16.5 implementation of {@link SoundConfiguration}.
 */
@Singleton
@ConfigImplementation(value = SoundConfiguration.class, version = "1.16.5")
public class VersionedSoundConfiguration implements SoundConfiguration {

  private final SoundMapper soundMapper;

  @Inject
  private VersionedSoundConfiguration(SoundMapper soundMapper) {
    this.soundMapper = soundMapper;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getSoundVolume(SoundCategory soundCategory) {
    return Minecraft.getInstance().gameSettings.getSoundLevel(
        (net.minecraft.util.SoundCategory)
            this.soundMapper.toMinecraftSoundCategory(soundCategory)) * 100F;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setSoundVolume(SoundCategory soundCategory, float volume) {
    Minecraft.getInstance().gameSettings.setSoundLevel(
        (net.minecraft.util.SoundCategory)
            this.soundMapper.toMinecraftSoundCategory(soundCategory),
        volume / 100F);
    Minecraft.getInstance().gameSettings.saveOptions();
  }
}
