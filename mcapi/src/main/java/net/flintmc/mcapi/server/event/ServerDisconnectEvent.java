package net.flintmc.mcapi.server.event;

import javax.annotation.Nullable;
import net.flintmc.framework.eventbus.event.Event;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.event.DirectionalEvent;
import net.flintmc.mcapi.server.ServerAddress;
import net.flintmc.mcapi.server.ServerController;

/**
 * This event will be fired whenever the user disconnects from a server. It doesn't matter whether
 * the disconnection has been called through the {@link ServerController} or by the user. Note that
 * this event will NOT be fired, when the server kicks the player. It will only be fired in the POST
 * phase.
 *
 * <p>In this case, the directions in the {@link DirectionalEvent} mean whether the disconnect has
 * been initiated by the server ({@link Direction#RECEIVE}), or initiated by the client ({@link
 * Direction#SEND}. If the direction is {@link Direction#SEND}, the {@link ServerKickEvent} will
 * also be fired at the same time.
 *
 * @see Subscribe
 */
public interface ServerDisconnectEvent extends Event, ServerAddressEvent, DirectionalEvent {

  /**
   * Factory for the {@link ServerDisconnectEvent}.
   */
  @AssistedFactory(ServerDisconnectEvent.class)
  interface Factory {

    /**
     * Creates a new {@link ServerDisconnectEvent} with the given address.
     *
     * @param address The address for the new event, may be {@code null} if the event happened in
     *                singleplayer
     * @return The new event
     */
    ServerDisconnectEvent create(@Assisted("address") @Nullable ServerAddress address);
  }
}
