package net.flintmc.mcapi.v1_15_2.world.mapper;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.chat.MinecraftComponentMapper;
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

/** 1.15.2 implementation of {@link ScoreboardMapper}. */
@Singleton
@Implement(value = ScoreboardMapper.class, version = "1.15.2")
public class VersionedScoreboardMapper implements ScoreboardMapper {

  private final Scoreboard scoreboard;
  private final Criteria.Provider criteriaProvider;
  private final MinecraftComponentMapper minecraftComponentMapper;
  private final Objective.Provider objectiveProvider;
  private final PlayerTeam.Provider playerTeamProvider;
  private final Score.Provider scoreProvider;

  @Inject
  public VersionedScoreboardMapper(
      Scoreboard scoreboard,
      Criteria.Provider criteriaProvider,
      MinecraftComponentMapper minecraftComponentMapper,
      Objective.Provider objectiveProvider,
      PlayerTeam.Provider playerTeamProvider,
      Score.Provider scoreProvider) {
    this.scoreboard = scoreboard;
    this.criteriaProvider = criteriaProvider;
    this.minecraftComponentMapper = minecraftComponentMapper;
    this.objectiveProvider = objectiveProvider;
    this.playerTeamProvider = playerTeamProvider;
    this.scoreProvider = scoreProvider;
  }

  /** {@inheritDoc} */
  @Override
  public PlayerTeam fromMinecraftPlayerTeam(Object team) {
    ScorePlayerTeam scorePlayerTeam = (ScorePlayerTeam) team;
    return this.playerTeamProvider.get(scorePlayerTeam.getName());
  }

  /** {@inheritDoc} */
  @Override
  public Object toMinecraftPlayerTeam(PlayerTeam team) {
    return Minecraft.getInstance().world.getScoreboard().getTeam(team.getName());
  }

  /** {@inheritDoc} */
  @Override
  public Object toMinecraftObjective(Objective objective) {
    return Minecraft.getInstance().world.getScoreboard().getObjective(objective.getName());
  }

  /** {@inheritDoc} */
  @Override
  public Objective fromMinecraftObjective(Object objective) {
    if (!(objective instanceof ScoreObjective)) return null;

    ScoreObjective scoreObjective = (ScoreObjective) objective;
    return this.objectiveProvider.get(
        this.scoreboard,
        scoreObjective.getName(),
        this.minecraftComponentMapper.fromMinecraft(scoreObjective.getDisplayName()),
        this.fromMinecraftCriteria(scoreObjective.getCriteria()),
        this.fromMinecraftRenderType(scoreObjective.getRenderType().name()));
  }

  /** {@inheritDoc} */
  @Override
  public Object toMinecraftScore(Score score) {
    return Minecraft.getInstance()
        .world
        .getScoreboard()
        .getOrCreateScore(
            score.getPlayerName(),
            (ScoreObjective) this.toMinecraftObjective(score.getObjective()));
  }

  /** {@inheritDoc} */
  @Override
  public Score fromMinecraftScore(Object score) {
    if (!(score instanceof net.minecraft.scoreboard.Score)) return null;

    net.minecraft.scoreboard.Score minecraftScore = (net.minecraft.scoreboard.Score) score;
    return this.scoreProvider.get(
        this.fromMinecraftObjective(minecraftScore.getObjective()),
        minecraftScore.getPlayerName(),
        minecraftScore.getScorePoints());
  }

  /** {@inheritDoc} */
  @Override
  public Object toMinecraftCriteria(Criteria criteria) {
    return Minecraft.getInstance().world.getScoreboard().getObjective(criteria.getName());
  }

  /** {@inheritDoc} */
  @Override
  public Criteria fromMinecraftCriteria(Object criteria) {
    if (!(criteria instanceof ScoreCriteria)) return null;

    ScoreCriteria scoreCriteria = (ScoreCriteria) criteria;

    return this.criteriaProvider.get(
        scoreCriteria.getName(),
        scoreCriteria.isReadOnly(),
        this.fromMinecraftRenderType(scoreCriteria.getRenderType().name()));
  }

  /** {@inheritDoc} */
  @Override
  public String toMinecraftRenderType(RenderType renderType) {
    switch (renderType) {
      case INTEGER:
      case HEARTS:
        return renderType.name();
      default:
        throw new IllegalStateException("Unexpected value: " + renderType);
    }
  }

  /** {@inheritDoc} */
  @Override
  public RenderType fromMinecraftRenderType(String value) {
    if (value.equalsIgnoreCase("INTEGER")) {
      return RenderType.INTEGER;
    } else if (value.equalsIgnoreCase("HEARTS")) {
      return RenderType.HEARTS;
    } else {
      throw new IllegalStateException("Unexpected value: " + value);
    }
  }
}
