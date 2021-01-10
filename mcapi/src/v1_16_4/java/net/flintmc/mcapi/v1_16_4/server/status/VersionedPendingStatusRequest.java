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

package net.flintmc.mcapi.v1_16_4.server.status;

import com.google.common.base.Preconditions;
import com.google.gson.JsonParseException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.CompletableFuture;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.framework.inject.logging.InjectLogger;
import net.flintmc.mcapi.chat.MinecraftComponentMapper;
import net.flintmc.mcapi.internal.server.status.DefaultServerFavicon;
import net.flintmc.mcapi.internal.server.status.DefaultServerPlayers;
import net.flintmc.mcapi.internal.server.status.DefaultServerVersion;
import net.flintmc.mcapi.player.gameprofile.GameProfile;
import net.flintmc.mcapi.player.serializer.gameprofile.GameProfileSerializer;
import net.flintmc.mcapi.server.ServerAddress;
import net.flintmc.mcapi.server.status.ServerFavicon;
import net.flintmc.mcapi.server.status.ServerPlayers;
import net.flintmc.mcapi.server.status.ServerStatus;
import net.flintmc.mcapi.server.status.ServerVersion;
import net.flintmc.mcapi.server.status.pending.PendingStatusRequest;
import net.flintmc.mcapi.server.status.pending.PendingStatusState;
import net.minecraft.client.network.status.IClientStatusNetHandler;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.ProtocolType;
import net.minecraft.network.ServerStatusResponse;
import net.minecraft.network.handshake.client.CHandshakePacket;
import net.minecraft.network.status.client.CPingPacket;
import net.minecraft.network.status.client.CServerQueryPacket;
import net.minecraft.network.status.server.SPongPacket;
import net.minecraft.network.status.server.SServerInfoPacket;
import net.minecraft.util.SharedConstants;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import org.apache.logging.log4j.Logger;

@Implement(value = PendingStatusRequest.class, version = "1.16.4")
public class VersionedPendingStatusRequest
    implements PendingStatusRequest, IClientStatusNetHandler {

  private static final ServerVersion DEFAULT_VERSION =
      new DefaultServerVersion("unknown", -1, false);
  private static final ServerPlayers DEFAULT_PLAYERS =
      new DefaultServerPlayers(0, 0, new GameProfile[0]);

  private final Logger logger;
  private final GameProfileSerializer<com.mojang.authlib.GameProfile> gameProfileSerializer;
  private final MinecraftComponentMapper componentMapper;
  private final ServerStatus.Factory statusFactory;
  private final ServerFavicon.Factory faviconFactory;
  private final ServerPlayers.Factory playersFactory;
  private final ServerVersion.Factory versionFactory;

  private final ServerFavicon defaultFavicon;
  private final CompletableFuture<ServerStatus> future;
  private final ServerAddress targetAddress;

  private NetworkManager networkManager;
  private ServerStatusResponse response;
  private long pingBegin;
  private PendingStatusState state;
  private long startTimestamp = -1;

  @AssistedInject
  public VersionedPendingStatusRequest(
      @Assisted("targetAddress") ServerAddress targetAddress,
      @Assisted("defaultFavicon") ServerFavicon defaultFavicon,
      @InjectLogger Logger logger,
      GameProfileSerializer gameProfileSerializer,
      MinecraftComponentMapper componentMapper,
      ServerStatus.Factory statusFactory,
      ServerFavicon.Factory faviconFactory,
      ServerPlayers.Factory playersFactory,
      ServerVersion.Factory versionFactory) {
    this.logger = logger;
    this.targetAddress = targetAddress;
    this.defaultFavicon = defaultFavicon;
    this.gameProfileSerializer = gameProfileSerializer;
    this.componentMapper = componentMapper;
    this.statusFactory = statusFactory;
    this.faviconFactory = faviconFactory;
    this.playersFactory = playersFactory;
    this.versionFactory = versionFactory;

    this.future = new CompletableFuture<>();
    this.state = PendingStatusState.IDLE;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public CompletableFuture<ServerStatus> getFuture() {
    return this.future;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ServerAddress getTargetAddress() {
    return this.targetAddress;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public PendingStatusState getState() {
    return this.state;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void start() throws UnknownHostException {
    Preconditions.checkState(
        this.state == PendingStatusState.IDLE, "Every request can only be started once");

    this.startTimestamp = System.currentTimeMillis();

    this.state = PendingStatusState.CONNECTING;
    this.networkManager =
        NetworkManager.createNetworkManagerAndConnect(
            InetAddress.getByName(this.targetAddress.getIP()), this.targetAddress.getPort(), false);
    this.networkManager.setNetHandler(this);

    this.networkManager.sendPacket(
        new CHandshakePacket(
            this.targetAddress.getIP(), this.targetAddress.getPort(), ProtocolType.STATUS));
    this.networkManager.sendPacket(new CServerQueryPacket());
    this.state = PendingStatusState.RECEIVING;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public long getStartTimestamp() {
    return this.startTimestamp;
  }

  @Override
  public void handleServerInfo(SServerInfoPacket packet) {
    try {
      this.response = packet.getResponse();
    } catch (JsonParseException exception) {
      this.logger.error(
          "Invalid response received from the server @" + this.targetAddress, exception);
    }

    this.state = PendingStatusState.PINGING;
    this.pingBegin = System.currentTimeMillis();
    this.networkManager.sendPacket(new CPingPacket(this.pingBegin));
  }

  @Override
  public void handlePong(SPongPacket packetIn) {
    this.finish();
  }

  @Override
  public void onDisconnect(ITextComponent reason) {
    if (this.response == null) {
      this.future.complete(null);
    }
  }

  private void finish() {
    this.state = PendingStatusState.FINISHING;
    if (this.response == null) {
      this.future.complete(null);
      this.state = PendingStatusState.FAILED;
      return;
    }

    long ping = System.currentTimeMillis() - this.pingBegin;

    ServerStatusResponse.Players players = this.response.getPlayers();

    ServerFavicon favicon =
        this.response.getFavicon() != null
            && this.response.getFavicon().startsWith(DefaultServerFavicon.PREFIX)
            ? this.faviconFactory.createCustom(this.response.getFavicon())
            : this.defaultFavicon;

    int requiredProtocol = SharedConstants.getVersion().getProtocolVersion();
    int protocol = this.response.getVersion().getProtocol();

    ServerStatus status =
        this.statusFactory.create(
            this.targetAddress,
            this.response.getVersion() == null
                ? DEFAULT_VERSION
                : this.versionFactory.create(
                    this.response.getVersion().getName(), protocol, protocol == requiredProtocol),
            this.response.getPlayers() == null
                ? DEFAULT_PLAYERS
                : this.playersFactory.create(
                    players.getOnlinePlayerCount(), players.getMaxPlayers(), this.createPlayers()),
            this.response.getServerDescription() == null
                ? null
                : this.componentMapper.fromMinecraft(this.response.getServerDescription()),
            favicon,
            ping);

    this.future.complete(status);

    this.networkManager.closeChannel(new TranslationTextComponent("multiplayer.status.finished"));
    this.state = PendingStatusState.FINISHED;
  }

  private GameProfile[] createPlayers() {
    com.mojang.authlib.GameProfile[] baseProfiles = this.response.getPlayers().getPlayers();
    if (baseProfiles == null) {
      baseProfiles = new com.mojang.authlib.GameProfile[0];
    }

    GameProfile[] profiles = new GameProfile[baseProfiles.length];
    for (int i = 0; i < baseProfiles.length; i++) {
      profiles[i] = this.gameProfileSerializer.deserialize(baseProfiles[i]);
    }

    return profiles;
  }

  @Override
  public NetworkManager getNetworkManager() {
    return this.networkManager;
  }
}
