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

package net.flintmc.mcapi.internal.world.scoreboard.score;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.internal.world.scoreboard.listener.ScoreChangeListener;
import net.flintmc.mcapi.world.scoreboad.score.Objective;
import net.flintmc.mcapi.world.scoreboad.score.Score;

@Implement(Score.class)
public class DefaultScore implements Score {

  private final ScoreChangeListener scoreChangeListener;
  private final Objective objective;
  private final String username;

  private int scorePoints;
  private boolean locked;
  private boolean forceUpdate;

  @AssistedInject
  private DefaultScore(
      @Assisted("objective") Objective objective,
      @Assisted("username") String username,
      ScoreChangeListener scoreChangeListener) {
    this(scoreChangeListener, objective, username, 0);
  }

  @AssistedInject
  private DefaultScore(
      ScoreChangeListener scoreChangeListener,
      @Assisted("objective") Objective objective,
      @Assisted("username") String username,
      @Assisted("score") int scorePoints) {
    this.scoreChangeListener = scoreChangeListener;
    this.objective = objective;
    this.username = username;
    this.scorePoints = scorePoints;
  }

  /** {@inheritDoc} */
  @Override
  public String getPlayerName() {
    return this.username;
  }

  /** {@inheritDoc} */
  @Override
  public Objective getObjective() {
    return this.objective;
  }

  /** {@inheritDoc} */
  @Override
  public void increaseScore(int amount) {
    if (this.objective.getCriteria().readOnly()) {
      throw new IllegalStateException("Cannot modify read-only score!");
    } else {
      this.setScorePoints(this.getScorePoints() + amount);
    }
  }

  /** {@inheritDoc} */
  @Override
  public void incrementScore() {
    this.increaseScore(1);
  }

  /** {@inheritDoc} */
  @Override
  public int getScorePoints() {
    return this.scorePoints;
  }

  /** {@inheritDoc} */
  @Override
  public void setScorePoints(int points) {
    int sPoints = this.scorePoints;
    this.scorePoints = points;

    if (sPoints != points || this.forceUpdate) {
      this.forceUpdate = false;
    }

    this.scoreChangeListener.changeScorePoints(this, points);
  }

  /** {@inheritDoc} */
  @Override
  public void reset() {
    this.setScorePoints(0);
  }

  /** {@inheritDoc} */
  @Override
  public boolean locked() {
    return this.locked;
  }

  /** {@inheritDoc} */
  @Override
  public void setLocked(boolean locked) {
    this.locked = locked;
    this.scoreChangeListener.changeLocked(this, locked);
  }
}
