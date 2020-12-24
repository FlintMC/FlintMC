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

package net.flintmc.mcapi.server.status;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.player.gameprofile.GameProfile;

/**
 * The players on a server in the {@link ServerStatus}.
 */
public interface ServerPlayers {

  /**
   * Retrieves the amount of online players sent by the server.
   *
   * @return The amount of online players
   */
  int getOnlinePlayerCount();

  /**
   * Retrieves the amount of max players sent by the server.
   *
   * @return The amount of max players
   */
  int getMaxPlayerCount();

  /**
   * Retrieves the list of players sent by the server. Most bigger servers don't send this list but
   * just an empty list, others might send players that are no actual players but some information
   * about the server. None of the profiles in the array will be filled with textures.
   *
   * @return The non-null array of non-null profiles
   */
  GameProfile[] getOnlinePlayers();

  /**
   * Factory for the {@link ServerPlayers}.
   */
  @AssistedFactory(ServerPlayers.class)
  interface Factory {

    /**
     * Creates a new players object with the given values.
     *
     * @param online  The amount of online players on the server
     * @param max     The amount of max players that are allowed on the server
     * @param players A non-null array of players that are on the server
     * @return The new non-null players object
     */
    ServerPlayers create(
        @Assisted("online") int online,
        @Assisted("max") int max,
        @Assisted("players") GameProfile[] players);
  }
}
