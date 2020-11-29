package net.flintmc.mcapi.server.payload;

import net.flintmc.framework.eventbus.event.Cancellable;
import net.flintmc.framework.eventbus.event.Event;
import net.flintmc.mcapi.resources.ResourceLocation;
import net.flintmc.mcapi.server.buffer.PacketBuffer;

/** Represents the base event of the payload. */
public interface PayloadEvent extends Cancellable, Event {

  /**
   * Retrieves channel identifier of this payload.
   *
   * @return The channel identifier.
   */
  ResourceLocation getIdentifier();

  /**
   * Retrieves the packet buffer of this payload.
   *
   * @return The packet buffer.
   */
  PacketBuffer getBuffer();
}
