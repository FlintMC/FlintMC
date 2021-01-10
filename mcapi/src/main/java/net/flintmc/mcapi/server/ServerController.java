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

/**
 * The controller for the currently connected server.
 */
public interface ServerController {

  /**
   * Retrieves whether the client is connected or currently connecting to any server (in
   * multiplayer).
   *
   * @return {@code true} if the client is connected or currently connecting to a server, {@code
   * false} otherwise
   */
  boolean isConnected();

  /**
   * Retrieves the currently connected server.
   *
   * @return The server or {@code null} if the client isn't connected with or currently connecting
   * to any server
   * @see #isConnected()
   */
  ConnectedServer getConnectedServer();

  /**
   * Disconnects the client from the currently connected server (or singleplayer world) or does
   * nothing when not connected to any server/singleplayer world.
   *
   * @see #isConnected()
   */
  void disconnectFromServer();

  /**
   * Connects the client with the given address and disconnects from the server the client is
   * currently connected to.
   *
   * @param address The non-null address of the target server
   */
  void connectToServer(ServerAddress address);
}
