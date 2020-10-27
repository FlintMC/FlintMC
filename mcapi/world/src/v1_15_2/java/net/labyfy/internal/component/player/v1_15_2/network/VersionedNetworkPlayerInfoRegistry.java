package net.labyfy.internal.component.player.v1_15_2.network;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.player.network.NetworkPlayerInfo;
import net.labyfy.component.player.network.NetworkPlayerInfoRegistry;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

/**
 * 1.15.2 implementation of {@link NetworkPlayerInfoRegistry}
 */
@Singleton
@Implement(value = NetworkPlayerInfoRegistry.class, version = "1.15.2")
public class VersionedNetworkPlayerInfoRegistry implements NetworkPlayerInfoRegistry {

  private final Map<UUID, NetworkPlayerInfo> networkPlayerInfoMap;

  @Inject
  private VersionedNetworkPlayerInfoRegistry() {
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
