package net.flintmc.mcapi.v1_15_2.world.scoreboard;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.chat.MinecraftComponentMapper;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.world.mapper.ScoreboardMapper;
import net.flintmc.mcapi.world.scoreboad.Scoreboard;
import net.flintmc.mcapi.world.scoreboad.score.Criteria;
import net.flintmc.mcapi.world.scoreboad.score.Objective;
import net.flintmc.mcapi.world.scoreboad.score.PlayerTeam;
import net.flintmc.mcapi.world.scoreboad.score.Score;
import net.flintmc.mcapi.world.scoreboad.type.RenderType;
import net.minecraft.client.Minecraft;
import net.minecraft.scoreboard.ScoreCriteria;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.util.text.ITextComponent;

import java.util.*;

/** 1.15.2 implementation of {@link Scoreboard} */
@Singleton
@Implement(value = Scoreboard.class, version = "1.15.2")
public class VersionedScoreboard implements Scoreboard {

  private final ScoreboardMapper scoreboardMapper;
  private final MinecraftComponentMapper componentMapper;
  private final Objective.Provider objectiveProvider;
  private final PlayerTeam.Provider playerTeamProvider;

  private final Map<String, Objective> objectives;
  private final Map<String, Map<Objective, Score>> entitiesScoreObjectives;
  private final Map<Criteria, List<Objective>> scoreObjectiveCriterias;
  private final Map<String, PlayerTeam> teams;
  private final Map<String, PlayerTeam> teamMemberships;

  @Inject
  public VersionedScoreboard(
      ScoreboardMapper scoreboardMapper,
      MinecraftComponentMapper componentMapper,
      Objective.Provider objectiveProvider,
      PlayerTeam.Provider playerTeamProvider) {
    this.scoreboardMapper = scoreboardMapper;
    this.componentMapper = componentMapper;
    this.objectiveProvider = objectiveProvider;
    this.playerTeamProvider = playerTeamProvider;

    this.objectives = new HashMap<>();
    this.entitiesScoreObjectives = new HashMap<>();
    this.scoreObjectiveCriterias = new HashMap<>();

    this.teams = new HashMap<>();
    this.teamMemberships = new HashMap<>();
  }

  /** {@inheritDoc} */
  @Override
  public boolean hasObjective(String name) {
    return this.objectives.containsKey(name);
  }

  /**
   * Whether an objective is registered by Minecraft.
   *
   * @param name The name of the objective.
   * @return {@code true} if an objective is registered, otherwise {@code false}.
   */
  private boolean hasMinecraftObjective(String name) {
    return Minecraft.getInstance().world.getScoreboard().hasObjective(name);
  }

  /** {@inheritDoc} */
  @Override
  public Objective getObjective(String name) {
    Objective objective = this.objectives.get(name);

    if (objective == null) {
      objective =
          this.scoreboardMapper.fromMinecraftObjective(
              Minecraft.getInstance().world.getScoreboard().getObjective(name));
    }

    return objective;
  }

  /** {@inheritDoc} */
  @Override
  public Objective addObjective(
      String name, Criteria criteria, ChatComponent displayName, RenderType renderType) {
    return this.addObjective(name, criteria, displayName, renderType, false);
  }

  /**
   * Adds an objective to the scoreboard.
   *
   * @param name The registry name of this objective.
   * @param criteria The criteria of this objective.
   * @param displayName The name that is displayed.
   * @param renderType The render type of this objective.
   * @param minecraft {@code true} if called from Minecraft, otherwise {@code false}
   * @return The added objective or {@code null}
   * @throws IllegalArgumentException If the name length is longer than 16 or the objective is
   *     already registered.
   */
  private Objective addObjective(
      String name,
      Criteria criteria,
      ChatComponent displayName,
      RenderType renderType,
      boolean minecraft) {
    if (name.length() > 16) {
      throw new IllegalArgumentException("The objective name \"" + name + "\" is too long!");
    } else if (minecraft ? this.hasMinecraftObjective(name) : this.hasObjective(name)) {

      // Checks if it is called by the Flint API
      if (!minecraft) {

        // Checks if the given name is already registered in the Minecraft scoreboard.
        // When the objective is not registered, add the objective to the Minecraft scoreboard
        if (this.hasMinecraftObjective(name)) {
          throw new IllegalArgumentException(
              "An objective with the name \"" + name + "\" already exists");
        } else {
          this.addMinecraftObjective(name, criteria, displayName, renderType);
        }
      }

      return null;
    } else {
      Objective objective =
          this.objectiveProvider.get(this, name, displayName, criteria, renderType);
      this.scoreObjectiveCriterias
          .computeIfAbsent(criteria, criteriaAbsent -> new ArrayList<>())
          .add(objective);

      // Checks if it is called by the Flint API.
      // When is called from the Flint API, add the new objective to the Minecraft scoreboard.
      if (!minecraft) this.addMinecraftObjective(name, criteria, displayName, renderType);

      // Puts the new objective to the key-value system
      this.objectives.put(name, objective);
      return objective;
    }
  }

  /**
   * Adds a Minecraft objective.
   *
   * @param name The registry name of this objective.
   * @param criteria The criteria of this objective.
   * @param displayName The name that is displayed.
   * @param renderType The render type of this objective.
   */
  private void addMinecraftObjective(
      String name, Criteria criteria, ChatComponent displayName, RenderType renderType) {
    Minecraft.getInstance()
        .world
        .getScoreboard()
        .addObjective(
            name,
            (ScoreCriteria) this.scoreboardMapper.toMinecraftCriteria(criteria),
            (ITextComponent) this.componentMapper.toMinecraft(displayName),
            ScoreCriteria.RenderType.valueOf(
                this.scoreboardMapper.toMinecraftRenderType(renderType)));
  }

  /** {@inheritDoc} */
  @Override
  public Score getOrCreateScore(String username, Objective objective) {
    // Retrieves or create a new Minecraft score
    net.minecraft.scoreboard.Score minecraftScore =
        this.getOrCreateMinecraftScore(username, objective);
    // Computes when the given username is absent
    Map<Objective, Score> entitiesScoreObjectives =
        this.entitiesScoreObjectives.computeIfAbsent(username, s -> new HashMap<>());

    if(entitiesScoreObjectives.containsKey(objective)) {
      return entitiesScoreObjectives.get(objective);
    } else {
      Score score = this.scoreboardMapper.fromMinecraftScore(minecraftScore);
      entitiesScoreObjectives.put(objective, score);
      return score;
    }
  }

  /**
   * Retrieves or creates a new Minecraft score.
   *
   * @param username The username to get the key-value system.
   * @param objective The objective to set the retrieved or created score.
   * @return A retreived or created score.
   */
  private net.minecraft.scoreboard.Score getOrCreateMinecraftScore(
      String username, Objective objective) {
    return Minecraft.getInstance()
        .world
        .getScoreboard()
        .getOrCreateScore(
            username, (ScoreObjective) this.scoreboardMapper.toMinecraftObjective(objective));
  }

  /** {@inheritDoc} */
  @Override
  public Objective getObjectiveInDisplaySlot(int slot) {
    return this.scoreboardMapper.fromMinecraftObjective(
        Minecraft.getInstance().world.getScoreboard().getObjectiveInDisplaySlot(slot));
  }

  /** {@inheritDoc} */
  @Override
  public void setObjectiveInDisplaySlot(int slot, Objective objective) {
    Minecraft.getInstance()
        .world
        .getScoreboard()
        .setObjectiveInDisplaySlot(
            slot, (ScoreObjective) this.scoreboardMapper.toMinecraftObjective(objective));
  }

  /** {@inheritDoc} */
  @Override
  public Collection<Score> getSortedScores(Objective objective) {
    // Retrieves the sorted score collection from Minecraft
    Collection<net.minecraft.scoreboard.Score> scores = this.getMinecraftSortedScore(objective);

    // Initializes a new array list
    List<Score> list = new ArrayList<>();

    // Iterated through the sorted collection.
    for (net.minecraft.scoreboard.Score score : scores) {

      // When the score is not null, add the score to the list
      if (score != null) {
        list.add(this.scoreboardMapper.fromMinecraftScore(score));
      }
    }

    // Sorts the list
    list.sort(
        (firstScore, secondScore) ->
            firstScore.getScorePoints() > secondScore.getScorePoints()
                ? 1
                : firstScore.getScorePoints() < secondScore.getScorePoints()
                    ? -1
                    : secondScore.getPlayerName().compareToIgnoreCase(firstScore.getPlayerName()));

    // Returns the sorted list
    return list;
  }

  private Collection<net.minecraft.scoreboard.Score> getMinecraftSortedScore(Objective objective) {
    return Minecraft.getInstance()
        .world
        .getScoreboard()
        .getSortedScores((ScoreObjective) this.scoreboardMapper.toMinecraftObjective(objective));
  }

  /** {@inheritDoc} */
  @Override
  public Map<Objective, Score> getObjectivesForEntity(String name) {
    Map<Objective, Score> map = this.entitiesScoreObjectives.get(name);

    // When the map is null, a new hash map is initialized
    if (map == null) {
      map = new HashMap<>();
    }

    return map;
  }

  /** {@inheritDoc} */
  @Override
  public boolean addPlayerToTeam(String username, PlayerTeam team) {
    return this.addPlayerToTeam(username, team, false);
  }

  /**
   * Adds a player to the team.
   *
   * @param username The username of the player to be added.
   * @param team The team in which the player is to be added.
   * @param minecraft {@code true} if called from Minecraft, otherwise {@code false}.
   * @return {@code true} if the player was added to the team, otherwise {@code false}.
   */
  private boolean addPlayerToTeam(String username, PlayerTeam team, boolean minecraft) {
    // When the name is longer than 40, an exception is thrown
    if (username.length() > 40) {
      throw new IllegalArgumentException("The player name \"" + username + "\" is too long!");
    } else {
      // Checks if the player in a team
      if (this.getPlayerTeam(username) != null) {
        // Removes the player from teams
        this.removePlayerFromTeams(username);
      }

      if (!minecraft) {
        Minecraft.getInstance()
            .world
            .getScoreboard()
            .addPlayerToTeam(
                username, (ScorePlayerTeam) this.scoreboardMapper.toMinecraftPlayerTeam(team));
      }

      this.teamMemberships.put(username, team);
      return team.getMembers().add(username);
    }
  }

  /** {@inheritDoc} */
  @Override
  public boolean removePlayerFromTeam(String username, PlayerTeam team) {
    return this.removePlayerFromTeam(username, team, false);
  }

  private boolean removePlayerFromTeam(String username, PlayerTeam team, boolean minecraft) {
    // Whether the given team is not equal to the player's team.
    if (!this.getPlayerTeam(username).getName().equals(team.getName())) {
      return false;
    } else {

      // Checks if it is called by the Flint API
      // Removes a team player from a team
      if (!minecraft) {
        ScorePlayerTeam playerTeam =
            (ScorePlayerTeam) this.scoreboardMapper.toMinecraftPlayerTeam(team);

        if (playerTeam != null) {
          Minecraft.getInstance().world.getScoreboard().removePlayerFromTeam(username, playerTeam);
        }
      }

      // Removes the player from the key-value system.
      this.teamMemberships.remove(username);
      // Removes the player from the member collection of the team.
      team.getMembers().remove(username);
      return true;
    }
  }

  /** {@inheritDoc} */
  @Override
  public boolean removePlayerFromTeams(String username) {
    PlayerTeam team = this.getPlayerTeam(username);
    return team != null && this.removePlayerFromTeam(username, team);
  }

  /** {@inheritDoc} */
  @Override
  public PlayerTeam getPlayerTeam(String username) {
    return this.teamMemberships.get(username);
  }

  /** {@inheritDoc} */
  @Override
  public PlayerTeam createTeam(String name) {
    return this.createTeam(name, false);
  }

  /**
   * Creates a new {@link PlayerTeam} with the given name.
   *
   * @param name The registry name of this team.
   * @param minecraft {@code true} if called from Minecraft, otherwise {@code false}
   * @return A created team or an already registered team.
   * @throws IllegalArgumentException If the name length longer than 16.
   */
  private PlayerTeam createTeam(String name, boolean minecraft) {
    if (!minecraft && name.length() > 16) {
      throw new IllegalArgumentException("The team name \"" + name + "\" is too long!");
    } else {
      // Checks if it called from Minecraft
      if (minecraft) {
        // Retrieves or creates a new team with the given name.
        return this.retrieveOrCreateTeam(name);
      } else {
        // Retrieves a registered team from Minecraft
        ScorePlayerTeam team = this.getMinecraftTeam(name);

        // Checks if the registered team from Minecraft null
        // When the registered team is null, creates or retrieves a
        // Flint team and creates a new Minecraft team.
        if (team == null) {

          // Retrieves or creates a Flint team
          PlayerTeam playerTeam = this.retrieveOrCreateTeam(name);
          // Creates a new Minecraft team
          Minecraft.getInstance().world.getScoreboard().createTeam(playerTeam.getName());
          return playerTeam;
        } else {
          // Retrieves or creates a new team with the given name.
          return this.retrieveOrCreateTeam(name);
        }
      }
    }
  }

  /**
   * Retrieves or creates a {@link PlayerTeam}
   *
   * @param name The registry name of the team
   * @return A created or retrieved team.
   */
  private PlayerTeam retrieveOrCreateTeam(String name) {
    PlayerTeam team = this.getTeam(name);

    if (team == null) {
      team = this.playerTeamProvider.get(name);

      this.teams.put(name, team);

      return team;
    }

    return team;
  }

  /**
   * Updates or creates a new {@link PlayerTeam}
   *
   * @param team The updated team.
   * @return A updated or created player team.
   */
  private void updateOrCreateTeam(PlayerTeam team) {
    PlayerTeam playerTeam = this.getTeam(team.getName());

    if (playerTeam == null) {
      this.teams.put(team.getName(), team);
      return;
    }

    playerTeam = team;
    this.teams.put(playerTeam.getName(), playerTeam);
  }

  /** {@inheritDoc} */
  @Override
  public void removeTeam(PlayerTeam team) {
    this.removeTeam(team, false);
  }

  /**
   * Removes a team.
   *
   * @param team The team to be remove
   * @param minecraft {@code true} if called from Minecraft, otherwise {@code false}
   */
  private void removeTeam(PlayerTeam team, boolean minecraft) {
    // Removes the team from the key-value system
    this.teams.remove(team.getName());

    // Removes all members of the removed team
    for (String member : team.getMembers()) {
      this.teamMemberships.remove(member);
    }

    // Checks if it is called by the Flint API
    if (!minecraft) {
      // Removes the team from the Miencraft scoreboard
      Minecraft.getInstance()
          .world
          .getScoreboard()
          .removeTeam((ScorePlayerTeam) this.scoreboardMapper.toMinecraftPlayerTeam(team));
    }
  }

  /** {@inheritDoc} */
  @Override
  public void addObjective(Objective objective) {
    this.addObjective(
        objective.getName(),
        objective.getCriteria(),
        objective.getDisplayName(),
        objective.getRenderType(),
        true);
  }

  /** {@inheritDoc} */
  @Override
  public void updateObjective(Objective objective) {
    this.objectives.put(objective.getName(), objective);
  }

  /** {@inheritDoc} */
  @Override
  public void removeObjective(Objective objective) {
    this.objectives.remove(objective.getName());
  }

  /** {@inheritDoc} */
  @Override
  public void addPlayerTeam(PlayerTeam team) {
    this.createTeam(team.getName(), true);
  }

  /** {@inheritDoc} */
  @Override
  public void updatePlayerTeam(PlayerTeam team) {
    if (this.teams.containsKey(team.getName())) {
      this.teams.put(team.getName(), team);
    } else {
      this.updateOrCreateTeam(team);
    }
  }

  /** {@inheritDoc} */
  @Override
  public void removePlayerTeam(PlayerTeam team) {
    this.removeTeam(team, true);
  }

  /** {@inheritDoc} */
  @Override
  public void attachPlayerToTeam(String username, PlayerTeam team) {
    this.addPlayerToTeam(username, team, true);
  }

  /** {@inheritDoc} */
  @Override
  public void detachPlayerFromTeam(String username, PlayerTeam team) {
    this.removePlayerFromTeam(username, team, true);
  }

  /** {@inheritDoc} */
  @Override
  public void removePlayer(String username) {
    this.entitiesScoreObjectives.remove(username);
  }

  /** {@inheritDoc} */
  @Override
  public void removeScorePlayer(String username, Objective objective) {
    Map<Objective, Score> map = this.entitiesScoreObjectives.get(username);

    if (map != null) {
      Score score = map.remove(objective);

      if (map.size() < 1) {
        this.entitiesScoreObjectives.remove(username);
      } else if (score != null) {
        map.remove(objective, score);
      }
    }
  }

  /** {@inheritDoc} */
  @Override
  public PlayerTeam getTeam(String name) {
    return this.teams.get(name);
  }

  /**
   * Retrieves a Minecraft {@link ScorePlayerTeam} with the given name.
   *
   * @return A Minecraft {@link ScorePlayerTeam} or {@code null}.
   */
  private ScorePlayerTeam getMinecraftTeam(String name) {
    return Minecraft.getInstance().world.getScoreboard().getTeam(name);
  }

  /** {@inheritDoc} */
  @Override
  public Collection<String> getTeamNames() {
    return this.teams.keySet();
  }

  /** {@inheritDoc} */
  @Override
  public Collection<PlayerTeam> getTeams() {
    return this.teams.values();
  }

  /** {@inheritDoc} */
  @Override
  public Collection<Objective> getScoreObjectives() {
    return this.objectives.values();
  }

  /** {@inheritDoc} */
  @Override
  public Collection<String> getScoreObjectiveNames() {
    return this.objectives.keySet();
  }

  /** {@inheritDoc} */
  @Override
  public Collection<String> getObjectiveNames() {
    return new ArrayList<>(this.entitiesScoreObjectives.keySet());
  }

  // TODO: 21.09.2020 Is called when the player is logged off the server

  /** {@inheritDoc} */
  @Override
  public void invalidate() {
    this.objectives.clear();
    this.entitiesScoreObjectives.clear();
    this.scoreObjectiveCriterias.clear();
    this.teams.clear();
    this.teamMemberships.clear();
  }
}
