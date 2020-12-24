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

package net.flintmc.mcapi.world.scoreboad.type;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * An enumeration of all available collision rules.
 */
public enum CollisionType {

  /**
   * The members of the team can push all objects and can be pushed by all objects.
   */
  ALWAYS("always", 0),
  /**
   * The members of the team cannot push an object, but they cannot be pushed either.
   */
  NEVER("never", 1),
  /**
   * The members of the team can push objects of other teams, but team mates cannot.
   */
  PUSH_OTHER_TEAMS("pushOtherTeams", 2),
  /**
   * The members of the team can only move objects of the same team.
   */
  PUSH_OWN_TEAM("pushOwnTeam", 3);

  private static final Map<String, CollisionType> COLLISION_TYPES =
      Arrays.stream(values())
          .collect(Collectors.toMap(function -> function.internalName, function -> function));

  private final String internalName;
  private final int identifier;

  CollisionType(String internalName, int identifier) {
    this.internalName = internalName;
    this.identifier = identifier;
  }

  public static CollisionType getByName(String name) {
    return COLLISION_TYPES.get(name);
  }

  /**
   * Retrieves the internal name of this collision type.
   *
   * @return The internal name of this collision type.
   */
  public String getInternalName() {
    return internalName;
  }

  /**
   * Retrieves the identifier of this collision type.
   *
   * @return The identifier of this collision type.
   */
  public int getIdentifier() {
    return identifier;
  }
}
