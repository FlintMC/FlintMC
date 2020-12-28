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

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.eventbus.EventBus;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.server.ServerAddress;
import net.flintmc.mcapi.server.ServerData;
import net.flintmc.mcapi.server.event.ServerListUpdateEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerList;

/** {@inheritDoc} */
@Singleton
@Implement(value = net.flintmc.mcapi.server.ServerList.class, version = "1.16.4")
public class VersionedServerList implements net.flintmc.mcapi.server.ServerList {

  private final ServerList mcServerList;
  private final ServerData.Factory serverDataFactory;
  private final ServerAddress.Factory serverAddressFactory;
  private final EventBus eventBus;
  private final ServerListUpdateEvent.Factory updateEventFactory;

  @Inject
  private VersionedServerList(
      ServerData.Factory serverDataFactory,
      ServerAddress.Factory serverAddressFactory,
      EventBus eventBus,
      ServerListUpdateEvent.Factory updateEventFactory) {
    this.serverDataFactory = serverDataFactory;
    this.serverAddressFactory = serverAddressFactory;
    this.eventBus = eventBus;
    this.updateEventFactory = updateEventFactory;
    this.mcServerList = new ServerList(Minecraft.getInstance());
  }

  /** {@inheritDoc} */
  @Override
  public void saveServerList() {
    this.mcServerList.saveServerList();
  }

  /** {@inheritDoc} */
  @Override
  public ServerData getServer(int index) {
    net.minecraft.client.multiplayer.ServerData data = this.mcServerList.getServerData(index);
    return this.serverDataFactory.create(
        data.serverName,
        this.serverAddressFactory.parse(data.serverIP),
        ServerData.ResourceMode.valueOf(data.getResourceMode().name()));
  }

  /** {@inheritDoc} */
  @Override
  public int size() {
    return this.mcServerList.countServers();
  }

  /** {@inheritDoc} */
  @Override
  public void updateServerData(int index, ServerData server) {
    ServerListUpdateEvent event =
        this.updateEventFactory.create(index, server, ServerListUpdateEvent.Type.UPDATE);
    if (this.eventBus.fireEvent(event, Subscribe.Phase.PRE).isCancelled()) {
      return;
    }

    net.minecraft.client.multiplayer.ServerData data = this.mcServerList.getServerData(index);
    data.serverIP = server.getServerAddress().getIP() + ":" + server.getServerAddress().getPort();
    data.serverName = server.getName();
    data.setResourceMode(
        net.minecraft.client.multiplayer.ServerData.ServerResourceMode.valueOf(
            server.getResourceMode().name()));

    this.eventBus.fireEvent(event, Subscribe.Phase.POST);
  }

  /** {@inheritDoc} */
  @Override
  public void addServer(ServerData server) {
    this.mcServerList.addServerData(createNMSServerData(server));
  }

  /** {@inheritDoc} */
  @Override
  public void addServer(int index, ServerData server) {
    ((ServerListShadow) this.mcServerList)
        .getServerDataList()
        .add(index, createNMSServerData(server));
  }

  /** {@inheritDoc} */
  private net.minecraft.client.multiplayer.ServerData createNMSServerData(ServerData server) {
    String ip = server.getServerAddress().getIP() + ":" + server.getServerAddress().getPort();
    net.minecraft.client.multiplayer.ServerData data =
        new net.minecraft.client.multiplayer.ServerData(server.getName(), ip, false);
    data.setResourceMode(
        net.minecraft.client.multiplayer.ServerData.ServerResourceMode.valueOf(
            server.getResourceMode().name()));
    return data;
  }

  /** {@inheritDoc} */
  @Override
  public void removeServer(int index) {
    ((ServerListShadow) this.mcServerList).getServerDataList().remove(index);
  }
}
