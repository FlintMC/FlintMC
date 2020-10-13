package net.labyfy.component.world;

import net.labyfy.component.entity.Entity;
import net.labyfy.component.player.AbstractClientPlayerEntity;
import net.labyfy.component.player.PlayerEntity;
import net.labyfy.component.world.scoreboad.Scoreboard;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

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
  boolean addPlayer(AbstractClientPlayerEntity player);

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

  Map<Integer, Entity> getEntities();

  Set<AbstractClientPlayerEntity> getPlayers();
}
