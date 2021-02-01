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

package net.flintmc.mcapi.world.mapper;

import net.flintmc.mcapi.world.scoreboad.score.Criteria;
import net.flintmc.mcapi.world.scoreboad.score.Objective;
import net.flintmc.mcapi.world.scoreboad.score.PlayerTeam;
import net.flintmc.mcapi.world.scoreboad.score.Score;
import net.flintmc.mcapi.world.scoreboad.type.CollisionType;
import net.flintmc.mcapi.world.scoreboad.type.RenderType;
import net.flintmc.mcapi.world.scoreboad.type.VisibleType;

/**
 * Scoreboard mapper between the Minecraft objects and the Flint objects.
 */
public interface ScoreboardMapper {

  /**
   * Creates a new Minecraft player team by using the given {@link PlayerTeam} as the base.
   *
   * @param team The non-null {@link PlayerTeam}.
   * @return The new Minecraft player team or {@code null}, if the given player team was invalid.
   */
  Object toMinecraftPlayerTeam(PlayerTeam team);

  /**
   * Creates a new {@link PlayerTeam} by using the given Minecraft player team as the base.
   *
   * @param team The non-null Minecraft player team.
   * @return The new {@link PlayerTeam} or {@code null}, if the given player team was invalid.
   */
  PlayerTeam fromMinecraftPlayerTeam(Object team);

  /**
   * Creates a new Minecraft objective by using the given {@link Objective} as the base.
   *
   * @param objective The non-null {@link Objective}.
   * @return The new Minecraft objective or {@code null}, if the given objective was invalid.
   */
  Object toMinecraftObjective(Objective objective);

  /**
   * Creates a new {@link Objective} by using the given Minecraft objective as the base.
   *
   * @param objective The non-null Minecraft objective.
   * @return The new {@link Objective} or {@code null}, if the given objective was invalid.
   */
  Objective fromMinecraftObjective(Object objective);

  /**
   * Creates a new Minecraft score by using the given {@link Score} as the base.
   *
   * @param score The non-null {@link Score}.
   * @return The new Minecraft score or {@code null}, if the given objective was invalid.
   */
  Object toMinecraftScore(Score score);

  /**
   * Creates a new {@link Score} by using the given Minecraft score as the base.
   *
   * @param score The non-null Minecraft score.
   * @return The new {@link Score} or {@code null}, if the given score was invalid.
   */
  Score fromMinecraftScore(Object score);

  /**
   * Creates a new Minecraft criteria by using the given {@link Criteria} as the base.
   *
   * @param criteria The non-null {@link Criteria}.
   * @return The new Minecraft criteria or {@code null}, if the given criteria was invalid.
   */
  Object toMinecraftCriteria(Criteria criteria);

  /**
   * Creates a new {@link Criteria} by using the given Minecraft criteria as the base.
   *
   * @param criteria The non-null Minecraft criteria.
   * @return The new {@link Criteria} or {@code null}, if the given criteria was invalid.
   */
  Criteria fromMinecraftCriteria(Object criteria);

  /**
   * Retrieves a {@link String} by the given {@link RenderType}.
   *
   * @param renderType The type to get the name.
   * @return The minecraft non-null render type.
   */
  Object toMinecraftRenderType(RenderType renderType);

  /**
   * Retrieves a {@link VisibleType} by using the given Minecraft visible as the base.
   *
   * @param handle The non-null Minecraft visible.
   * @return A visible type or {@code null}.
   */
  VisibleType fromMinecraftVisible(Object handle);

  /**
   * Retrieves a Minecraft visible by using the given {@link VisibleType} as the base.
   *
   * @param visibleType The non-null visible type.
   * @return A visible type or {@code null}.
   */
  Object toMinecraftVisible(VisibleType visibleType);

  /**
   * Retrieves a {@link CollisionType} by using the given Minecraft collision rule as the base.
   *
   * @param handle The non-null Minecraft collision rule.
   * @return A collision type or {@code null}.
   */
  CollisionType fromMinecraftCollisionRule(Object handle);

  /**
   * Retrieves a Minecraft collision rule by using the given {@link CollisionType} as the base.
   *
   * @param collisionType The non-null collision type.
   * @return A collision rule.
   */
  Object toMinecraftCollisionRule(CollisionType collisionType);

  /**
   * Retrieves a {@link RenderType} by the given value.
   *
   * @param value The non-null Minecraft render type.
   * @return A {@link RenderType}.
   * @throws UnsupportedOperationException If thrown when the render type doesn't exist.
   */
  RenderType fromMinecraftRenderType(Object value);
}
