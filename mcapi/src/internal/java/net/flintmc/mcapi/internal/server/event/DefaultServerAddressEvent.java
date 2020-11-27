package net.flintmc.mcapi.internal.server.event;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.server.ServerAddress;
import net.flintmc.mcapi.server.event.ServerAddressEvent;

import javax.annotation.Nullable;

@Implement(ServerAddressEvent.class)
public class DefaultServerAddressEvent implements ServerAddressEvent {

  private final ServerAddress address;

  @AssistedInject
  public DefaultServerAddressEvent(@Assisted("address") @Nullable ServerAddress address) {
    this.address = address;
  }

  @Override
  public ServerAddress getAddress() {
    return this.address;
  }
}
