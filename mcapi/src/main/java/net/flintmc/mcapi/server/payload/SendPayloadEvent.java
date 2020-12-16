package net.flintmc.mcapi.server.payload;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.resources.ResourceLocation;
import net.flintmc.mcapi.server.buffer.PacketBuffer;

/**
 * Fired when a payload message is sent to the server.
 */
public interface SendPayloadEvent extends PayloadEvent {

  /**
   * A factory class for the {@link SendPayloadEvent}.
   */
  @AssistedFactory(SendPayloadEvent.class)
  interface Factory {

    /**
     * Creates a new {@link SendPayloadEvent} with the parameters.
     *
     * @param identifier The channel identifier where the message should be sent to.
     * @param buffer     The buffer which include the payload.
     * @return A created send payload event.
     */
    SendPayloadEvent create(@Assisted ResourceLocation identifier, @Assisted PacketBuffer buffer);
  }
}
