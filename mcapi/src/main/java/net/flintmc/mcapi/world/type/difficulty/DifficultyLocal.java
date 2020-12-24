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

package net.flintmc.mcapi.world.type.difficulty;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;

/**
 * Represents the local difficulty of the Minecraft world.
 */
public interface DifficultyLocal {

  /**
   * Retrieves the global difficulty of this world.
   *
   * @return The global difficulty.
   */
  Difficulty getDifficulty();

  /**
   * Retrieves the additional difficulty of this world.
   *
   * @return The additional difficulty.
   */
  float getAdditionalDifficulty();

  /**
   * Whether if the current difficulty more difficult than the given difficulty.
   *
   * @param difficulty The difficulty to be checked.
   * @return {@code true} if the current difficulty more difficult than the given difficulty,
   * otherwise {@code false}.
   */
  boolean isHarderThan(float difficulty);

  /**
   * Retrieves the clamped additional difficulty of this world..
   *
   * @return The clamped additional difficulty.
   */
  float getClampedAdditionalDifficulty();

  /**
   * Calculates the additional difficulty of this world.
   *
   * @param difficulty         The world difficulty.
   * @param worldTime          The current world time.
   * @param chunkInhabitedTime The chunk inhabited time.
   * @param moonPhaseFactor    The factor of the moon phase.
   * @return The calculated additional difficulty.
   */
  float calculateAdditionalDifficulty(
      Difficulty difficulty, long worldTime, long chunkInhabitedTime, float moonPhaseFactor);

  /**
   * A factory class for {@link DifficultyLocal}.
   */
  @AssistedFactory(DifficultyLocal.class)
  interface Factory {

    /**
     * Creates a new {@link DifficultyLocal} with the given parameters.
     *
     * @param difficulty         The world difficulty.
     * @param worldTime          The current world time.
     * @param chunkInhabitedTime The chunk inhabited time.
     * @param moonPhaseFactor    The factory of the moon phase.
     * @return A created difficulty local.
     */
    DifficultyLocal create(
        @Assisted("difficulty") Difficulty difficulty,
        @Assisted("worldTime") long worldTime,
        @Assisted("chunkInhabitedTime") long chunkInhabitedTime,
        @Assisted("moonPhaseFactor") float moonPhaseFactor);
  }
}
