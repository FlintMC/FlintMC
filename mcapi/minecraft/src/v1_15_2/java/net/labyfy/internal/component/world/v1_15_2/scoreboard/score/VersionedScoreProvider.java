package net.labyfy.internal.component.world.v1_15_2.scoreboard.score;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.world.scoreboad.Scoreboard;
import net.labyfy.component.world.scoreboad.score.Objective;
import net.labyfy.component.world.scoreboad.score.Score;

/**
 * 1.15.2 implementation of {@link Score.Provider}.
 */
@Singleton
@Implement(value = Score.Provider.class, version = "1.15.2")
public class VersionedScoreProvider implements Score.Provider {

  private final Scoreboard scoreboard;
  private final Score.Factory scoreFactory;

  @Inject
  public VersionedScoreProvider(Scoreboard scoreboard, Score.Factory scoreFactory) {
    this.scoreboard = scoreboard;
    this.scoreFactory = scoreFactory;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Score get(Objective objective, String username) {
    return this.scoreFactory.create(scoreboard, objective, username);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Score get(Objective objective, String username, int points) {
    return this.scoreFactory.create(this.scoreboard, objective, username, points);
  }
}
