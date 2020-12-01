package net.flintmc.mcapi.internal.server.event;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.event.DirectionalEvent;
import net.flintmc.mcapi.event.DirectionalEvent.Direction;
import net.flintmc.mcapi.server.ServerAddress;
import net.flintmc.mcapi.server.event.ServerDisconnectEvent;

import javax.annotation.Nullable;

/** {@inheritDoc} */
@Implement(ServerDisconnectEvent.class)
public class DefaultServerDisconnectEvent extends DefaultServerAddressEvent
    implements ServerDisconnectEvent {

  @AssistedInject
  public DefaultServerDisconnectEvent(@Assisted("address") @Nullable ServerAddress address) {
    super(address);
  }

  /** {@inheritDoc} */
  @Override
  public Direction getDirection() {
    return Direction.SEND;
  }
}
