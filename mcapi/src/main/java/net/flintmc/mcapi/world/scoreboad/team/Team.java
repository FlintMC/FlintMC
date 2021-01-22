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

package net.flintmc.mcapi.world.scoreboad.team;

import java.util.Collection;
import java.util.Optional;
import net.flintmc.mcapi.chat.format.ChatColor;
import net.flintmc.mcapi.chat.format.ChatFormat;
import net.flintmc.mcapi.world.scoreboad.type.CollisionType;
import net.flintmc.mcapi.world.scoreboad.type.VisibleType;

/**
 * Represents a Minecraft team.
 */
public interface Team {

  /**
   * Whether the given team is the same team of this object.
   *
   * @param other The team to be checked.
   * @return {@code true} if the given team is the same, otherwise {@code false}.
   */
  default boolean isSameTeam(Team other) {
    return this == other;
  }

  /**
   * Retrieves the registry name of this team.
   *
   * @return The registry name.
   */
  String getName();

  /**
   * Retrieves a collection of all members in this team.
   *
   * @return a collection of all members.
   */
  Collection<String> getMembers();

  /**
   * Retrieves the color of this team.
   *
   * @deprecated To avoid an error.
   * @return The team color.
   */
  @Deprecated
  ChatColor getTeamColor();

  /**
   * Retrieves an optional color of this team.
   *
   * @return The optional team color.
   */
  Optional<ChatColor> getOptionalTeamColor();

  /**
   * Retrieves an optional chat format of this team.
   *
   * @return The optional team chat format.
   */
  Optional<ChatFormat> getOptionalChatFormat();

  /**
   * Whether friendly invisible members may be seen.
   *
   * @return {@code true} if friendly invisible members can be seen, otherwise {@code false}.
   */
  boolean seeFriendlyInvisible();

  /**
   * Whether friendly fire is allowed in this team.
   *
   * @return {@code true} if friendly fire allowed, otherwise {@code false}.
   */
  boolean allowFriendlyFire();

  /**
   * Retrieves the name tag visibility of this team.
   *
   * @return The name tag visibility.
   */
  VisibleType getNameTagVisibility();

  /**
   * Retrieves the death message visibility of this team.
   *
   * @return The death message visibility.
   */
  VisibleType getDeathMessageVisibility();

  /**
   * Retrieves the collision rule of this team.
   *
   * @return The collision rule.
   */
  CollisionType getCollisionType();
}
