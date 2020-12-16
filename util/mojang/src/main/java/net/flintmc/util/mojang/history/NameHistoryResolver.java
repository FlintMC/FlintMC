package net.flintmc.util.mojang.history;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Resolver for the name history of players on Mojang.
 *
 * @see NameHistory
 */
public interface NameHistoryResolver {

  /**
   * Resolves the past names of a specified player.
   *
   * @param uniqueId The non-null UUID of the player to retrieve the name history for
   * @return The non-null future which will be completed with the history or {@code null} if there
   * is no player with the given uuid or an internal error occurred
   */
  CompletableFuture<NameHistory> resolveHistory(UUID uniqueId);
}
