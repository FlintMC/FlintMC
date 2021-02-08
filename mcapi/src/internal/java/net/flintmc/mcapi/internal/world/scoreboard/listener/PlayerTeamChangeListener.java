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

package net.flintmc.mcapi.internal.world.scoreboard.listener;

import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.chat.format.ChatColor;
import net.flintmc.mcapi.world.scoreboad.score.PlayerTeam;
import net.flintmc.mcapi.world.scoreboad.type.CollisionType;
import net.flintmc.mcapi.world.scoreboad.type.VisibleType;

public interface PlayerTeamChangeListener {

  void changeDisplayName(PlayerTeam playerTeam, ChatComponent displayName);

  void changePrefix(PlayerTeam playerTeam, ChatComponent prefix);

  void changeSuffix(PlayerTeam playerTeam, ChatComponent suffix);

  void changeColor(PlayerTeam playerTeam, String colorName);

  @Deprecated
  void changeColor(PlayerTeam playerTeam, ChatColor chatColor);

  void changeAllowFriendlyFire(PlayerTeam playerTeam, boolean friendlyFire);

  void changeSeeFriendlyInvisible(PlayerTeam playerTeam, boolean friendlyInvisible);

  void changeNameTagVisibility(PlayerTeam playerTeam, VisibleType visibleType);

  void changeDeathMessageVisibility(PlayerTeam playerTeam, VisibleType visibleType);

  void changeCollisionType(PlayerTeam playerTeam, CollisionType collisionType);
}
