package net.labyfy.internal.component.world.v1_15_2.scoreboard;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.processing.autoload.AutoLoad;
import net.labyfy.component.world.mapper.ScoreboardMapper;
import net.labyfy.component.world.scoreboad.Scoreboard;
import net.labyfy.component.world.scoreboad.score.Objective;
import net.labyfy.component.world.scoreboad.score.PlayerTeam;
import net.labyfy.component.world.scoreboad.score.Score;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
@AutoLoad
@Singleton
@Implement(value = Scoreboard.class, version = "1.15.2")
public class VersionedScoreboard implements Scoreboard {

  private final Map<String, PlayerTeam> teams;
  private final Map<String, Objective> objectives;
  private final ScoreboardMapper scoreboardMapper;


  @Inject
  public VersionedScoreboard(
          ScoreboardMapper scoreboardMapper
  ) {
    this.teams = new HashMap<>();
    this.objectives = new HashMap<>();
    this.scoreboardMapper = scoreboardMapper;
  }

  @Override
  public void addTeam(PlayerTeam team) {
    if (!this.teams.containsKey(team.getName())) {
      this.teams.put(team.getName(), team);
    }
  }

  @Override
  public void updateTeam(PlayerTeam team) {
    this.teams.put(team.getName(), team);
  }

  @Override
  public void removeTeam(PlayerTeam team) {
    this.teams.remove(team.getName());
  }

  @Override
  public void addObjective(Objective objective) {
    if (!this.objectives.containsKey(objective.getName())) {
      this.objectives.put(objective.getName(), objective);
    }
  }

  @Override
  public void updateObjective(Objective objective) {
    this.objectives.put(objective.getName(), objective);
  }

  @Override
  public void removeObjective(Objective objective) {
    this.objectives.remove(objective.getName());
  }

  @Override
  public void scoreChanged(Score score) {

  }

  @Override
  public Collection<PlayerTeam> getTeams() {
    return this.teams.values();
  }

  @Override
  public Collection<Objective> getObjectives() {
    return this.objectives.values();
  }


}
