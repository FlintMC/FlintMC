package net.labyfy.component.server;

import com.google.inject.assistedinject.Assisted;
import net.labyfy.component.inject.assisted.AssistedFactory;

/**
 * An address of a minecraft server.
 */
public interface ServerAddress {

  /**
   * Retrieves the IP out of this address.
   *
   * @return The non-null IP out of this address
   */
  String getIP();

  /**
   * Retrieves the port out of this address.
   *
   * @return The port out of this address in the range from 0 - 65535
   */
  int getPort();

  /**
   * Factory for the {@link ServerAddress}.
   */
  @AssistedFactory(ServerAddress.class)
  interface Factory {

    /**
     * Parses an address out of the given string. If the port 25565 is provided, this method will try to retrieve the
     * content of the _minecraft._tcp SRV record. The address can be IPv4 or IPv6.
     *
     * @param rawAddress The raw address to be parsed in the format IPv4:PORT or [IPv6]:PORT
     * @return The new non-null address
     */
    ServerAddress parse(@Assisted("rawAddress") String rawAddress);

    /**
     * Creates a new address out of the given IP and port.
     *
     * @param ip   The non-null IP of the new address.
     * @param port The non-null port of the new address in the range from 0 - 65535
     * @return The new non-null address
     */
    ServerAddress create(@Assisted("ip") String ip, @Assisted("port") int port);

  }

}
