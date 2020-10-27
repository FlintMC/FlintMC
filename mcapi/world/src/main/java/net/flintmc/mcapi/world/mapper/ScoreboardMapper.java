package net.flintmc.mcapi.world.mapper;

import net.flintmc.mcapi.world.scoreboad.score.Criteria;
import net.flintmc.mcapi.world.scoreboad.score.Objective;
import net.flintmc.mcapi.world.scoreboad.score.PlayerTeam;
import net.flintmc.mcapi.world.scoreboad.score.Score;
import net.flintmc.mcapi.world.scoreboad.type.RenderType;

/**
 * Scoreboard mapper between the Minecraft objects and the Labyfy objects.
 */
public interface ScoreboardMapper {

  /**
   * Creates a new Minecraft player team by using the given {@link PlayerTeam} as the base.
   *
   * @param team The non-null {@link PlayerTeam}.
   * @return The new Minecraft player team or {@code null} if the given player team was invalid.
   */
  Object toMinecraftPlayerTeam(PlayerTeam team);

  /**
   * Creates a new {@link PlayerTeam} by using the given Minecraft player team as the base.
   *
   * @param team The non-null Minecraft player team.
   * @return The new {@link PlayerTeam} or {@code null} if the given player team was invalid.
   */
  PlayerTeam fromMinecraftPlayerTeam(Object team);

  /**
   * Creates a new Minecraft objective by using the given {@link Objective} as the base.
   *
   * @param objective The non-null {@link Objective}.
   * @return The new Minecraft objective or {@code null} if the given objective was invalid.
   */
  Object toMinecraftObjective(Objective objective);

  /**
   * Creates a new {@link Objective} by using the given Minecraft objective as the base.
   *
   * @param objective The non-null Minecraft objective.
   * @return The new {@link Objective} or {@code null} if the given objective was invalid.
   */
  Objective fromMinecraftObjective(Object objective);

  /**
   * Creates a new Minecraft score by using the given {@link Score} as the base.
   *
   * @param score The non-null {@link Score}.
   * @return The new Minecraft score or {@code null} if the given objective was invalid.
   */
  Object toMinecraftScore(Score score);

  /**
   * Creates a new {@link Score} by using the given Minecraft score as the base.
   *
   * @param score The non-null Minecraft score.
   * @return The new {@link Score} or {@code null} if the given score was invalid.
   */
  Score fromMinecraftScore(Object score);

  /**
   * Creates a new Minecraft criteria by using the given {@link Criteria} as the base.
   *
   * @param criteria The non-null {@link Criteria}.
   * @return The new Minecraft criteria or {@code null} if the given criteria was invalid.
   */
  Object toMinecraftCriteria(Criteria criteria);

  /**
   * Creates a new {@link Criteria} by using the given Minecraft criteria as the base.
   *
   * @param criteria The non-null Minecraft criteria.
   * @return The new {@link Criteria} or {@code null} if the given criteria was invalid.
   */
  Criteria fromMinecraftCriteria(Object criteria);

  /**
   * Retrieves a {@link String} by the given {@link RenderType}.
   *
   * @param renderType The type to get the name.
   * @return The name of the render type.
   */
  String toMinecraftRenderType(RenderType renderType);

  /**
   * Retrieves a {@link RenderType} by the given value.
   *
   * @param value The name of the render type.
   * @return A {@link RenderType}.
   * @throws UnsupportedOperationException If thrown when the render type doesn't exist.
   */
  RenderType fromMinecraftRenderType(String value);

}
