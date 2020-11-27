package net.flintmc.mcapi.internal.server.event;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.framework.stereotype.NameSpacedKey;
import net.flintmc.mcapi.event.DirectionalEvent;
import net.flintmc.mcapi.server.ServerAddress;
import net.flintmc.mcapi.server.event.ServerCustomPayloadEvent;

import javax.annotation.Nullable;

@Implement(ServerCustomPayloadEvent.class)
public class DefaultServerCustomPayloadEvent extends DefaultServerAddressEvent
    implements ServerCustomPayloadEvent {

  private final DirectionalEvent.Direction direction;
  private final NameSpacedKey identifier;
  private final byte[] data;
  private boolean cancelled;

  @AssistedInject
  public DefaultServerCustomPayloadEvent(
      @Assisted("address") @Nullable ServerAddress address,
      @Assisted("direction") DirectionalEvent.Direction direction,
      @Assisted("identifier") NameSpacedKey identifier,
      @Assisted("data") byte[] data) {
    super(address);
    this.direction = direction;
    this.identifier = identifier;
    this.data = data;
  }

  @Override
  public DirectionalEvent.Direction getDirection() {
    return this.direction;
  }

  @Override
  public NameSpacedKey getIdentifier() {
    return this.identifier;
  }

  @Override
  public byte[] getData() {
    return this.data;
  }

  @Override
  public boolean isCancelled() {
    return this.cancelled;
  }

  @Override
  public void setCancelled(boolean cancelled) {
    this.cancelled = cancelled;
  }
}
