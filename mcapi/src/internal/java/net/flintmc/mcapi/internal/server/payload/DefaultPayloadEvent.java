package net.flintmc.mcapi.internal.server.payload;

import net.flintmc.mcapi.resources.ResourceLocation;
import net.flintmc.mcapi.server.buffer.PacketBuffer;
import net.flintmc.mcapi.server.payload.PayloadEvent;

public class DefaultPayloadEvent implements PayloadEvent {

  private final ResourceLocation identifier;
  private final PacketBuffer buffer;
  private boolean cancelled;

  public DefaultPayloadEvent(ResourceLocation identifier, PacketBuffer buffer) {
    this.identifier = identifier;
    this.buffer = buffer;
    this.cancelled = false;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ResourceLocation getIdentifier() {
    return this.identifier;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public PacketBuffer getBuffer() {
    return this.buffer;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isCancelled() {
    return this.cancelled;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setCancelled(boolean cancelled) {
    this.cancelled = cancelled;
  }
}
