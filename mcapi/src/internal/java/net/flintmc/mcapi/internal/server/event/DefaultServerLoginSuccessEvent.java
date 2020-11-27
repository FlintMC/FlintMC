package net.flintmc.mcapi.internal.server.event;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.server.ServerAddress;
import net.flintmc.mcapi.server.event.ServerLoginSuccessEvent;

import javax.annotation.Nullable;

@Implement(ServerLoginSuccessEvent.class)
public class DefaultServerLoginSuccessEvent extends DefaultServerAddressEvent
    implements ServerLoginSuccessEvent {

  @AssistedInject
  public DefaultServerLoginSuccessEvent(@Assisted("address") @Nullable ServerAddress address) {
    super(address);
  }
}
