package net.flintmc.mcapi.server.status.pending;

import java.net.UnknownHostException;
import java.util.concurrent.CompletableFuture;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.server.ServerAddress;
import net.flintmc.mcapi.server.status.ServerFavicon;
import net.flintmc.mcapi.server.status.ServerStatus;

/**
 * A pending request for the {@link ServerStatus} of a server.
 */
public interface PendingStatusRequest {

  /**
   * Retrieves the future which will be completed with the {@link ServerStatus} when the status has
   * been successfully retrieved or {@code null} if the status couldn't be retrieved.
   *
   * @return The non-null future of this request
   */
  CompletableFuture<ServerStatus> getFuture();

  /**
   * Retrieves the address of the server where this request will be sent (or has already been sent)
   * to.
   *
   * @return The non-null address of the server
   */
  ServerAddress getTargetAddress();

  /**
   * Retrieves the current state of this request.
   *
   * @return The non-null state of this request
   */
  PendingStatusState getState();

  /**
   * Connects to the server and requests the status. This changes the state from {@link
   * PendingStatusState#IDLE} to {@link PendingStatusState#CONNECTING} and to {@link
   * PendingStatusState#RECEIVING}.
   *
   * @throws UnknownHostException     If an unknown host has been provided when creating this
   *                                  request
   * @throws IllegalArgumentException If this method has been called already.
   */
  void start() throws UnknownHostException, IllegalArgumentException;

  /**
   * Retrieves the timestamp in milliseconds when the {@link #start()} method has been called.
   *
   * @return The timestamp in milliseconds since 01/01/1970 or -1 if the {@link #start()} method
   * hasn't been called yet
   */
  long getStartTimestamp();

  /**
   * Factory for the {@link PendingStatusRequest}.
   */
  @AssistedFactory(PendingStatusRequest.class)
  interface Factory {

    /**
     * Creates a new {@link PendingStatusRequest} with the {@link PendingStatusState#IDLE} state.
     * This method will not connect to the server, to connect to the server and request a status,
     * use {@link PendingStatusRequest#start()}.
     *
     * @param targetAddress  The non-null address to retrieve the status from.
     * @param defaultFavicon The non-null favicon to be used when the server doesn't send any
     *                       favicon
     * @return The new non-null status request
     */
    PendingStatusRequest create(
        @Assisted("targetAddress") ServerAddress targetAddress,
        @Assisted("defaultFavicon") ServerFavicon defaultFavicon);
  }
}
