/*
 * FlintMC
 * Copyright (C) 2020-2021 LabyMedia GmbH and contributors
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package net.flintmc.mcapi.world.scoreboad;

import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.world.scoreboad.score.Criteria;
import net.flintmc.mcapi.world.scoreboad.score.Objective;
import net.flintmc.mcapi.world.scoreboad.score.PlayerTeam;
import net.flintmc.mcapi.world.scoreboad.score.Score;
import net.flintmc.mcapi.world.scoreboad.type.RenderType;
import java.util.Collection;
import java.util.Map;

/**
 * Represents a Minecraft scoreboard.
 */
public interface Scoreboard {

  /**
   * Whether an objective is registered.
   *
   * @param name The name of the objective.
   * @return {@code true} if an objective is registered, otherwise {@code false}.
   */
  boolean hasObjective(String name);

  /**
   * Retrieves an {@link Objective}.
   *
   * @param name The name of an objective.
   * @return A registered {@link Objective} or {@code null}.
   */
  Objective getObjective(String name);

  /**
   * Adds an {@link Objective} to the scoreboard.
   *
   * @param name        The registry name of this objective.
   * @param criteria    The criteria of this objective.
   * @param displayName The name that is displayed.
   * @param renderType  The render type of this objective.
   * @return The added objective or {@code null}
   * @throws IllegalArgumentException If the name length is longer than 16 or the objective is
   *                                  already registered.
   */
  Objective addObjective(
      String name, Criteria criteria, ChatComponent displayName, RenderType renderType);

  /**
   * Retrieves or creates a new score.
   *
   * @param username  The non-null username to get the score for
   * @param objective The non-null objective to set the retrieved or created score
   * @return A retrieved or created score.
   * @throws IllegalArgumentException If the given username is longer than 40 characters
   */
  Score getOrCreateScore(String username, Objective objective);

  /**
   * Retrieves a score from this scoreboard.
   *
   * @param username  The non-null username to get the score for
   * @param objective The non-null objective to get the score from
   * @return The score or {@code null} if no score with the given name in the given objective exists
   * @throws IllegalArgumentException If the given username is longer than 40 characters
   */
  Score getScore(String username, Objective objective);

  /**
   * Retrieves an {@link Objective} in the given display slot.
   *
   * @param slot The display slot to get an {@link Objective}.
   * @return An {@link Objective} at the display slot.
   */
  Objective getObjectiveInDisplaySlot(int slot);

  /**
   * Removes the given objective from this scoreboard.
   *
   * @param objective The non-null objective to be removed
   */
  void removeObjective(Objective objective);

  /**
   * Changes an {@link Objective} at the given slot.
   *
   * @param slot      The slot to set the {@link Objective}.
   * @param objective The {@link Objective} to set.
   */
  void setObjectiveInDisplaySlot(int slot, Objective objective);

  /**
   * Retrieves a sorted score collection.
   *
   * @param objective The {@link Objective} to get the sorted collection.
   * @return A sorted score collection.
   */
  Collection<Score> getSortedScores(Objective objective);

  /**
   * Removes an objective from an entity.
   *
   * @param name      The name of the entity.
   * @param objective The objective to be removed.
   */
  void removeObjectiveFromEntity(String name, Objective objective);

  /**
   * Retrieves a key-value system from the given name.
   *
   * @param name The name to get the key-value system.
   * @return A key-value system.
   */
  Map<Objective, Score> getObjectivesForEntity(String name);

  /**
   * Adds a player to the team.
   *
   * @param username The username of the player to be added.
   * @param team     The team in which the player is to be added.
   * @return {@code true} if the player was added, otherwise {@code false}.
   */
  boolean addPlayerToTeam(String username, PlayerTeam team);

  /**
   * Removes a player from the team.
   *
   * @param username The username of the player to be removed.
   * @param team     The team in which the player is.
   * @return {@code true} if the player was removed, otherwise {@code false}.
   */
  void removePlayerFromTeam(String username, PlayerTeam team);

  /**
   * Removes a player from teams.
   *
   * @param username The username of the player to be removed.
   * @return {@code true} if the player was removed from teh teams, otherwise {@code false}.
   */
  boolean removePlayerFromTeams(String username);

  /**
   * Retrieves a player team from the given username.
   *
   * @param username The username of a player to get a team.
   * @return The player's team or {@code null}.
   */
  PlayerTeam getPlayerTeam(String username);

  /**
   * Creates a new {@link PlayerTeam} with the given name.
   *
   * @param name The registry name of this team.
   * @return A created team or an already registered team.
   */
  PlayerTeam createTeam(String name);

  /**
   * Removes a {@link PlayerTeam}
   *
   * @param team The player team to be removed.
   */
  void removeTeam(PlayerTeam team);

  /**
   * Retrieves a player team with the given name.
   *
   * @param name A name of a player team.
   * @return A registered player team or {@code null}.
   */
  PlayerTeam getTeam(String name);

  /**
   * Retrieves a collection with all registered team names.
   *
   * @return A collection with all registered team names.
   */
  Collection<String> getTeamNames();

  /**
   * Retrieves a collection with all registered teams.
   *
   * @return A collection with all registered teams.
   */
  Collection<PlayerTeam> getTeams();

  /**
   * Retrieves a collection with all registered objectives.
   *
   * @return A collection with all registered objectives.
   */
  Collection<Objective> getScoreObjectives();

  /**
   * Retrieves a collection with all registered score objective names.
   *
   * @return A collection with all registered score objective names.
   */
  Collection<String> getScoreObjectiveNames();

  /**
   * Retrieves a collection with all registered objective names.
   *
   * @return A collection with all registered objective names.
   */
  Collection<String> getObjectiveNames();

  /**
   * Clears all collections.
   */
  void invalidate();
}
