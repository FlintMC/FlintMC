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
