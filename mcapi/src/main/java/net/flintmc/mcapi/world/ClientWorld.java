package net.flintmc.mcapi.world;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import net.flintmc.mcapi.entity.Entity;
import net.flintmc.mcapi.player.PlayerEntity;
import net.flintmc.mcapi.world.scoreboad.Scoreboard;

/**
 * Represents the world of this client.
 */
public interface ClientWorld extends World {

  /**
   * Retrieves an entity with the given identifier.
   *
   * @param identifier The identifier of an entity.
   * @return An entity with the identifier or {@code null}.
   */
  Entity getEntityByIdentifier(int identifier);

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
   * @return {@code true} if this collection changed as a result of the call, otherwise {@code
   * false}
   */
  boolean addPlayer(PlayerEntity player);

  /**
   * Removes a player from the collection.
   *
   * @param uniqueId The unique identifier of the player to be removed.
   * @return {@code true} if the player was removed, otherwise {@code false}.
   */
  boolean removePlayer(UUID uniqueId);

  /**
   * Retrieves the player count of this world.
   *
   * @return The player count of this world.
   */
  int getPlayerCount();

  /**
   * Retrieves the scoreboard of this world.
   *
   * @return The scoreboard of this world.
   */
  Scoreboard getScoreboard();

  /**
   * Retrieves a key-value system with all entities in the world.
   *
   * @return A key-value system with all entities in the world.
   */
  Map<Integer, Entity> getEntities();

  /**
   * Retrieves a collection with all players in the world.
   *
   * @return A collection with all players in the world.
   */
  Set<PlayerEntity> getPlayers();
}
