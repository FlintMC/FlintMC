package net.flintmc.mcapi.server;

/**
 * The controller for the currently connected server.
 */
public interface ServerController {

  /**
   * Retrieves whether the client is connected to any server (in multiplayer).
   *
   * @return {@code true} if the client is connected to a server, {@code false} otherwise
   */
  boolean isConnected();

  /**
   * Retrieves the currently connected server.
   *
   * @return The server or {@code null} if the client isn't connected with any server
   * @see #isConnected()
   */
  ConnectedServer getConnectedServer();

  /**
   * Disconnects the client from the currently connected server or does nothing when not connected to any server.
   *
   * @see #isConnected()
   */
  void disconnectFromServer();

  /**
   * Connects the client with the given address and disconnects from the server the client is currently connected to.
   *
   * @param address The non-null address of the target server
   */
  void connectToServer(ServerAddress address);

}
