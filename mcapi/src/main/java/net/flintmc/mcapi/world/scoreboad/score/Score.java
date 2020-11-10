package net.flintmc.mcapi.world.scoreboad.score;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.world.scoreboad.Scoreboard;

/** Represents a minecraft score. */
public interface Score {

  /**
   * Retrieves the player name of this score.
   *
   * @return The player's name.
   */
  String getPlayerName();

  /**
   * Retrieves the objective of this score.
   *
   * @return The score's objective.
   */
  Objective getObjective();

  /**
   * Increases the score by the given amount.
   *
   * @param amount The amount ot increase the score.
   */
  void increaseScore(int amount);

  /** Increases the score by one. */
  void incrementScore();

  /**
   * Retrieves the points of this score.
   *
   * @return The score points
   */
  int getScorePoints();

  /**
   * Updates the points of this score.
   *
   * @param points The new score points.
   */
  void setScorePoints(int points);

  /** Resets the score points to {@code 0} */
  void reset();

  /**
   * Whether the score is locked.
   *
   * @return {@code true} if the score is locked, otherwise {@code false}
   */
  boolean locked();

  /**
   * Updates the state of the lock of this score.
   *
   * @param locked {@code true} if the score should be locked, otherwise {@code false}
   */
  void setLocked(boolean locked);

  /** A factory class for {@link Score} */
  @AssistedFactory(Score.class)
  interface Factory {

    /**
     * Creates a new {@link Score} with the given parameters.
     *
     * @param scoreboard The scoreboard for this score.
     * @param objective The objective for this score.
     * @param username The username for this score.
     * @return A created score.
     */
    Score create(
        @Assisted("scoreboard") Scoreboard scoreboard,
        @Assisted("objective") Objective objective,
        @Assisted("username") String username);

    /**
     * Creates a new {@link Score} with the given parameters.
     *
     * @param scoreboard The score for this score.
     * @param objective The objective for this score.
     * @param username The username for this score.
     * @param scorePoints The points for this score.
     * @return A created score.
     */
    Score create(
        @Assisted("scoreboard") Scoreboard scoreboard,
        @Assisted("objective") Objective objective,
        @Assisted("username") String username,
        @Assisted("score") int scorePoints);
  }

  /** Represents a service interface for creating {@link Score} */
  interface Provider {

    /**
     * Creates a new {@link Score} with given parameters.
     *
     * @param objective The objective for this score.
     * @param username The username for this score.
     * @return A created score.
     */
    Score get(Objective objective, String username);

    /**
     * Creates a new {@link Score} with given parameters.
     *
     * @param objective The objective for this score.
     * @param username The username for this score.
     * @param points The points for this score.
     * @return A created score.
     */
    Score get(Objective objective, String username, int points);
  }
}
