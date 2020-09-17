package net.labyfy.component.world;

import net.labyfy.component.player.Player;
import net.labyfy.component.world.scoreboad.Scoreboard;

import java.util.List;

/**
 * Represents the world of this client.
 */
public interface ClientWorld extends World {

  /**
   * Retrieves the loaded entity count of this world.
   *
   * @return The loaded entity count of this world.
   */
  int getEntityCount();

  /**
   * Adds a player to the player collection.
   *
   * @param player The player to add
   * @return {@code true} if this collection changed as a result of the call, otherwise {@code false}
   */
  boolean addPlayer(Player player);

  /**
   * Retrieves a collection with all loaded players of this world.
   *
   * @return A collection with all loaded players of this world.
   */
  List<Player> getPlayers();

  /**
   * Retrieves the player count of this world.
   *
   * @return The player count of this world.
   */
  int getPlayerCount();

  /**
   * Retrieves the time of this world.
   *
   * @return The time of this world.
   */
  long getTime();

  /**
   * Retrieves the scoreboard of this world.
   *
   * @return The scoreboard of this world.
   */
  Scoreboard getScoreboard();

}
