/*
 * FlintMC
 * Copyright (C) 2020-2021 LabyMedia GmbH and contributors
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package net.flintmc.mcapi.server;

import java.net.InetSocketAddress;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;

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
     * Parses an address out of the given string. If the port 25565 is provided, this method will
     * try to retrieve the content of the _minecraft._tcp SRV record. The address can be IPv4 or
     * IPv6.
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

    /**
     * Creates a new address out of the given inet socket address.
     *
     * @param address The non-null address to be converted to a {@link ServerAddress}
     * @return The new non-null address
     */
    ServerAddress create(@Assisted("socketAddress") InetSocketAddress address);
  }
}
