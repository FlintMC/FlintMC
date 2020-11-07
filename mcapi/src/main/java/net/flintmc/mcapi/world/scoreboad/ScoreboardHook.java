package net.flintmc.mcapi.world.scoreboad;

import net.flintmc.mcapi.world.scoreboad.score.Objective;
import net.flintmc.mcapi.world.scoreboad.score.PlayerTeam;

/** Represents a hook that is hooked in all methods that start with <b>on</b>. */
public interface ScoreboardHook {

  /**
   * Is called when an objective was added.
   *
   * @param objective The added objective.
   */
  void addObjective(Objective objective);

  /**
   * Is called when an objective was updated.
   *
   * @param objective The updated objective.
   */
  void updateObjective(Objective objective);

  /**
   * Is called when an objective was removed.
   *
   * @param objective The removed objective.
   */
  void removeObjective(Objective objective);

  /**
   * Is called when a player team has been added.
   *
   * @param team The added team.
   */
  void addPlayerTeam(PlayerTeam team);

  /**
   * Is called when a player team was updated.
   *
   * @param team The updated team.
   */
  void updatePlayerTeam(PlayerTeam team);

  /**
   * Is called when a player team was removed.
   *
   * @param team The removed team.
   */
  void removePlayerTeam(PlayerTeam team);

  /**
   * Is called when a player was attached to the team.
   *
   * @param username The username of the player.
   * @param team The team in which the player should be added.
   */
  void attachPlayerToTeam(String username, PlayerTeam team);

  /**
   * Is called when a player was detached from the team.
   *
   * @param username The username of the player.
   * @param team The team in which teh player should be removed.
   */
  void detachPlayerFromTeam(String username, PlayerTeam team);

  /**
   * Is called when a score has been removed from player.
   *
   * @param username The username of the player from whom a score was removed.
   */
  void removePlayer(String username);

  /**
   * Is called when a score has been removed by a player.
   *
   * @param username The username of the removing player..
   * @param objective The objective for the score.
   */
  void removeScorePlayer(String username, Objective objective);
}
