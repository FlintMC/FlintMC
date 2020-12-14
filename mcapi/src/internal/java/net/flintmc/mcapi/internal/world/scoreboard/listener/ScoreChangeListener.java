package net.flintmc.mcapi.internal.world.scoreboard.listener;

import net.flintmc.mcapi.world.scoreboad.score.Score;

public interface ScoreChangeListener {

  void changeScorePoints(Score score, int points);

  void changeLocked(Score score, boolean locked);
}
