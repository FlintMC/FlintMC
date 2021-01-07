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

package net.flintmc.mcapi.v1_15_2.server.event;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.eventbus.EventBus;
import net.flintmc.mcapi.server.ServerAddress;
import net.flintmc.mcapi.server.ServerController;
import net.flintmc.mcapi.server.event.ServerDisconnectEvent;
import net.flintmc.transform.hook.Hook;

@Singleton
public class ServerDisconnectEventInjector {

  private final EventBus eventBus;
  private final ServerController controller;
  private final ServerDisconnectEvent.Factory eventFactory;

  @Inject
  public ServerDisconnectEventInjector(
      EventBus eventBus, ServerController controller, ServerDisconnectEvent.Factory eventFactory) {
    this.eventBus = eventBus;
    this.controller = controller;
    this.eventFactory = eventFactory;
  }

  @Hook(
      className = "net.minecraft.client.world.ClientWorld",
      methodName = "sendQuittingDisconnectingPacket",
      version = "1.15.2")
  public void handleDisconnect(Hook.ExecutionTime executionTime) {
    ServerAddress address =
        this.controller.isConnected() ? this.controller.getConnectedServer().getAddress() : null;
    this.eventBus.fireEvent(this.eventFactory.create(address), executionTime);
  }
}
