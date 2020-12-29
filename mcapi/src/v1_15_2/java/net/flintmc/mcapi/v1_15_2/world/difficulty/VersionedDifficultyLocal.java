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

package net.flintmc.mcapi.v1_15_2.world.difficulty;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.world.type.difficulty.Difficulty;
import net.flintmc.mcapi.world.type.difficulty.DifficultyLocal;
import net.minecraft.util.math.MathHelper;

/** 1.15.2 implementation of {@link DifficultyLocal}. */
@Implement(value = DifficultyLocal.class, version = "1.15.2")
public class VersionedDifficultyLocal implements DifficultyLocal {

  private final Difficulty worldDifficulty;
  private final float additionalDifficulty;

  @AssistedInject
  private VersionedDifficultyLocal(
      @Assisted("difficulty") Difficulty worldDifficulty,
      @Assisted("worldTime") long worldTime,
      @Assisted("chunkInhabitedTime") long chunkInhabitedTime,
      @Assisted("moonPhaseFactor") float moonPhaseFactor) {
    this.worldDifficulty = worldDifficulty;
    this.additionalDifficulty =
        this.calculateAdditionalDifficulty(
            worldDifficulty, worldTime, chunkInhabitedTime, moonPhaseFactor);
  }

  /** {@inheritDoc} */
  @Override
  public Difficulty getDifficulty() {
    return this.worldDifficulty;
  }

  /** {@inheritDoc} */
  @Override
  public float getAdditionalDifficulty() {
    return this.additionalDifficulty;
  }

  /** {@inheritDoc} */
  @Override
  public boolean isHarderThan(float difficulty) {
    return this.additionalDifficulty > difficulty;
  }

  /** {@inheritDoc} */
  @Override
  public float getClampedAdditionalDifficulty() {
    if (this.additionalDifficulty < 2.0F) {
      return 0.0F;
    } else {
      return this.additionalDifficulty > 4.0F ? 1.0F : (this.additionalDifficulty - 2.0F) / 2.0F;
    }
  }

  /** {@inheritDoc} */
  @Override
  public float calculateAdditionalDifficulty(
      Difficulty difficulty, long worldTime, long chunkInhabitedTime, float moonPhaseFactor) {
    if (difficulty == Difficulty.PEACEFUL) {
      return 0.0F;
    } else {
      // A flag for the hard difficulty
      boolean isHard = difficulty == Difficulty.HARD;
      // Default value for the additional difficulty
      float additionalDifficulty = 0.75F;
      // Calculates the clamped world time.
      float clampedWorld =
          MathHelper.clamp(((float) worldTime + -72000.0F) / 1440000.0F, 0.0F, 1.0F) * 0.25F;
      additionalDifficulty = additionalDifficulty + clampedWorld;

      // Calculates the clamped factor
      float clampedFactor = 0.0F;
      clampedFactor =
          clampedFactor
              + MathHelper.clamp((float) chunkInhabitedTime / 3600000.0F, 0.0F, 1.0F)
                  * (isHard ? 1.0F : 0.75F);
      clampedFactor = clampedFactor + MathHelper.clamp(moonPhaseFactor * 0.25F, 0.0F, clampedWorld);

      // Checks if the given difficulty EASY
      if (difficulty == Difficulty.EASY) {
        // Multiply the clamped factor by 0.5
        clampedFactor *= 0.5F;
      }

      additionalDifficulty = additionalDifficulty + clampedFactor;
      return (float) difficulty.getId() * additionalDifficulty;
    }
  }
}
