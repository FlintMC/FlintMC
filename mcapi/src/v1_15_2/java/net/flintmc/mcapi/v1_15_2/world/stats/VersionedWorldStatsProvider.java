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

package net.flintmc.mcapi.v1_15_2.world.stats;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.eventbus.EventBus;
import net.flintmc.framework.eventbus.event.subscribe.PostSubscribe;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe.Phase;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.event.DirectionalEvent.Direction;
import net.flintmc.mcapi.server.event.PacketEvent;
import net.flintmc.mcapi.server.event.ServerConnectEvent;
import net.flintmc.mcapi.server.event.ServerDisconnectEvent;
import net.flintmc.mcapi.world.stats.StatsCategory;
import net.flintmc.mcapi.world.stats.WorldStatType;
import net.flintmc.mcapi.world.stats.WorldStats;
import net.flintmc.mcapi.world.stats.WorldStatsProvider;
import net.flintmc.mcapi.world.stats.event.PlayerStatsUpdateEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.StatsScreen;
import net.minecraft.network.play.client.CClientStatusPacket;
import net.minecraft.network.play.client.CClientStatusPacket.State;
import net.minecraft.network.play.server.SStatisticsPacket;
import net.minecraft.stats.StatisticsManager;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Singleton
@Implement(WorldStatsProvider.class)
public class VersionedWorldStatsProvider implements WorldStatsProvider {

  private final VersionedWorldStatsMapper mapper;
  private final StatsScreenShadow shadow;

  private final EventBus eventBus;
  private final PlayerStatsUpdateEvent event;

  private CompletableFuture<WorldStats> pendingRequest;
  private WorldStats lastStats;

  @Inject
  private VersionedWorldStatsProvider(
      VersionedWorldStatsMapper mapper, EventBus eventBus, PlayerStatsUpdateEvent event) {
    this.mapper = mapper;
    this.eventBus = eventBus;
    this.event = event;

    StatsScreen screen = new StatsScreen(null, new StatisticsManager());
    screen.init(Minecraft.getInstance(), 1024, 1024);
    this.shadow = (StatsScreenShadow) screen;
    this.shadow.updateData();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public WorldStats getCurrentStats() {
    return this.lastStats != null ? this.lastStats
        : (this.lastStats = this.mapper.map(this.shadow));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public CompletableFuture<WorldStats> requestStatsUpdate() {
    if (this.pendingRequest == null) {
      if (Minecraft.getInstance().getConnection() == null) {
        throw new IllegalStateException("Cannot request stats while not connected to a server");
      }

      this.pendingRequest = new CompletableFuture<>();

      Minecraft.getInstance().getConnection()
          .sendPacket(new CClientStatusPacket(State.REQUEST_STATS));
    }

    return this.pendingRequest;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Map<WorldStatType, StatsCategory> getCategories() {
    return this.mapper.getCategories();
  }

  @PostSubscribe
  public void resetPendingRequest(ServerConnectEvent event) {
    this.reset();
  }

  @PostSubscribe
  public void resetPendingRequest(ServerDisconnectEvent event) {
    this.reset();
  }

  private void reset() {
    this.pendingRequest = null;
    this.lastStats = null;
  }

  @Subscribe(phase = Phase.ANY)
  public void processStatsResponse(PacketEvent event, Phase phase) {
    if (event.getDirection() != Direction.RECEIVE
        || !(event.getPacket() instanceof SStatisticsPacket)) {
      return;
    }

    if (phase == Phase.PRE) {
      this.eventBus.fireEvent(this.event, Phase.PRE);
      return;
    }

    SStatisticsPacket packet = (SStatisticsPacket) event.getPacket();
    packet.getStatisticMap()
        .forEach((stat, value) -> this.shadow.getStatsManager().setValue(null, stat, value));
    this.shadow.updateData();

    if (this.pendingRequest != null) {
      this.pendingRequest.complete(this.mapper.map(this.shadow));
      this.pendingRequest = null;
    }

    this.lastStats = this.mapper.map(this.shadow);

    this.eventBus.fireEvent(this.event, Phase.POST);
  }
}
