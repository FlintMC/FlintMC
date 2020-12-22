package net.flintmc.mcapi.server;

import java.net.UnknownHostException;
import java.util.concurrent.CompletableFuture;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.server.status.ServerStatus;

/**
 * Describes an entry of the server list.
 */
public interface ServerData {

  /**
   * Loads the {@link ServerStatus} of the server that is described by this instance.
   *
   * @return a completable future of the {@link ServerStatus}.
   * @throws UnknownHostException if the server couldn't be resolved
   */
  CompletableFuture<ServerStatus> loadStatus() throws UnknownHostException;

  /**
   * @return the name of the server described by this instance
   */
  String getName();

  /**
   * @return the server address of the server described by this instance
   */
  ServerAddress getServerAddress();

  /**
   * @return the resource mode of this server list entry
   */
  ResourceMode getResourceMode();

  /**
   * The resource mode of a server list entry.
   */
  enum ResourceMode {
    /**
     * Accepts server resource packs without asking.
     */
    ENABLED,
    /**
     * Doesn't accept server resource packs.
     */
    DISABLED,
    /**
     * Prompts the player whether to accept or decline server resource packs.
     */
    PROMPT
  }

  @AssistedFactory(ServerData.class)
  interface Factory {

    /**
     * Creates a new server list entry (but doesn't add it to the actual server list).
     *
     * @param name         the name of the server
     * @param address      the address of the server
     * @param resourceMode the resource mode of the server
     * @return the new {@link ServerData} instance
     */
    ServerData create(@Assisted String name, @Assisted ServerAddress address,
        @Assisted ResourceMode resourceMode);
  }
}
