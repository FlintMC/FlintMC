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

package net.flintmc.mcapi.internal.world.event;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.player.type.sound.Sound;
import net.flintmc.mcapi.player.type.sound.SoundCategory;
import net.flintmc.mcapi.world.event.SoundPlayEvent;
import net.flintmc.mcapi.world.math.Vector3D;

/** {@inheritDoc} */
@Implement(SoundPlayEvent.class)
public class DefaultSoundPlayEvent implements SoundPlayEvent {

  private final Vector3D position;
  private final Sound sound;
  private final SoundCategory category;
  private final float volume;
  private final float pitch;

  @AssistedInject
  public DefaultSoundPlayEvent(
      @Assisted Vector3D position,
      @Assisted Sound sound,
      @Assisted SoundCategory category,
      @Assisted("volume") float volume,
      @Assisted("pitch") float pitch) {
    this.position = position;
    this.sound = sound;
    this.category = category;

    this.volume = volume;
    this.pitch = pitch;
  }

  /** {@inheritDoc} */
  @Override
  public Vector3D getPosition() {
    return this.position;
  }

  /** {@inheritDoc} */
  @Override
  public Sound getSound() {
    return this.sound;
  }

  /** {@inheritDoc} */
  @Override
  public SoundCategory getCategory() {
    return this.category;
  }

  /** {@inheritDoc} */
  @Override
  public float getVolume() {
    return this.volume;
  }

  /** {@inheritDoc} */
  @Override
  public float getPitch() {
    return this.pitch;
  }
}
