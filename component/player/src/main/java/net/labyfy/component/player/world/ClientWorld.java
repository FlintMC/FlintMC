package net.labyfy.component.player.world;

import net.labyfy.component.player.Player;

import java.util.List;
import java.util.function.Predicate;

/**
 * Represents the world of this client
 */
public interface ClientWorld {

    /**
     * Retrieves the minecraft world.
     *
     * @return The minecraft world.
     */
    Object getClientWorld();

    /**
     * Retrieves the time of this world.
     *
     * @return The time of this world.
     */
    long getTime();

    /**
     * Retrieves the player count of this world.
     *
     * @return The player count of this world.
     */
    int getPlayerCount();

    /**
     * Adds a player to the collection.
     *
     * @param player The player to add
     * @return {@code true} if this collection changed as a result of the call, otherwise {@code false}.
     */
    boolean addPlayer(Player player);

    /**
     * Removes all of the players of this collection that satisfy the given predicate. Error or runtime
     * exception thrown during iteration or by the predicate are relayed to the caller.
     *
     * @param filter A predicate which returns {@code true} for players to removed
     * @return {@code true} if any players were removed
     */
    boolean removeIfPlayer(Predicate<? super Player> filter);

    /**
     * Retrieves a collection with all players of this world.
     *
     * @return A collection with all players of this world.
     */
    List<Player> getPlayers();

    /**
     * Retrieves the entity count of this world.
     *
     * @return The entity count of this world.
     */
    int getCountLoadedEntities();

    /**
     * Retrieves the dimension of this world.
     *
     * @return The dimension of this world.
     */
    Dimension getDimension();

    /**
     * Retrieves the scoreboard of this world.
     *
     * @return The scoreboard of this world.
     */
    // TODO: 05.09.2020 Replaces the Object to Scoreboard when the Scoreboard API is ready
    Object getScoreboard();

}
