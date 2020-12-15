package net.flintmc.mcapi.v1_15_2.world.scoreboard.listener;

import com.google.inject.Inject;
import java.util.Optional;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.internal.world.scoreboard.listener.ScoreChangeListener;
import net.flintmc.mcapi.world.mapper.ScoreboardMapper;
import net.flintmc.mcapi.world.scoreboad.score.Score;
import net.minecraft.client.Minecraft;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;

@Implement(value = ScoreChangeListener.class, version = "1.15.2")
public class VersionedScoreChangeListener implements ScoreChangeListener {

  private final ScoreboardMapper scoreboardMapper;

  @Inject
  private VersionedScoreChangeListener(ScoreboardMapper scoreboardMapper) {
    this.scoreboardMapper = scoreboardMapper;
  }

  /** {@inheritDoc} */
  @Override
  public void changeScorePoints(Score score, int points) {
    this.getScore(score).ifPresent(s -> s.setScorePoints(points));
  }

  /** {@inheritDoc} */
  @Override
  public void changeLocked(Score score, boolean locked) {
    this.getScore(score).ifPresent(s -> s.setLocked(locked));
  }

  private Optional<net.minecraft.scoreboard.Score> getScore(Score score) {
    Scoreboard scoreboard = Minecraft.getInstance().world.getScoreboard();

    return scoreboard == null
        ? Optional.empty()
        : Optional.ofNullable(
            scoreboard.getOrCreateScore(
                score.getPlayerName(),
                (ScoreObjective) this.scoreboardMapper.toMinecraftObjective(score.getObjective())));
  }
}
