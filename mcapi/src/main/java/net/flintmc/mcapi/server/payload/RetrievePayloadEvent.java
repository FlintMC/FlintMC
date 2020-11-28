package net.flintmc.mcapi.server.payload;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.resources.ResourceLocation;
import net.flintmc.mcapi.server.buffer.PacketBuffer;

/** Fried when a custom payload message is received from the server. */
public interface RetrievePayloadEvent extends PayloadEvent {

  /** A factory class for {@link RetrievePayloadEvent}. */
  @AssistedFactory(RetrievePayloadEvent.class)
  interface Factory {

    /**
     * Creates a new {@link RetrievePayloadEvent} with the given parameters.
     *
     * @param identifier The channel identifier where the message should be received.
     * @param buffer The buffer that contains the payload.
     * @return A created retrieve payload event.
     */
    RetrievePayloadEvent create(
        @Assisted ResourceLocation identifier, @Assisted PacketBuffer buffer);
  }
}
