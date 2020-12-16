package net.flintmc.mcapi.internal.server.payload;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.resources.ResourceLocation;
import net.flintmc.mcapi.server.buffer.PacketBuffer;
import net.flintmc.mcapi.server.payload.RetrievePayloadEvent;

@Implement(RetrievePayloadEvent.class)
public class DefaultRetrievePayloadEvent extends DefaultPayloadEvent
    implements RetrievePayloadEvent {

  @AssistedInject
  public DefaultRetrievePayloadEvent(
      @Assisted ResourceLocation identifier, @Assisted PacketBuffer buffer) {
    super(identifier, buffer);
  }
}
