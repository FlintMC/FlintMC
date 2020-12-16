package net.flintmc.mcapi.server.status;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;

/**
 * The version of the server in the {@link ServerStatus}.
 */
public interface ServerVersion {

  /**
   * Retrieves the name sent by the server which will be displayed by the client if the version is
   * not compatible with the client version. For example the version could be something like
   * "BungeeCord 1.8.x - 1.16.x".
   *
   * @return The non-null name of the version
   * @see #isCompatible()
   */
  String getName();

  /**
   * Retrieves the protocol version sent by the server which will be used to check whether this
   * client is compatible with the server. For example Minecraft 1.16.3 uses 753 as the protocol
   * version.
   *
   * @return The protocol version by the server
   */
  int getProtocolVersion();

  /**
   * Checks whether this client is compatible with the server version.
   *
   * @return {@code true} if it is compatible, {@code false} otherwise
   * @see #getProtocolVersion()
   */
  boolean isCompatible();

  /**
   * Factory for the {@link ServerVersion}.
   */
  @AssistedFactory(ServerVersion.class)
  interface Factory {

    /**
     * Creates a new server version with the given values
     *
     * @param name            The non-null name of the version, this will be displayed if {@code
     *                        compatible} is {@code false}
     * @param protocolVersion The protocol version sent by the server
     * @param compatible      Whether the protocol version sent by the server is compatible with the
     *                        client version or not
     * @return The new non-null server version
     */
    ServerVersion create(
        @Assisted("name") String name,
        @Assisted("protocolVersion") int protocolVersion,
        @Assisted("compatible") boolean compatible);
  }
}
