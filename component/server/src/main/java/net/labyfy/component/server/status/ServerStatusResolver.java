package net.labyfy.component.server.status;

import net.labyfy.component.server.ServerAddress;
import net.labyfy.component.server.status.pending.PendingStatusRequest;

import java.net.UnknownHostException;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

/**
 * This interface is used to ping servers and retrieve their status for the server list.
 */
public interface ServerStatusResolver {

  /**
   * Connects to the given server and retrieves its status for the server list.
   *
   * @param address The non-null address to connect to
   * @return The non-null future to retrieve the status
   * @throws UnknownHostException If an unknown host has been provided in the address
   */
  CompletableFuture<ServerStatus> resolveStatus(ServerAddress address) throws UnknownHostException;

  /**
   * Retrieves an unordered collection of all status requests that haven't received an answer from the server yet.
   *
   * @return An unmodifiable collection of all pending requests
   */
  Collection<PendingStatusRequest> getPendingRequests();

}
