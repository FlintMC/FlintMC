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
 * Represents the minecraft server list.
 */
public interface ServerList {

  /**
   * Saves the server list to disk.
   */
  void saveServerList();

  /**
   * Gets a server list entry.
   *
   * @param index the index of the entry to get
   * @return the server list entry
   */
  ServerData getServer(int index);

  /**
   * @return the size of the server list
   */
  int size();

  /**
   * Updates the content of the entry at a given index to tbe provided data.
   *
   * @param index  the index of the server list entry to update
   * @param server the data to update the entry from
   */
  void updateServerData(int index, ServerData server);

  /**
   * Adds a server to the server list
   *
   * @param server the data describing the server to add
   */
  void addServer(ServerData server);

  /**
   * Adds a server to the server list at a given index.
   *
   * @param index  the index at which to add the server to the list
   * @param server the data describing the server to add
   */
  void addServer(int index, ServerData server);

  /**
   * Removes a server from the server list.
   *
   * @param index the index of the server to remove.
   */
  void removeServer(int index);
}
