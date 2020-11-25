package net.flintmc.mcapi.server;

import net.flintmc.mcapi.resources.ResourceLocation;
import net.flintmc.mcapi.server.status.ServerStatus;
import net.flintmc.mcapi.server.status.ServerStatusResolver;

import java.util.concurrent.CompletableFuture;

public interface ConnectedServer {

  /**
   * Retrieves the address of the server which the client is connected to.
   *
   * @return The address of the server or {@code null} if the client is not connected to any server
   */
  ServerAddress getAddress();

  /**
   * Retrieves whether the client is connected to any server (in multiplayer).
   *
   * @return {@code true} if the client is connected to a server, {@code false} otherwise
   */
  boolean isConnected();

  /**
   * Uses {@link ServerStatusResolver#resolveStatus(ServerAddress)} to get the status of a server
   * for the server list.
   *
   * @return The non-null future which will be completed with the status or {@code null} if the
   *     status couldn't be retrieved
   * @throws IllegalStateException If the client is not connected with any server
   */
  CompletableFuture<ServerStatus> resolveStatus() throws IllegalStateException;

  /**
   * Sends a custom payload message to the currently connected server which can be used to
   * communicate with servers for any extra data in the client.
   *
   * @param identifier The non-null identifier of the payload
   * @param payload The non-null payload for the server
   * @throws IllegalStateException If the client is not connected with any server
   */
  // TODO replace the String with the NamespacedKey
  void sendCustomPayload(ResourceLocation identifier, byte[] payload) throws IllegalStateException;

  void retrieveCustomPayload(String identifier, byte[] payload);

}

