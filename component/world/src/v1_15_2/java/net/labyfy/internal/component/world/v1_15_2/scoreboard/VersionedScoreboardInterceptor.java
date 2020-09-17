package net.labyfy.internal.component.world.v1_15_2.scoreboard;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import net.labyfy.chat.MinecraftComponentMapper;
import net.labyfy.component.processing.autoload.AutoLoad;
import net.labyfy.component.stereotype.type.Type;
import net.labyfy.component.transform.hook.Hook;
import net.labyfy.component.world.mapper.ScoreboardMapper;
import net.labyfy.component.world.scoreboad.Scoreboard;
import net.labyfy.component.world.scoreboad.score.PlayerTeam;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;

/**
 *
 */
@Singleton
@AutoLoad
public class VersionedScoreboardInterceptor {

  private final Scoreboard scoreboard;
  private final ScoreboardMapper scoreboardMapper;
  private final MinecraftComponentMapper minecraftComponentMapper;

  @Inject
  public VersionedScoreboardInterceptor(
          Scoreboard scoreboard,
          ScoreboardMapper scoreboardMapper,
          MinecraftComponentMapper minecraftComponentMapper
  ) {
    this.scoreboard = scoreboard;
    this.scoreboardMapper = scoreboardMapper;
    this.minecraftComponentMapper = minecraftComponentMapper;
  }

  @Hook(
          className = "net.minecraft.scoreboard.Scoreboard",
          methodName = "onObjectiveAdded",
          parameters = {
                  @Type(reference = ScoreObjective.class)
          }
  )
  public void hookAfterObjectiveAdded(@Named("args") Object[] args) {
    this.scoreboard.addObjective(this.scoreboardMapper.fromMinecraftObjective(args[0]));
  }

  @Hook(
          className = "net.minecraft.scoreboard.Scoreboard",
          methodName = "onObjectiveChanged",
          parameters = {
                  @Type(reference = ScoreObjective.class)
          }
  )
  public void hookAfterObjectiveChanged(@Named("args") Object[] args) {
    this.scoreboard.updateObjective(this.scoreboardMapper.fromMinecraftObjective(args[0]));
  }

  @Hook(
          className = "net.minecraft.scoreboard.Scoreboard",
          methodName = "onObjectiveRemoved",
          parameters = {
                  @Type(reference = ScoreObjective.class)
          }
  )
  public void hookAfterObjectiveRemoved(@Named("args") Object[] args) {
    this.scoreboard.removeObjective(this.scoreboardMapper.fromMinecraftObjective(args[0]));
  }

  @Hook(
          className = "net.minecraft.scoreboard.Scoreboard",
          methodName = "onScoreChanged",
          parameters = {
                  @Type(reference = Score.class)
          }
  )
  public void hookAfterScoreChanged(@Named("args") Object[] args) {
    this.scoreboard.scoreChanged(this.scoreboardMapper.fromMinecraftScore(args[0]));
  }

  @Hook(
          className = "net.minecraft.scoreboard.Scoreboard",
          methodName = "onTeamAdded",
          parameters = {
                  @Type(reference = ScorePlayerTeam.class)
          }
  )
  public void hookAfterTeamAdded(@Named("args") Object[] args) {
    this.scoreboard.addTeam(this.scoreboardMapper.fromMinecraftPlayerTeam(args[0]));
  }

  @Hook(
          className = "net.minecraft.scoreboard.Scoreboard",
          methodName = "onTeamChanged",
          parameters = {
                  @Type(reference = ScorePlayerTeam.class)
          }
  )
  public void hookAfterTeamChanged(@Named("args") Object[] args) {
    this.scoreboard.updateTeam(this.scoreboardMapper.fromMinecraftPlayerTeam(args[0]));
  }

  @Hook(
          className = "net.minecraft.scoreboard.Scoreboard",
          methodName = "onTeamRemoved",
          parameters = {
                  @Type(reference = ScorePlayerTeam.class)
          }
  )
  public void hookAfterTeamRemoved(@Named("args") Object[] args) {
    this.scoreboard.removeTeam(this.scoreboardMapper.fromMinecraftPlayerTeam(args[0]));
  }


}
