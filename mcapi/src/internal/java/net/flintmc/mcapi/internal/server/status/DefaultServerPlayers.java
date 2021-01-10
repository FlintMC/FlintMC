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

package net.flintmc.mcapi.internal.server.status;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.player.gameprofile.GameProfile;
import net.flintmc.mcapi.server.status.ServerPlayers;

@Implement(ServerPlayers.class)
public class DefaultServerPlayers implements ServerPlayers {

  private final int online;
  private final int max;
  private final GameProfile[] players;

  @AssistedInject
  public DefaultServerPlayers(
      @Assisted("online") int online,
      @Assisted("max") int max,
      @Assisted("players") GameProfile[] players) {
    this.online = online;
    this.max = max;
    this.players = players;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getOnlinePlayerCount() {
    return this.online;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getMaxPlayerCount() {
    return this.max;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public GameProfile[] getOnlinePlayers() {
    return this.players;
  }
}
