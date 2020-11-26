package net.flintmc.mcapi.v1_15_2.server.event;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import io.netty.util.concurrent.GenericFutureListener;
import net.flintmc.framework.eventbus.EventBus;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
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
      parameters = {@Type(reference = IPacket.class), @Type(reference = INetHandler.class)})
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
    this.eventBus.fireEvent(event, Subscribe.Phase.PRE);

    return event.isCancelled() ? HookResult.BREAK : HookResult.CONTINUE;
  }

  @Hook(
      executionTime = {Hook.ExecutionTime.BEFORE, Hook.ExecutionTime.AFTER},
      className = "net.minecraft.network.NetworkManager",
      methodName = "dispatchPacket",
      parameters = {
        @Type(reference = IPacket.class),
        @Type(reference = GenericFutureListener.class)
      })
  public HookResult dispatchOutgoingPacket(@Named("args") Object[] args, Hook.ExecutionTime executionTime) {
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
