package net.flintmc.mcapi.v1_15_2.server.event;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.eventbus.EventBus;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.framework.stereotype.NameSpacedKey;
import net.flintmc.mcapi.event.DirectionalEvent.Direction;
import net.flintmc.mcapi.server.ConnectedServer;
import net.flintmc.mcapi.server.ServerAddress;
import net.flintmc.mcapi.server.ServerController;
import net.flintmc.mcapi.server.event.PacketEvent;
import net.flintmc.mcapi.server.event.ServerCustomPayloadEvent;
import net.flintmc.mcapi.v1_15_2.server.event.shadow.AccessibleCCustomPayloadPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SCustomPayloadPlayPacket;
import net.minecraft.util.ResourceLocation;

@Singleton
public class ServerCustomPayloadEventInjector {

  private final ServerController controller;
  private final EventBus eventBus;
  private final ServerCustomPayloadEvent.Factory customPayloadFactory;

  @Inject
  public ServerCustomPayloadEventInjector(
      ServerController controller,
      EventBus eventBus,
      ServerCustomPayloadEvent.Factory customPayloadFactory) {
    this.controller = controller;
    this.eventBus = eventBus;
    this.customPayloadFactory = customPayloadFactory;
  }

  @Subscribe(phase = Subscribe.Phase.PRE)
  public void handlePreCustomPayloadSend(PacketEvent event) {
    this.firePayloadSendEvent(event, Subscribe.Phase.PRE);
  }

  @Subscribe(phase = Subscribe.Phase.POST)
  public void handlePostCustomPayloadSend(PacketEvent event) {
    this.firePayloadSendEvent(event, Subscribe.Phase.POST);
  }

  private void firePayloadSendEvent(PacketEvent event, Subscribe.Phase phase) {
    ServerCustomPayloadEvent payloadEvent = null;

    if (event.getPacket() instanceof AccessibleCCustomPayloadPacket) {
      AccessibleCCustomPayloadPacket packet = (AccessibleCCustomPayloadPacket) event.getPacket();

      payloadEvent =
          this.fireCustomPayload(Direction.SEND, packet.getChannelName(), packet.getData(), phase);
    }

    if (event.getPacket() instanceof SCustomPayloadPlayPacket) {
      SCustomPayloadPlayPacket packet = (SCustomPayloadPlayPacket) event.getPacket();

      payloadEvent =
          this.fireCustomPayload(
              Direction.RECEIVE, packet.getChannelName(), packet.getBufferData(), phase);
    }

    if (payloadEvent != null && payloadEvent.isCancelled()) {
      event.setCancelled(true);
    }
  }

  private ServerCustomPayloadEvent fireCustomPayload(
      Direction direction,
      ResourceLocation resourceLocation,
      PacketBuffer buffer,
      Subscribe.Phase phase) {
    ConnectedServer server = this.controller.getConnectedServer();
    ServerAddress address = server != null ? server.getAddress() : null;

    NameSpacedKey identifier = this.createIdentifier(resourceLocation);
    byte[] data = this.copyBufferData(buffer);

    return this.eventBus.fireEvent(
        this.customPayloadFactory.create(address, direction, identifier, data), phase);
  }

  private NameSpacedKey createIdentifier(ResourceLocation resourceLocation) {
    return NameSpacedKey.of(resourceLocation.getNamespace(), resourceLocation.getPath());
  }

  private byte[] copyBufferData(PacketBuffer buffer) {
    buffer.markReaderIndex();
    buffer.readerIndex(0);

    byte[] data = new byte[buffer.readableBytes()];

    buffer.readBytes(data);
    buffer.resetReaderIndex();

    return data;
  }
}
