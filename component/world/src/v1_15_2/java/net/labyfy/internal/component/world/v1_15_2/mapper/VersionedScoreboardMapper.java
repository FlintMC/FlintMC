package net.labyfy.internal.component.world.v1_15_2.mapper;

import com.google.inject.Inject;
import net.labyfy.chat.MinecraftComponentMapper;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.processing.autoload.AutoLoad;
import net.labyfy.component.world.mapper.ScoreboardMapper;
import net.labyfy.component.world.scoreboad.Scoreboard;
import net.labyfy.component.world.scoreboad.score.Criteria;
import net.labyfy.component.world.scoreboad.score.Objective;
import net.labyfy.component.world.scoreboad.score.PlayerTeam;
import net.labyfy.component.world.scoreboad.score.Score;
import net.labyfy.component.world.scoreboad.type.RenderType;
import net.minecraft.client.Minecraft;
import net.minecraft.scoreboard.ScoreCriteria;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;

/**
 *
 */
@AutoLoad
@Implement(value = ScoreboardMapper.class, version = "1.15.2")
public class VersionedScoreboardMapper implements ScoreboardMapper {

  private final Criteria.Provider criteriaProvider;
  private final MinecraftComponentMapper minecraftComponentMapper;
  private final Objective.Provider objectiveProvider;
  private final PlayerTeam.Provider playerTeamProvider;
  private final Score.Provider scoreProvider;

  @Inject
  public VersionedScoreboardMapper(
          Criteria.Provider criteriaProvider,
          MinecraftComponentMapper minecraftComponentMapper,
          Objective.Provider objectiveProvider,
          PlayerTeam.Provider playerTeamProvider,
          Score.Provider scoreProvider
  ) {
    this.criteriaProvider = criteriaProvider;
    this.minecraftComponentMapper = minecraftComponentMapper;
    this.objectiveProvider = objectiveProvider;
    this.playerTeamProvider = playerTeamProvider;
    this.scoreProvider = scoreProvider;
  }

  @Override
  public PlayerTeam fromMinecraftPlayerTeam(Object team) {
    ScorePlayerTeam scorePlayerTeam = (ScorePlayerTeam) team;
    return this.playerTeamProvider.get(scorePlayerTeam.getName());
  }

  @Override
  public Object toMinecraftPlayerTeam(PlayerTeam team) {
    return Minecraft.getInstance().world.getScoreboard().getTeam(team.getName());
  }

  @Override
  public Object toMinecraftObjective(Objective objective) {
    return Minecraft.getInstance().world.getScoreboard().getObjective(objective.getName());
  }

  @Override
  public Objective fromMinecraftObjective(Object objective) {
    if (!(objective instanceof ScoreObjective)) return null;

    ScoreObjective scoreObjective = (ScoreObjective) objective;
    return this.objectiveProvider.get(
            scoreObjective.getName(),
            this.minecraftComponentMapper.fromMinecraft(scoreObjective.getDisplayName()),
            this.fromMinecraftCriteria(scoreObjective.getCriteria()),
            this.fromMinecraftRenderType(scoreObjective.getRenderType().name())
    );
  }

  @Override
  public Object toMinecraftScore(Score score) {
    return Minecraft.getInstance().world.getScoreboard().getOrCreateScore(
            score.getPlayerName(),
            (ScoreObjective) this.toMinecraftObjective(score.getObjective())
    );
  }

  @Override
  public Score fromMinecraftScore(Object score) {
    if (!(score instanceof net.minecraft.scoreboard.Score)) return null;

    net.minecraft.scoreboard.Score minecraftScore = (net.minecraft.scoreboard.Score) score;
    return this.scoreProvider.get(
            this.fromMinecraftObjective(
                    minecraftScore.getObjective()
            ),
            minecraftScore.getPlayerName()
    );
  }

  @Override
  public Object toMinecraftCriteria(Criteria criteria) {
    return Minecraft.getInstance().world.getScoreboard().getObjective(criteria.getName());
  }

  @Override
  public Criteria fromMinecraftCriteria(Object criteria) {
    if (!(criteria instanceof ScoreCriteria)) return null;

    ScoreCriteria scoreCriteria = (ScoreCriteria) criteria;

    return this.criteriaProvider.get(
            scoreCriteria.getName(),
            scoreCriteria.isReadOnly(),
            this.fromMinecraftRenderType(
                    scoreCriteria.getRenderType().name()
            )
    );
  }

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
