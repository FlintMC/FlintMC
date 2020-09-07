package net.labyfy.component.player.world;

import net.labyfy.component.player.Player;

import java.util.List;

/**
 * Represents the world of this client
 */
public interface ClientWorld {

    /**
     * Retrieves the minecraft world.
     *
     * @return the minecraft world.
     */
    Object getMinecraftWorld();

    /**
     * Retrieves the time of this world.
     *
     * @return the time of this world.
     */
    long getTime();

    /**
     * Retrieves the player count of this world.
     *
     * @return the player count of this world.
     */
    int getPlayerCount();

    /**
     * Retrieves a collection with all players of this world.
     *
     * @return a collection with all players of this world.
     */
    List<Player> getPlayers();

    /**
     * Retrieves the dimension of this world.
     *
     * @return the dimension of this world.
     */
    Dimension getDimension();

    /**
     * Retrieves the scoreboard of this world.
     *
     * @return the scoreboard of this world.
     */
    // TODO: 05.09.2020 Replaces the Object to Scoreboard when the Scoreboard API is ready
    Object getScoreboard();

}
