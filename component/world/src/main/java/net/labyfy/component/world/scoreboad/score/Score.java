package net.labyfy.component.world.scoreboad.score;

import com.google.inject.assistedinject.Assisted;
import net.labyfy.component.inject.assisted.AssistedFactory;
import net.labyfy.component.world.scoreboad.Scoreboard;

/**
 *
 */
public interface Score {

  String getPlayerName();

  Objective getObjective();

  void increaseScore(int amount);

  void incrementScore();

  int getScorePoints();

  void setScorePoints(int points);

  void reset();

  boolean locked();

  void setLocked(boolean locked);

  @AssistedFactory(Score.class)
  interface Factory {

    Score create(
            @Assisted("scoreboard") Scoreboard scoreboard,
            @Assisted("objective") Objective objective,
            @Assisted("username") String username
    );

  }

  interface Provider {

    Score get(Objective objective, String username);

  }

}
