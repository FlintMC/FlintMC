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

package net.flintmc.mcapi.player.network;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

/**
 * Represents a {@link NetworkPlayerInfo} registry.
 */
public interface NetworkPlayerInfoRegistry {

  /**
   * Retrieves the network info of a player with the username
   *
   * @param username The username of a player
   * @return The network info of a player
   */
  NetworkPlayerInfo getPlayerInfo(String username);

  /**
   * Retrieves the network info of a player with the unique identifier
   *
   * @param uniqueId The unique identifier of a player
   * @return The network info of a player
   */
  NetworkPlayerInfo getPlayerInfo(UUID uniqueId);

  /**
   * Retrieves a collection of a network player info
   *
   * @return A collection of {@link NetworkPlayerInfo}
   */
  Collection<NetworkPlayerInfo> getPlayerInfo();

  /**
   * Retrieves a key-value system of all online players network information.
   *
   * @return A key-value system.
   */
  Map<UUID, NetworkPlayerInfo> getPlayerInfoMap();
}
