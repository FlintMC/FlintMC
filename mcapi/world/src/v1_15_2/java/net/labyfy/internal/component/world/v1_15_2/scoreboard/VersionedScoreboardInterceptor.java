package net.labyfy.internal.component.world.v1_15_2.scoreboard;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import net.labyfy.chat.MinecraftComponentMapper;
import net.labyfy.component.stereotype.type.Type;
import net.labyfy.component.transform.hook.Hook;
import net.labyfy.component.world.mapper.ScoreboardMapper;
import net.labyfy.component.world.scoreboad.Scoreboard;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;

/**
 * 1.15.2 implementation of the scoreboard interceptor.
 */
@Singleton
public class VersionedScoreboardInterceptor {

  private final Scoreboard scoreboard;
  private final ScoreboardMapper scoreboardMapper;

  @Inject
  public VersionedScoreboardInterceptor(
          Scoreboard scoreboard,
          ScoreboardMapper scoreboardMapper,
          MinecraftComponentMapper minecraftComponentMapper
  ) {
    this.scoreboard = scoreboard;
    this.scoreboardMapper = scoreboardMapper;
  }

  @Hook(className = "net.minecraft.scoreboard.Scoreboard",
          methodName = "addPlayerToTeam",
          parameters = {
                  @Type(reference = String.class),
                  @Type(reference = ScorePlayerTeam.class)
          }
  )
  public void hookAfterAddPlayerToTeam(@Named("args") Object[] args) {
    this.scoreboard.attachPlayerToTeam((String) args[0], this.scoreboardMapper.fromMinecraftPlayerTeam(args[1]));
  }

  @Hook(className = "net.minecraft.scoreboard.Scoreboard",
          methodName = "removePlayerFromTeam",
          parameters = {
                  @Type(reference = String.class),
                  @Type(reference = ScorePlayerTeam.class)
          }
  )
  public void hookAfterRemovePlayerFromTeam(@Named("args") Object[] args) {
    this.scoreboard.detachPlayerFromTeam((String) args[0], this.scoreboardMapper.fromMinecraftPlayerTeam(args[1]));
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
          methodName = "onTeamAdded",
          parameters = {
                  @Type(reference = ScorePlayerTeam.class)
          }
  )
  public void hookAfterTeamAdded(@Named("args") Object[] args) {
    this.scoreboard.addPlayerTeam(this.scoreboardMapper.fromMinecraftPlayerTeam(args[0]));
  }

  @Hook(
          className = "net.minecraft.scoreboard.Scoreboard",
          methodName = "onTeamChanged",
          parameters = {
                  @Type(reference = ScorePlayerTeam.class)
          }
  )
  public void hookAfterTeamChanged(@Named("args") Object[] args) {
    this.scoreboard.updatePlayerTeam(this.scoreboardMapper.fromMinecraftPlayerTeam(args[0]));
  }

  @Hook(
          className = "net.minecraft.scoreboard.Scoreboard",
          methodName = "onTeamRemoved",
          parameters = {
                  @Type(reference = ScorePlayerTeam.class)
          }
  )
  public void hookAfterTeamRemoved(@Named("args") Object[] args) {
    this.scoreboard.removePlayerTeam(this.scoreboardMapper.fromMinecraftPlayerTeam(args[0]));
  }

  @Hook(
          className = "net.minecraft.scoreboard.Scoreboard",
          methodName = "onPlayerRemoved",
          parameters = {
                  @Type(reference = String.class)
          }
  )
  public void hookAfterPlayerRemoved(@Named("args") Object[] args) {
    this.scoreboard.removePlayer((String) args[0]);
  }

  @Hook(
          className = "net.minecraft.scoreboard.Scoreboard",
          methodName = "onPlayerScoreRemoved",
          parameters = {
                  @Type(reference = String.class),
                  @Type(reference = ScoreObjective.class)
          }
  )
  public void hookAfterPlayerScoreRemoved(@Named("args") Object[] args) {
    this.scoreboard.removeScorePlayer((String) args[0], this.scoreboardMapper.fromMinecraftObjective(args[1]));
  }

}
