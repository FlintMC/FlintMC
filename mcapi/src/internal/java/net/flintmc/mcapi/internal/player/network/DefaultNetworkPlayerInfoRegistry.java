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

package net.flintmc.mcapi.internal.player.network;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.player.network.NetworkPlayerInfo;
import net.flintmc.mcapi.player.network.NetworkPlayerInfoRegistry;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

/** Default implementation of {@link NetworkPlayerInfoRegistry} */
@Singleton
@Implement(value = NetworkPlayerInfoRegistry.class, version = "1.15.2")
public class DefaultNetworkPlayerInfoRegistry implements NetworkPlayerInfoRegistry {

  private final Map<UUID, NetworkPlayerInfo> networkPlayerInfoMap;

  @Inject
  private DefaultNetworkPlayerInfoRegistry() {
    this.networkPlayerInfoMap = Maps.newHashMap();
  }

  /**
   * Retrieves the network info of a player with the username
   *
   * @param username The username of a player
   * @return The network info of a player
   */
  @Override
  public NetworkPlayerInfo getPlayerInfo(String username) {
    for (NetworkPlayerInfo networkPlayerInfo : this.networkPlayerInfoMap.values()) {
      if (networkPlayerInfo.getGameProfile().getName().equalsIgnoreCase(username)) {
        return networkPlayerInfo;
      }
    }
    return null;
  }

  /**
   * Retrieves the network info of a player with the unique identifier
   *
   * @param uniqueId The unique identifier of a player
   * @return The network info of a player
   */
  @Override
  public NetworkPlayerInfo getPlayerInfo(UUID uniqueId) {
    return this.networkPlayerInfoMap.get(uniqueId);
  }

  /**
   * Retrieves a collection of a network player info
   *
   * @return A collection of {@link NetworkPlayerInfo}
   */
  @Override
  public Collection<NetworkPlayerInfo> getPlayerInfo() {
    return this.networkPlayerInfoMap.values();
  }

  @Override
  public Map<UUID, NetworkPlayerInfo> getPlayerInfoMap() {
    return this.networkPlayerInfoMap;
  }
}
