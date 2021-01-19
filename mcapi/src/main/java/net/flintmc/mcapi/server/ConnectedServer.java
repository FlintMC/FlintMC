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

import java.util.concurrent.CompletableFuture;
import net.flintmc.mcapi.resources.ResourceLocation;
import net.flintmc.mcapi.server.buffer.PacketBuffer;
import net.flintmc.mcapi.server.status.ServerStatus;
import net.flintmc.mcapi.server.status.ServerStatusResolver;

public interface ConnectedServer {

  /**
   * Retrieves the address of the server which the client is connected to.
   *
   * @return The address of the server or {@code null}, if the client is not connected to any server
   */
  ServerAddress getAddress();

  /**
   * Retrieves whether the client is connected or currently connecting to any server (in
   * multiplayer).
   *
   * @return {@code true} if the client is connected or currently connecting to a server, {@code
   * false} otherwise
   */
  boolean isConnected();

  /**
   * Uses {@link ServerStatusResolver#resolveStatus(ServerAddress)} to get the status of a server
   * for the server list.
   *
   * @return The non-null future which will be completed with the status or {@code null}, if the
   * status couldn't be retrieved
   * @throws IllegalStateException If the client is not connected with any server
   */
  CompletableFuture<ServerStatus> resolveStatus() throws IllegalStateException;

  /**
   * Sends a custom payload message to the currently connected server which can be used to
   * communicate with servers for any extra data in the client.
   *
   * @param identifier   The non-null identifier of the payload
   * @param packetBuffer The non-null packet buffer for the server
   * @throws IllegalStateException If the client is not connected with any server
   */
  void sendCustomPayload(ResourceLocation identifier, PacketBuffer packetBuffer);

  /**
   * Sends a custom payload message to the currently connected server which can be used to
   * communicate with servers for any extra data in the client.
   *
   * @param identifier The non-null identifier of the payload
   * @param payload    The non-null payload for the server
   * @throws IllegalStateException If the client is not connected with any server
   */
  void sendCustomPayload(ResourceLocation identifier, byte[] payload) throws IllegalStateException;

  /**
   * Retrieves a custom payload message from the currently connected server.
   *
   * @param identifier    The non-null identifier of the payload.
   * @param payloadBuffer The non-null payload buffer from the server.
   */
  void retrieveCustomPayload(String identifier, Object payloadBuffer);
}
