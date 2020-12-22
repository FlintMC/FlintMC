package net.flintmc.mcapi.server.event;

import javax.annotation.Nullable;
import net.flintmc.framework.eventbus.event.Event;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.server.ServerAddress;

/**
 * This event will be fired when the server has sent the login success packet. It will be fired in
 * both PRE and POST phases. In this case PRE/POST means before/after the client changes the message
 * in the GUI and switches the Protocol for incoming/outgoing packets to PLAY.
 *
 * @see Subscribe
 */
public interface ServerLoginSuccessEvent extends Event, ServerAddressEvent {

  @AssistedFactory(ServerLoginSuccessEvent.class)
  interface Factory {

    /**
     * Creates a new {@link ServerLoginSuccessEvent} with the given address.
     *
     * @param address The address for the new event, may be {@code null} if the event happened in
     *                singleplayer
     * @return The new event
     */
    ServerLoginSuccessEvent create(@Assisted("address") @Nullable ServerAddress address);
  }
}
