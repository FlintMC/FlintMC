package net.labyfy.component.world.mapper;

import net.labyfy.component.world.scoreboad.score.Criteria;
import net.labyfy.component.world.scoreboad.score.Objective;
import net.labyfy.component.world.scoreboad.score.PlayerTeam;
import net.labyfy.component.world.scoreboad.score.Score;
import net.labyfy.component.world.scoreboad.type.RenderType;

/**
 *
 */
public interface ScoreboardMapper {

  Object toMinecraftPlayerTeam(PlayerTeam team);

  PlayerTeam fromMinecraftPlayerTeam(Object team);

  Object toMinecraftObjective(Objective objective);

  Objective fromMinecraftObjective(Object objective);

  Object toMinecraftScore(Score score);

  Score fromMinecraftScore(Object score);

  Object toMinecraftCriteria(Criteria criteria);

  Criteria fromMinecraftCriteria(Object criteria);

  String toMinecraftRenderType(RenderType renderType);

  RenderType fromMinecraftRenderType(String value);

}
