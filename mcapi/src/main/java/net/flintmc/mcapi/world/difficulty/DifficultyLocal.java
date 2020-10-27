package net.flintmc.mcapi.world.difficulty;

import com.google.inject.assistedinject.Assisted;
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
   * @return {@code true} if the current difficulty more difficult than the given difficulty, otherwise {@code false}.
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
          Difficulty difficulty,
          long worldTime,
          long chunkInhabitedTime,
          float moonPhaseFactor
  );

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
            @Assisted("moonPhaseFactor") float moonPhaseFactor
    );

  }

}
