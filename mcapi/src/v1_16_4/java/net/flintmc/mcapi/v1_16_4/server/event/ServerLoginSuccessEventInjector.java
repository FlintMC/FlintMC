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

package net.flintmc.mcapi.v1_16_4.server.event;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import net.flintmc.framework.eventbus.EventBus;
import net.flintmc.framework.stereotype.type.Type;
import net.flintmc.mcapi.server.ServerAddress;
import net.flintmc.mcapi.server.event.ServerLoginSuccessEvent;
import net.flintmc.transform.hook.Hook;
import net.minecraft.client.network.login.ClientLoginNetHandler;
import net.minecraft.network.login.server.SLoginSuccessPacket;

@Singleton
public class ServerLoginSuccessEventInjector {

  private final EventBus eventBus;
  private final ServerAddress.Factory addressFactory;
  private final ServerLoginSuccessEvent.Factory eventFactory;

  @Inject
  public ServerLoginSuccessEventInjector(
      EventBus eventBus,
      ServerAddress.Factory addressFactory,
      ServerLoginSuccessEvent.Factory eventFactory) {
    this.eventBus = eventBus;
    this.addressFactory = addressFactory;
    this.eventFactory = eventFactory;
  }

  @Hook(
      executionTime = {Hook.ExecutionTime.BEFORE, Hook.ExecutionTime.AFTER},
      className = "net.minecraft.client.network.login.ClientLoginNetHandler",
      methodName = "handleLoginSuccess",
      parameters = @Type(reference = SLoginSuccessPacket.class))
  public void handleLoginSuccess(
      @Named("instance") Object instance, Hook.ExecutionTime executionTime) {
    ClientLoginNetHandler handler = (ClientLoginNetHandler) instance;
    SocketAddress socketAddress = handler.getNetworkManager().getRemoteAddress();

    ServerAddress address =
        socketAddress instanceof InetSocketAddress
            ? this.addressFactory.create((InetSocketAddress) socketAddress)
            : null;
    this.eventBus.fireEvent(this.eventFactory.create(address), executionTime);
  }
}
