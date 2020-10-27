package net.flintmc.mcapi.v1_15_2.world.scoreboard.score;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.world.scoreboad.Scoreboard;
import net.flintmc.mcapi.world.scoreboad.score.Objective;
import net.flintmc.mcapi.world.scoreboad.score.Score;

/** 1.15.2 implementation of {@link Score} */
@Implement(value = Score.class, version = "1.15.2")
public class VersionedScore implements Score {

  private final Scoreboard scoreboard;
  private final Objective objective;
  private final String username;

  private int scorePoints;
  private boolean locked;
  private boolean forceUpdate;

  @AssistedInject
  private VersionedScore(
      @Assisted("scoreboard") Scoreboard scoreboard,
      @Assisted("objective") Objective objective,
      @Assisted("username") String username) {
    this.scoreboard = scoreboard;
    this.objective = objective;
    this.username = username;
  }

  @AssistedInject
  private VersionedScore(
      @Assisted("scoreboard") Scoreboard scoreboard,
      @Assisted("objective") Objective objective,
      @Assisted("username") String username,
      @Assisted("score") int scorePoints) {
    this.scoreboard = scoreboard;
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
    if (this.scorePoints != points || this.forceUpdate) {
      this.forceUpdate = false;
    }
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
  }
}
