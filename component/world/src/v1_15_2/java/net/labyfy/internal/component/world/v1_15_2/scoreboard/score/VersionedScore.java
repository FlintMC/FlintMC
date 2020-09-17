package net.labyfy.internal.component.world.v1_15_2.scoreboard.score;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.processing.autoload.AutoLoad;
import net.labyfy.component.world.scoreboad.Scoreboard;
import net.labyfy.component.world.scoreboad.score.Objective;
import net.labyfy.component.world.scoreboad.score.Score;

/**
 * 1.15.2 implementation of {@link Score}
 */
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
          @Assisted("username") String username
  ) {
    this.scoreboard = scoreboard;
    this.objective = objective;
    this.username = username;
  }

  @Override
  public String getPlayerName() {
    return this.username;
  }

  @Override
  public Objective getObjective() {
    return this.objective;
  }

  @Override
  public void increaseScore(int amount) {
    if (this.objective.getCriteria().readOnly()) {
      throw new IllegalStateException("Cannot modify read-only score!");
    } else {
      this.setScorePoints(this.getScorePoints() + amount);
    }
  }

  @Override
  public void incrementScore() {
    this.increaseScore(1);
  }

  @Override
  public int getScorePoints() {
    return this.scorePoints;
  }

  @Override
  public void setScorePoints(int points) {
    int scorePoints = this.scorePoints;
    this.scorePoints = scorePoints;

    if (scorePoints != points || this.forceUpdate) {
      this.forceUpdate = false;
    }
  }

  @Override
  public void reset() {
    this.setScorePoints(0);
  }

  @Override
  public boolean locked() {
    return this.locked;
  }

  @Override
  public void setLocked(boolean locked) {
    this.locked = locked;
  }

}
