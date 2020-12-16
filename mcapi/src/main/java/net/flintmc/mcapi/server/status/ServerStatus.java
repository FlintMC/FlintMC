package net.flintmc.mcapi.server.status;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.server.ServerAddress;

/**
 * The status of a server for the server list containing the description of the server ("MOTD"), the
 * version of the server, the player count of the server, the favicon of the server and the ping to
 * the server.
 */
public interface ServerStatus {

  /**
   * Retrieves the address where this status came from.
   *
   * @return The non-null address as the source of this status
   */
  ServerAddress getSourceAddress();

  /**
   * Retrieves the version (name and protocol) of the server.
   *
   * @return The non-null version of the server
   */
  ServerVersion getVersion();

  /**
   * Retrieves the players (online, max, list) of the server.
   *
   * @return The non-null players of the server
   */
  ServerPlayers getPlayers();

  /**
   * Retrieves the description ("MOTD") of the server which might contain a line break to separate
   * two lines.
   *
   * @return The non-null description of the server
   */
  ChatComponent getDescription();

  /**
   * Retrieves the favicon of the server, by default servers are only allowed to send 64x64 images.
   * This is not null even if the server doesn't send anything.
   *
   * @return The non-null favicon of the server
   */
  ServerFavicon getFavicon();

  /**
   * Retrieves the ping to the server in milliseconds.
   *
   * @return The ping in milliseconds, always {@code x >= 0}
   */
  long getPing();

  /**
   * Retrieves the timestamp in milliseconds when this status has been created.
   *
   * @return The timestamp in milliseconds since 01/01/1970
   */
  long getTimestamp();

  /** Factory for the {@link ServerStatus}. */
  @AssistedFactory(ServerStatus.class)
  interface Factory {

    /**
     * Creates a new server status with the given values.
     *
     * @param sourceAddress The non-null address of the server where these values came from
     * @param version The non-null version of the server
     * @param players The non-null players on the server
     * @param description The non-null description of the server (can contain a line break)
     * @param favicon The non-null favicon of the server
     * @param ping The ping between the client and the server in milliseconds
     * @return The new non-null status
     */
    ServerStatus create(
        @Assisted("sourceAddress") ServerAddress sourceAddress,
        @Assisted("version") ServerVersion version,
        @Assisted("players") ServerPlayers players,
        @Assisted("description") ChatComponent description,
        @Assisted("favicon") ServerFavicon favicon,
        @Assisted("ping") long ping);
  }
}
