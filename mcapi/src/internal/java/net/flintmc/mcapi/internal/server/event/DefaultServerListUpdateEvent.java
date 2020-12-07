package net.flintmc.mcapi.internal.server.event;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.server.ServerData;
import net.flintmc.mcapi.server.event.ServerListUpdateEvent;

/** {@inheritDoc} */
@Implement(ServerListUpdateEvent.class)
public class DefaultServerListUpdateEvent implements ServerListUpdateEvent {

  private final int index;
  private final ServerData serverData;
  private final Type type;
  private boolean cancelled;

  @AssistedInject
  public DefaultServerListUpdateEvent(
      @Assisted int index, @Assisted ServerData serverData, @Assisted Type type) {
    this.index = index;
    this.serverData = serverData;
    this.type = type;
  }

  /** {@inheritDoc} */
  @Override
  public int getIndex() {
    return this.index;
  }

  /** {@inheritDoc} */
  @Override
  public ServerData getServerData() {
    return this.serverData;
  }

  /** {@inheritDoc} */
  @Override
  public Type getType() {
    return this.type;
  }

  /** {@inheritDoc} */
  @Override
  public boolean isCancelled() {
    return this.cancelled;
  }

  /** {@inheritDoc} */
  @Override
  public void setCancelled(boolean cancelled) {
    this.cancelled = cancelled;
  }
}
