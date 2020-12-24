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

package net.flintmc.mcapi.world.scoreboad.score;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;

/**
 * Represents a minecraft score.
 */
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

  /**
   * Increases the score by one.
   */
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

  /**
   * Resets the score points to {@code 0}
   */
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

  /**
   * A factory class for {@link Score}
   */
  @AssistedFactory(Score.class)
  interface Factory {

    /**
     * Creates a new {@link Score} with the given parameters.
     *
     * @param objective The objective for this score.
     * @param username  The username for this score.
     * @return A created score.
     */
    Score create(@Assisted("objective") Objective objective, @Assisted("username") String username);

    /**
     * Creates a new {@link Score} with the given parameters.
     *
     * @param objective   The objective for this score.
     * @param username    The username for this score.
     * @param scorePoints The points for this score.
     * @return A created score.
     */
    Score create(
        @Assisted("objective") Objective objective,
        @Assisted("username") String username,
        @Assisted("score") int scorePoints);
  }
}
