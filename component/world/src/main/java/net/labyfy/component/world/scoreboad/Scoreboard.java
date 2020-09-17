package net.labyfy.component.world.scoreboad;

import net.labyfy.component.world.scoreboad.score.Objective;
import net.labyfy.component.world.scoreboad.score.PlayerTeam;
import net.labyfy.component.world.scoreboad.score.Score;

import java.util.Collection;

/**
 *
 */
public interface Scoreboard {

  void addTeam(PlayerTeam team);

  void updateTeam(PlayerTeam team);

  void removeTeam(PlayerTeam team);

  void addObjective(Objective objective);

  void updateObjective(Objective objective);

  void removeObjective(Objective objective);

  void scoreChanged(Score score);

  Collection<PlayerTeam> getTeams();

  Collection<Objective> getObjectives();
}
