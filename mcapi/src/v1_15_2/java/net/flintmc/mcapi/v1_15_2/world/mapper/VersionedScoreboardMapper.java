package net.flintmc.mcapi.v1_15_2.world.mapper;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.chat.MinecraftComponentMapper;
import net.flintmc.mcapi.world.mapper.ScoreboardMapper;
import net.flintmc.mcapi.world.scoreboad.score.Criteria;
import net.flintmc.mcapi.world.scoreboad.score.Objective;
import net.flintmc.mcapi.world.scoreboad.score.Objective.Factory;
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

  private final Criteria.Factory criteriaFactory;
  private final MinecraftComponentMapper minecraftComponentMapper;
  private final Objective.Factory objectiveFactory;
  private final PlayerTeam.Factory playerTeamFactory;
  private final Score.Factory scoreFactory;

  @Inject
  public VersionedScoreboardMapper(
      Criteria.Factory criteriaFactory,
      MinecraftComponentMapper minecraftComponentMapper,
      Factory objectiveFactory,
      PlayerTeam.Factory playerTeamFactory,
      Score.Factory scoreFactory) {
    this.criteriaFactory = criteriaFactory;
    this.minecraftComponentMapper = minecraftComponentMapper;
    this.objectiveFactory = objectiveFactory;
    this.playerTeamFactory = playerTeamFactory;
    this.scoreFactory = scoreFactory;
  }

  /** {@inheritDoc} */
  @Override
  public PlayerTeam fromMinecraftPlayerTeam(Object team) {
    ScorePlayerTeam scorePlayerTeam = (ScorePlayerTeam) team;
    return this.playerTeamFactory.create(scorePlayerTeam.getName());
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
    if (!(objective instanceof ScoreObjective)) {
      return null;
    }

    ScoreObjective scoreObjective = (ScoreObjective) objective;
    return this.objectiveFactory.create(
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
    if (!(score instanceof net.minecraft.scoreboard.Score)) {
      return null;
    }

    net.minecraft.scoreboard.Score minecraftScore = (net.minecraft.scoreboard.Score) score;
    return this.scoreFactory.create(
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
    if (!(criteria instanceof ScoreCriteria)) {
      return null;
    }

    ScoreCriteria scoreCriteria = (ScoreCriteria) criteria;

    return this.criteriaFactory.create(
        scoreCriteria.getName(),
        scoreCriteria.isReadOnly(),
        this.fromMinecraftRenderType(scoreCriteria.getRenderType().name()));
  }

  /** {@inheritDoc} */
  @Override
  public Object toMinecraftRenderType(RenderType renderType) {
    switch (renderType) {
      case INTEGER:
        return ScoreCriteria.RenderType.INTEGER;
      case HEARTS:
        return ScoreCriteria.RenderType.HEARTS;
      default:
        throw new IllegalStateException("Unexpected value: " + renderType);
    }
  }

  /** {@inheritDoc} */
  @Override
  public RenderType fromMinecraftRenderType(Object value) {
    if (!(value instanceof ScoreCriteria.RenderType)) {
      return null;
    }

    ScoreCriteria.RenderType renderType = (ScoreCriteria.RenderType) value;

    switch (renderType) {
      case HEARTS:
        return RenderType.HEARTS;
      case INTEGER:
      default:
        return RenderType.INTEGER;
    }
  }
}
