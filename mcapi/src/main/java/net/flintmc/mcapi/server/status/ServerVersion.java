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
