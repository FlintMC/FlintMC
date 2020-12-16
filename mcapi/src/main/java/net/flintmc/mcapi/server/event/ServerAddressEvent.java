package net.flintmc.mcapi.server.event;

import net.flintmc.framework.eventbus.event.Event;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.server.ServerAddress;

import javax.annotation.Nullable;

/**
 * The base event for everything happening with a server or in the singleplayer with the integrated
 * server.
 *
 * @see Subscribe
 */
public interface ServerAddressEvent extends Event {

  /**
   * Retrieves the address of the server where this event has happened.
   *
   * @return The address of the server or {@code null} if the event happend in singleplayer with the
   *     integrated server
   */
  ServerAddress getAddress();

  /** Factory for the {@link ServerAddressEvent}. */
  @AssistedFactory(ServerAddressEvent.class)
  interface Factory {

    /**
     * Creates a new {@link ServerAddressEvent} with the given address.
     *
     * @param address The address for the new event, may be {@code null} if the event happened in
     *     singleplayer
     * @return The new event
     */
    ServerAddressEvent create(@Assisted("address") @Nullable ServerAddress address);
  }
}
