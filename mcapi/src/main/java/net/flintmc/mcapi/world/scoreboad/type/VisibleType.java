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
 * An enumeration of all available visibility types.
 */
public enum VisibleType {

  /**
   * Visible to all players.
   */
  ALWAYS("always", 0),
  /**
   * Invisible to all players.
   */
  NEVER("never", 1),
  /**
   * The members of the own team can see it, other teams cannot.
   */
  HIDE_FOR_OTHER_TEAMS("hideForOtherTeams", 2),
  /**
   * The members of the own team cannot see it, but other teams can see it.
   */
  HIDE_FOR_OWN_TEAMS("hideForOwnTeam", 3);

  private static final Map<String, VisibleType> VISIBLE_TYPES =
      Arrays.stream(values())
          .collect(Collectors.toMap(function -> function.internalName, function -> function));

  private final String internalName;
  private final int id;

  VisibleType(String internalName, int id) {
    this.internalName = internalName;
    this.id = id;
  }

  public static VisibleType getByName(String name) {
    return VISIBLE_TYPES.get(name);
  }

  /**
   * Retrieves the internal name of this visible type.
   *
   * @return The internal name of this visible type.
   */
  public String getInternalName() {
    return internalName;
  }

  /**
   * Retrieves the identifier of this visible type.
   *
   * @return The identifier of this visible type.
   */
  public int getId() {
    return id;
  }
}
