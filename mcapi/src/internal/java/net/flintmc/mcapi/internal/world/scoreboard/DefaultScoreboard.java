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

package net.flintmc.mcapi.internal.world.scoreboard;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.world.scoreboad.Scoreboard;
import net.flintmc.mcapi.world.scoreboad.score.Criteria;
import net.flintmc.mcapi.world.scoreboad.score.Objective;
import net.flintmc.mcapi.world.scoreboad.score.Objective.Factory;
import net.flintmc.mcapi.world.scoreboad.score.PlayerTeam;
import net.flintmc.mcapi.world.scoreboad.score.Score;
import net.flintmc.mcapi.world.scoreboad.type.RenderType;

@Singleton
@Implement(Scoreboard.class)
public class DefaultScoreboard implements Scoreboard {

  public static final int DISPLAY_LIMIT = 19;
  public static final Comparator<Score> SCORE_COMPARATOR =
      (scoreOne, scoreTwo) -> {
        if (scoreOne.getScorePoints() > scoreOne.getScorePoints()) {
          return 1;
        } else {
          return scoreOne.getScorePoints() < scoreTwo.getScorePoints()
              ? -1
              : scoreTwo.getPlayerName().compareToIgnoreCase(scoreOne.getPlayerName());
        }
      };

  private final Map<String, Objective> objectives;
  private final Map<Criteria, List<Objective>> scoreCriterias;
  private final Map<String, Map<Objective, Score>> entitiesScores;
  private final Map<String, PlayerTeam> teams;
  private final Map<String, PlayerTeam> teamMembers;
  private final Objective.Factory objectiveFactory;
  private final PlayerTeam.Factory playerTeamFactory;
  private final Score.Factory scoreFactory;
  private Objective[] displayObjectives;

  @Inject
  public DefaultScoreboard(
      Score.Factory scoreFactory, Factory objectiveFactory, PlayerTeam.Factory playerTeamFactory) {
    this.scoreFactory = scoreFactory;
    this.objectiveFactory = objectiveFactory;
    this.playerTeamFactory = playerTeamFactory;
    this.objectives = Maps.newHashMap();
    this.scoreCriterias = Maps.newHashMap();
    this.entitiesScores = Maps.newHashMap();
    this.teams = Maps.newHashMap();
    this.teamMembers = Maps.newHashMap();

    this.displayObjectives = new Objective[DISPLAY_LIMIT];
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasObjective(String name) {
    return this.objectives.containsKey(name);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Objective getObjective(String name) {
    return this.objectives.get(name);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Objective addObjective(
      String name, Criteria criteria, ChatComponent displayName, RenderType renderType) {

    if (name.length() > 16) {
      throw new IllegalArgumentException(
          String.format("The objective name \"%s\" is too long!", name));
    } else if (this.hasObjective(name)) {
      throw new IllegalArgumentException(
          String.format("An objective with the name \"%s\" already exists!", name));
    } else {
      Objective objective = this.objectiveFactory.create(name, displayName, criteria, renderType);
      this.scoreCriterias
          .computeIfAbsent(criteria, function -> Lists.newArrayList())
          .add(objective);
      this.objectives.put(name, objective);
      return objective;
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Score getOrCreateScore(String username, Objective objective) {
    if (username.length() > 40) {
      throw new IllegalArgumentException(
          String.format("The player name \"%s\" is too long!", username));
    } else {
      Map<Objective, Score> objectiveScores =
          this.entitiesScores.computeIfAbsent(username, function -> Maps.newHashMap());
      return objectiveScores.computeIfAbsent(
          objective, function -> this.scoreFactory.create(objective, username, 0));
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Objective getObjectiveInDisplaySlot(int slot) {
    return this.displayObjectives[slot];
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removeObjective(Objective objective) {
    this.objectives.remove(objective.getName());

    for (int slot = 0; slot < DISPLAY_LIMIT; ++slot) {
      if (this.getObjectiveInDisplaySlot(slot) == objective) {
        this.setObjectiveInDisplaySlot(slot, null);
      }
    }

    List<Objective> objectives = this.scoreCriterias.get(objective.getCriteria());

    if (objectives != null) {
      objectives.remove(objective);
    }

    for (Map<Objective, Score> value : this.entitiesScores.values()) {
      value.remove(objective);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setObjectiveInDisplaySlot(int slot, Objective objective) {
    this.displayObjectives[slot] = objective;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Collection<Score> getSortedScores(Objective objective) {
    List<Score> scores = Lists.newArrayList();

    for (Map<Objective, Score> value : this.entitiesScores.values()) {
      Score score = value.get(objective);

      if (score != null) {
        scores.add(score);
      }
    }

    scores.sort(SCORE_COMPARATOR);
    return scores;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removeObjectiveFromEntity(String name, Objective objective) {
    if (objective == null) {
      this.entitiesScores.remove(name);
    } else {
      Map<Objective, Score> objectiveScores = this.entitiesScores.get(name);

      if (objectiveScores != null) {
        objectiveScores.remove(objective);
        if (objectiveScores.size() < 1) {
          this.entitiesScores.remove(name);
        }
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Map<Objective, Score> getObjectivesForEntity(String name) {
    Map<Objective, Score> objectiveScores = this.entitiesScores.get(name);

    if (objectiveScores == null) {
      objectiveScores = Maps.newHashMap();
    }

    return objectiveScores;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean addPlayerToTeam(String username, PlayerTeam team) {
    if (username.length() > 40) {
      throw new IllegalArgumentException(String.format("The player name \"%s\" is too long!"));
    } else {
      if (this.getPlayerTeam(username) != null) {
        this.removePlayerFromTeams(username);
      }

      this.teamMembers.put(username, team);
      return team.getMembers().add(username);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removePlayerFromTeam(String username, PlayerTeam team) {
    if (this.getPlayerTeam(username) != team) {
      throw new IllegalStateException(
          String.format(
              "Player is either on another team or not on any team. Cannot remove from team \"%s\".",
              username));
    } else {
      this.teamMembers.remove(username);
      team.getMembers().remove(username);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean removePlayerFromTeams(String username) {
    PlayerTeam playerTeam = this.getPlayerTeam(username);

    if (playerTeam != null) {
      this.removePlayerFromTeam(username, playerTeam);
      return true;
    } else {
      return false;
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public PlayerTeam getPlayerTeam(String username) {
    return this.teamMembers.get(username);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public PlayerTeam createTeam(String name) {
    if (name.length() > 16) {
      throw new IllegalArgumentException(String.format("The team name \"%s\" is too long!", name));
    } else {
      PlayerTeam playerTeam = this.getTeam(name);

      if (playerTeam != null) {
        throw new IllegalArgumentException(
            String.format("A team with the name \"%s\" already exists!", name));
      } else {
        playerTeam = this.playerTeamFactory.create(name);
        this.teams.put(name, playerTeam);
        return playerTeam;
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removeTeam(PlayerTeam team) {
    this.teams.remove(team.getName());

    for (String member : team.getMembers()) {
      this.teamMembers.remove(member);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public PlayerTeam getTeam(String name) {
    return this.teams.get(name);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Collection<String> getTeamNames() {
    return this.teams.keySet();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Collection<PlayerTeam> getTeams() {
    return this.teams.values();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Collection<Objective> getScoreObjectives() {
    return this.objectives.values();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Collection<String> getScoreObjectiveNames() {
    return this.objectives.keySet();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Collection<String> getObjectiveNames() {
    return new ArrayList<>(this.entitiesScores.keySet());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void invalidate() {
    this.objectives.clear();
    this.scoreCriterias.clear();
    this.entitiesScores.clear();
    this.displayObjectives = new Objective[DISPLAY_LIMIT];
    this.teams.clear();
    this.teamMembers.clear();
  }
}
