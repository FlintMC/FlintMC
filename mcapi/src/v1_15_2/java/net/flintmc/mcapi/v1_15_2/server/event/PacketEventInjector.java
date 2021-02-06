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
import io.netty.util.concurrent.GenericFutureListener;
import net.flintmc.framework.eventbus.EventBus;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe.Phase;
import net.flintmc.framework.inject.logging.InjectLogger;
import net.flintmc.framework.stereotype.type.Type;
import net.flintmc.mcapi.event.DirectionalEvent;
import net.flintmc.mcapi.server.event.PacketEvent;
import net.flintmc.transform.hook.Hook;
import net.flintmc.transform.hook.HookResult;
import net.minecraft.network.INetHandler;
import net.minecraft.network.IPacket;
import net.minecraft.network.ProtocolType;
import org.apache.logging.log4j.Logger;

@Singleton
public class PacketEventInjector {

  private final Logger logger;
  private final EventBus eventBus;
  private final PacketEvent.Factory eventFactory;

  @Inject
  private PacketEventInjector(
      @InjectLogger Logger logger, EventBus eventBus, PacketEvent.Factory eventFactory) {
    this.logger = logger;
    this.eventBus = eventBus;
    this.eventFactory = eventFactory;
  }

  @Hook(
      executionTime = Hook.ExecutionTime.BEFORE,
      className = "net.minecraft.network.NetworkManager",
      methodName = "processPacket",
      parameters = {@Type(reference = IPacket.class), @Type(reference = INetHandler.class)},
      version = "1.15.2")
  public HookResult processIncomingPacket(@Named("args") Object[] args) {
    Object packet = args[0];
    ProtocolType type = ProtocolType.getFromPacket((IPacket<?>) packet);
    if (type == null) {
      this.logger.warn(
          "Unknown packet type when receiving a packet which doesn't have a protocol type registered: {}",
          packet.getClass().getName());
      return HookResult.CONTINUE;
    }

    PacketEvent.ProtocolPhase phase = this.getFlintPhaseFromType(type);
    PacketEvent event = this.eventFactory.create(packet, phase, DirectionalEvent.Direction.RECEIVE);
    this.eventBus.fireEvent(event, Phase.PRE);

    // Cannot be fired in POST because the processPacket method throws an exception when not
    // invoked on the main thread

    return event.isCancelled() ? HookResult.BREAK : HookResult.CONTINUE;
  }

  @Hook(
      executionTime = {Hook.ExecutionTime.BEFORE, Hook.ExecutionTime.AFTER},
      className = "net.minecraft.network.NetworkManager",
      methodName = "dispatchPacket",
      parameters = {
          @Type(reference = IPacket.class),
          @Type(reference = GenericFutureListener.class)
      },
      version = "1.15.2")
  public HookResult dispatchOutgoingPacket(
      @Named("args") Object[] args, Hook.ExecutionTime executionTime) {
    Object packet = args[0];
    ProtocolType type = ProtocolType.getFromPacket((IPacket<?>) packet);
    if (type == null) {
      this.logger.warn(
          "Unknown packet type when sending a packet which doesn't have a protocol type registered: {}",
          packet.getClass().getName());
      return HookResult.CONTINUE;
    }

    PacketEvent.ProtocolPhase phase = this.getFlintPhaseFromType(type);
    PacketEvent event = this.eventFactory.create(packet, phase, DirectionalEvent.Direction.SEND);
    this.eventBus.fireEvent(event, executionTime);

    return event.isCancelled() ? HookResult.BREAK : HookResult.CONTINUE;
  }

  private PacketEvent.ProtocolPhase getFlintPhaseFromType(ProtocolType type) {
    switch (type) {
      case HANDSHAKING:
        return PacketEvent.ProtocolPhase.HANDSHAKE;
      case PLAY:
        return PacketEvent.ProtocolPhase.PLAY;
      case STATUS:
        return PacketEvent.ProtocolPhase.STATUS;
      case LOGIN:
        return PacketEvent.ProtocolPhase.LOGIN;
      default:
        throw new IllegalStateException("Unexpected value: " + type);
    }
  }
}
