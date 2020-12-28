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

import java.net.UnknownHostException;
import java.util.concurrent.CompletableFuture;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.server.status.ServerStatus;

/**
 * Describes an entry of the server list.
 */
public interface ServerData {

  /**
   * Loads the {@link ServerStatus} of the server that is described by this instance.
   *
   * @return a completable future of the {@link ServerStatus}.
   * @throws UnknownHostException if the server couldn't be resolved
   */
  CompletableFuture<ServerStatus> loadStatus() throws UnknownHostException;

  /**
   * @return the name of the server described by this instance
   */
  String getName();

  /**
   * @return the server address of the server described by this instance
   */
  ServerAddress getServerAddress();

  /**
   * @return the resource mode of this server list entry
   */
  ResourceMode getResourceMode();

  /**
   * The resource mode of a server list entry.
   */
  enum ResourceMode {
    /**
     * Accepts server resource packs without asking.
     */
    ENABLED,
    /**
     * Doesn't accept server resource packs.
     */
    DISABLED,
    /**
     * Prompts the player whether to accept or decline server resource packs.
     */
    PROMPT
  }

  @AssistedFactory(ServerData.class)
  interface Factory {

    /**
     * Creates a new server list entry (but doesn't add it to the actual server list).
     *
     * @param name         the name of the server
     * @param address      the address of the server
     * @param resourceMode the resource mode of the server
     * @return the new {@link ServerData} instance
     */
    ServerData create(@Assisted String name, @Assisted ServerAddress address,
        @Assisted ResourceMode resourceMode);
  }
}
