package net.labyfy.component.player.network;

import java.util.Collection;
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


}
