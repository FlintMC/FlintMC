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

package net.flintmc.mcapi.v1_16_5.server.event;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import net.flintmc.framework.eventbus.EventBus;
import net.flintmc.framework.eventbus.event.subscribe.PostSubscribe;
import net.flintmc.framework.eventbus.event.subscribe.PreSubscribe;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe.Phase;
import net.flintmc.framework.stereotype.type.Type;
import net.flintmc.mcapi.event.DirectionalEvent.Direction;
import net.flintmc.mcapi.server.ServerAddress;
import net.flintmc.mcapi.server.ServerController;
import net.flintmc.mcapi.server.event.PacketEvent;
import net.flintmc.mcapi.server.event.PacketEvent.ProtocolPhase;
import net.flintmc.mcapi.server.event.ServerConnectEvent;
import net.flintmc.transform.hook.Hook;
import net.minecraft.network.login.server.SLoginSuccessPacket;

@Singleton
public class ServerConnectEventInjector {

  private final EventBus eventBus;
  private final ServerController serverController;
  private final ServerAddress.Factory addressFactory;
  private final ServerConnectEvent.Factory eventFactory;

  @Inject
  public ServerConnectEventInjector(
      EventBus eventBus,
      ServerController serverController,
      ServerAddress.Factory addressFactory,
      ServerConnectEvent.Factory eventFactory) {
    this.eventBus = eventBus;
    this.serverController = serverController;
    this.addressFactory = addressFactory;
    this.eventFactory = eventFactory;
  }

  @Hook(
      executionTime = Hook.ExecutionTime.BEFORE,
      className = "net.minecraft.client.gui.screen.ConnectingScreen",
      methodName = "connect",
      parameters = {@Type(reference = String.class), @Type(reference = int.class)},
      version = "1.16.5")
  public void handleConnect(Hook.ExecutionTime executionTime, @Named("args") Object[] args) {
    ServerAddress address = this.addressFactory.create((String) args[0], (int) args[1]);
    this.eventBus.fireEvent(this.eventFactory.create(address), executionTime);
  }


  @PreSubscribe(version = "1.16.5")
  public void handleLoginSuccess(PacketEvent event) {
    if (event.getDirection() != Direction.RECEIVE || event.getPhase() != ProtocolPhase.LOGIN
        || !(event.getPacket() instanceof SLoginSuccessPacket)
        || this.serverController.getConnectedServer() == null) {
      return;
    }

    ServerAddress address = this.serverController.getConnectedServer().getAddress();
    this.eventBus.fireEvent(this.eventFactory.create(address), Phase.POST);
  }

}
