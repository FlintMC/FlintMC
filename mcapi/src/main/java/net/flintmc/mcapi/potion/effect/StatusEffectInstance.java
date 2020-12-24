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

package net.flintmc.mcapi.potion.effect;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;

/**
 * Represents the Minecraft effect instance.
 */
public interface StatusEffectInstance {

  /**
   * Retrieves the effect of the foundation.
   *
   * @return The foundation's effect.
   */
  StatusEffect getPotion();

  /**
   * Retrieves the duration of the effect foundation.
   *
   * @return The effect foundation's duration.
   */
  int getDuration();

  /**
   * Retrieves the amplifier of the effect foundation.
   *
   * @return The effect foundation's amplifier.
   */
  int getAmplifier();

  /**
   * Whether the effect foundation is ambient.
   *
   * @return {@code true} if the effect foundation is ambient, otherwise {@code false}.
   */
  boolean isAmbient();

  /**
   * Whether the effect foundation shows particles.
   *
   * @return {@code true} if the effect foundation shows particles, otherwise {@code false}.
   */
  boolean doesShowParticles();

  /**
   * Whether the effect foundation shows an icon.
   *
   * @return {@code true} if the effect foundation shows an icon, otherwise {@code false}.
   */
  boolean isShowIcon();

  /**
   * Retrieves the effect name of the foundation.
   *
   * @return The foundation effect name.
   */
  String getEffectName();

  /**
   * Whether the duration of the effect foundation is maximal.
   *
   * @return {@code true} if the duration of the effect foundation is maximal, otherwise {@code
   * false}.
   */
  boolean isDurationMaximum();

  /**
   * Changes whether the duration of the effect foundation is maximal.
   *
   * @param maxDuration {@code true} if the duration of the effect foundation should be maximum,
   *                    otherwise {@code false}.
   */
  void setDurationMaximum(boolean maxDuration);

  /**
   * Updates the duration of the status effect.
   *
   * @return {@code true} if the duration of the status effect is greater than {@code 0}, otherwise
   * {@code false}.
   */
  boolean update();

  /**
   * A factory class for creating {@link StatusEffectInstance}'s.
   */
  @AssistedFactory(StatusEffectInstance.class)
  interface Factory {

    /**
     * Creates a new {@link StatusEffectInstance} with the given parameters.
     *
     * @param potion          The potion effect of the foundation.
     * @param duration        The duration of the foundation.
     * @param amplifier       The amplifier of the foundation.
     * @param ambient         {@code true} if the foundation ambient, otherwise {@code false}.
     * @param durationMaximum {@code true} if the duration maximum, otherwise {@code false}.
     * @param showParticles   {@code true} if the foundation should be show particles, otherwise
     *                        {@code false}.
     * @param showIcon        {@code true} if the foundation should be show an icon, otherwise
     *                        {@code false}.
     * @return A created effect foundation.
     */
    StatusEffectInstance create(
        @Assisted("potion") StatusEffect potion,
        @Assisted("duration") int duration,
        @Assisted("amplifier") int amplifier,
        @Assisted("ambient") boolean ambient,
        @Assisted("durationMaximum") boolean durationMaximum,
        @Assisted("showParticles") boolean showParticles,
        @Assisted("showIcon") boolean showIcon);
  }
}
