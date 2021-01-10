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

package net.flintmc.mcapi.v1_16_4.server;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.netty.buffer.Unpooled;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.concurrent.CompletableFuture;
import net.flintmc.framework.eventbus.EventBus;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe.Phase;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.resources.ResourceLocation;
import net.flintmc.mcapi.resources.ResourceLocationProvider;
import net.flintmc.mcapi.server.ConnectedServer;
import net.flintmc.mcapi.server.ServerAddress;
import net.flintmc.mcapi.server.ServerAddress.Factory;
import net.flintmc.mcapi.server.payload.RetrievePayloadEvent;
import net.flintmc.mcapi.server.payload.SendPayloadEvent;
import net.flintmc.mcapi.server.status.ServerStatus;
import net.flintmc.mcapi.server.status.ServerStatusResolver;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.play.ClientPlayNetHandler;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CCustomPayloadPacket;

@Singleton
@Implement(value = ConnectedServer.class, version = "1.16.4")
public class VersionedConnectedServer implements ConnectedServer {

  private final EventBus eventBus;
  private final SendPayloadEvent.Factory sendPayloadEventFactory;
  private final RetrievePayloadEvent.Factory retrievePayloadEventFactory;
  private final net.flintmc.mcapi.server.buffer.PacketBuffer.Factory packetBufferFactory;
  private final ResourceLocationProvider resourceLocationProvider;
  private final ServerStatusResolver statusResolver;
  private final Factory serverAddressFactory;

  @Inject
  public VersionedConnectedServer(
      EventBus eventBus,
      SendPayloadEvent.Factory sendPayloadEventFactory,
      RetrievePayloadEvent.Factory retrievePayloadEventFactory,
      net.flintmc.mcapi.server.buffer.PacketBuffer.Factory packetBufferFactory,
      ResourceLocationProvider resourceLocationProvider,
      ServerStatusResolver statusResolver,
      Factory serverAddressFactory) {
    this.eventBus = eventBus;
    this.sendPayloadEventFactory = sendPayloadEventFactory;
    this.retrievePayloadEventFactory = retrievePayloadEventFactory;
    this.packetBufferFactory = packetBufferFactory;
    this.resourceLocationProvider = resourceLocationProvider;
    this.statusResolver = statusResolver;
    this.serverAddressFactory = serverAddressFactory;
  }

  private SocketAddress getRawAddress() {
    if (Minecraft.getInstance().currentScreen instanceof ConnectingScreenShadow) {
      ConnectingScreenShadow screen = (ConnectingScreenShadow) Minecraft
          .getInstance().currentScreen;
      return screen.getNetworkManager().getRemoteAddress();
    }

    ClientPlayNetHandler connection = Minecraft.getInstance().getConnection();
    return connection != null ? connection.getNetworkManager().getRemoteAddress() : null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ServerAddress getAddress() {
    SocketAddress address = this.getRawAddress();
    if (address instanceof InetSocketAddress) {
      InetSocketAddress inetAddress = (InetSocketAddress) address;
      return this.serverAddressFactory.create(
          inetAddress.getAddress().getHostAddress(), inetAddress.getPort());
    }

    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isConnected() {
    return this.getRawAddress() instanceof InetSocketAddress;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public CompletableFuture<ServerStatus> resolveStatus() {
    ServerAddress address = this.getAddress();
    Preconditions.checkState(address != null, "Not connected with any server");

    try {
      return this.statusResolver.resolveStatus(address);
    } catch (UnknownHostException exception) {
      return CompletableFuture.completedFuture(null);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void sendCustomPayload(
      ResourceLocation identifier, net.flintmc.mcapi.server.buffer.PacketBuffer packetBuffer) {
    ClientPlayNetHandler handler = this.getConnection();

    // Creates a send payload event
    SendPayloadEvent sendPayloadEvent =
        this.sendPayloadEventFactory.create(identifier, packetBuffer);
    // Fires the send payload event
    this.eventBus.fireEvent(sendPayloadEvent, Phase.PRE);

    // Cancel the send attempt
    if (sendPayloadEvent.isCancelled()) {
      return;
    }

    // Creates a new minecraft packet buffer
    PacketBuffer buffer =
        new PacketBuffer(Unpooled.wrappedBuffer(sendPayloadEvent.getBuffer().array()));

    // Sends a custom payload packet to the connected server
    handler.sendPacket(new CCustomPayloadPacket(identifier.getHandle(), buffer));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void sendCustomPayload(ResourceLocation identifier, byte[] payload) {
    this.sendCustomPayload(identifier, this.packetBufferFactory.create(payload));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void retrieveCustomPayload(String identifier, Object payload) {
    if (!(payload instanceof PacketBuffer)) {
      return;
    }

    PacketBuffer packetBuffer = (PacketBuffer) payload;
    ResourceLocation resourceLocation;

    // Whether the identifier has a colon.
    if (!identifier.contains(":")) {
      // Creates a fallback identifier
      resourceLocation = this.resourceLocationProvider.get("flintmc", identifier.toLowerCase());
    } else {
      String[] split = identifier.split(":");
      // Creates the received identifier as resource location
      resourceLocation = this.resourceLocationProvider.get(split[0], split[1]);
    }

    // Creates a retrieve payload event
    RetrievePayloadEvent retrievePayloadEvent =
        this.retrievePayloadEventFactory.create(
            resourceLocation, this.packetBufferFactory.create(packetBuffer));

    // Fired the retrieve payload event
    this.eventBus.fireEvent(retrievePayloadEvent, Phase.PRE);
  }

  private ClientPlayNetHandler getConnection() {
    ClientPlayNetHandler handler = Minecraft.getInstance().getConnection();
    Preconditions.checkState(handler != null, "Not connected with any server");
    return handler;
  }
}
