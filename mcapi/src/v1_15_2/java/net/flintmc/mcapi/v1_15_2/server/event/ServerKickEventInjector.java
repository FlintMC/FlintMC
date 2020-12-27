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
import com.google.inject.name.Named;
import net.flintmc.framework.eventbus.EventBus;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe.Phase;
import net.flintmc.framework.stereotype.type.Type;
import net.flintmc.mcapi.chat.MinecraftComponentMapper;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.server.ConnectedServer;
import net.flintmc.mcapi.server.ServerAddress;
import net.flintmc.mcapi.server.ServerController;
import net.flintmc.mcapi.server.event.ServerKickEvent;
import net.flintmc.mcapi.v1_15_2.server.event.shadow.AccessibleDisconnectedScreen;
import net.flintmc.transform.hook.Hook;
import net.minecraft.util.text.ITextComponent;

@Singleton
public class ServerKickEventInjector {

  private final EventBus eventBus;
  private final ServerController controller;
  private final ServerAddress.Factory addressFactory;
  private final ServerKickEvent.Factory eventFactory;
  private final MinecraftComponentMapper componentMapper;

  @Inject
  public ServerKickEventInjector(
      EventBus eventBus,
      ServerController controller,
      ServerAddress.Factory addressFactory,
      ServerKickEvent.Factory eventFactory,
      MinecraftComponentMapper componentMapper) {
    this.eventBus = eventBus;
    this.controller = controller;
    this.addressFactory = addressFactory;
    this.eventFactory = eventFactory;
    this.componentMapper = componentMapper;
  }

  @Hook(
      executionTime = Hook.ExecutionTime.BEFORE,
      className = "net.minecraft.client.network.play.ClientPlayNetHandler",
      methodName = "onDisconnect",
      parameters = @Type(reference = ITextComponent.class))
  public void handleKick(@Named("args") Object[] args) {
    ConnectedServer server = this.controller.getConnectedServer();

    ServerAddress address = server != null ? server.getAddress() : null;
    ChatComponent reason = this.componentMapper.fromMinecraft(args[0]);

    this.eventBus.fireEvent(this.eventFactory.create(address, reason), Phase.PRE);
  }
}
