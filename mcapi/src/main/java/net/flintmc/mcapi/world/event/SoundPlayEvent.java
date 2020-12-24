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

package net.flintmc.mcapi.world.event;

import net.flintmc.framework.eventbus.event.Event;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe.Phase;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.player.type.sound.Sound;
import net.flintmc.mcapi.player.type.sound.SoundCategory;
import net.flintmc.mcapi.world.math.Vector3D;

/**
 * This event will be fired when a sound is played for this client. It will be fired in both the
 * {@link Phase#PRE} and {@link Phase#POST} phases.
 *
 * @see Subscribe
 */
public interface SoundPlayEvent extends Event {

  /**
   * Retrieves the position where the sound is being played.
   *
   * @return The non-null position where the sound is being played
   */
  Vector3D getPosition();

  /**
   * Retrieves the sound that is being played.
   *
   * @return The non-null sound that is being played
   */
  Sound getSound();

  /**
   * Retrieves the category where the played sound is from.
   *
   * @return The non-null category of the sound that is being played
   */
  SoundCategory getCategory();

  /**
   * Retrieves the volume of the sound to play.
   *
   * @return The volume of the sound to play, always > 0
   */
  float getVolume();

  /**
   * Retrieves the pitch of the sound to play.
   *
   * @return The pitch of the sound to play, always > 0
   */
  float getPitch();

  /**
   * Factory for the {@link SoundPlayEvent}.
   */
  @AssistedFactory(SoundPlayEvent.class)
  interface Factory {

    /**
     * Creates a new {@link SoundPlayEvent} with the given parameters.
     *
     * @param position The non-null position where the sound is being played
     * @param sound    The non-null sound that is being played
     * @param category The non-null category of the sound that is being played
     * @param volume   The volume of the sound to play, always > 0
     * @param pitch    The pitch of the sound to play, always > 0
     * @return The new non-null {@link SoundPlayEvent}
     */
    SoundPlayEvent create(
        @Assisted Vector3D position,
        @Assisted Sound sound,
        @Assisted SoundCategory category,
        @Assisted("volume") float volume,
        @Assisted("pitch") float pitch);
  }
}
