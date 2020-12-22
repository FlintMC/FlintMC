package net.flintmc.mcapi.server.event;

import net.flintmc.framework.eventbus.event.Event;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.server.ServerAddress;
import net.flintmc.mcapi.server.ServerController;

/**
 * This event will be fired whenever the user connects to a server. It doesn't matter whether the
 * connection has been called through the {@link ServerController} or by the user. It will be fired
 * in both PRE and POST phases ignoring any errors that occur while connecting.
 *
 * <p>In this event, the {@link ServerAddressEvent#getAddress() address} will never be {@code
 * null}.
 *
 * @see Subscribe
 */
public interface ServerConnectEvent extends Event, ServerAddressEvent {

  /**
   * Factory for the {@link ServerConnectEvent}.
   */
  @AssistedFactory(ServerConnectEvent.class)
  interface Factory {

    /**
     * Creates a new {@link ServerConnectEvent} with the given address.
     *
     * @param address The non-null address for the new event
     * @return The new event
     */
    ServerConnectEvent create(@Assisted("address") ServerAddress address);
  }
}
