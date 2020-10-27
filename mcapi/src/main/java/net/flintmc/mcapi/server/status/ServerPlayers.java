package net.flintmc.mcapi.server.status;

import com.google.inject.assistedinject.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.player.gameprofile.GameProfile;

/**
 * The players on a server in the {@link ServerStatus}.
 */
public interface ServerPlayers {

  /**
   * Retrieves the amount of online players sent by the server.
   *
   * @return The amount of online players
   */
  int getOnlinePlayerCount();

  /**
   * Retrieves the amount of max players sent by the server.
   *
   * @return The amount of max players
   */
  int getMaxPlayerCount();

  /**
   * Retrieves the list of players sent by the server. Most bigger servers don't send this list but just an empty list,
   * others might send players that are no actual players but some information about the server. None of the profiles in
   * the array will be filled with textures.
   *
   * @return The non-null array of non-null profiles
   */
  GameProfile[] getOnlinePlayers();

  /**
   * Factory for the {@link ServerPlayers}.
   */
  @AssistedFactory(ServerPlayers.class)
  interface Factory {

    /**
     * Creates a new players object with the given values.
     *
     * @param online  The amount of online players on the server
     * @param max     The amount of max players that are allowed on the server
     * @param players A non-null array of players that are on the server
     * @return The new non-null players object
     */
    ServerPlayers create(@Assisted("online") int online, @Assisted("max") int max, @Assisted("players") GameProfile[] players);

  }

}
