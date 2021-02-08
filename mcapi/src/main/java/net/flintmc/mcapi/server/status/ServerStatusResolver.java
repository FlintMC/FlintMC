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

import net.flintmc.mcapi.server.ServerAddress;

import java.net.UnknownHostException;
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
}
