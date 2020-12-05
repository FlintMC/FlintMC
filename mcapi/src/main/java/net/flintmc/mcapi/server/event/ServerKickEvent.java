package net.flintmc.mcapi.server.event;

import net.flintmc.framework.eventbus.event.Event;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.server.ServerAddress;

import javax.annotation.Nullable;

/**
 * This event will be fired whenever the user gets kicked from a server. Note that this event will
 * NOT be fired if the user disconnects from the server by himself. It will only be fired in the
 * POST phase.
 *
 * @see Subscribe
 */
public interface ServerKickEvent extends Event, ServerAddressEvent, ServerDisconnectEvent {

  /**
   * Retrieves the reason for the kick which has been sent by the server.
   *
   * @return The non-null reason for the kick
   */
  ChatComponent getReason();

  /** Factory for the {@link ServerKickEvent}. */
  @AssistedFactory(ServerKickEvent.class)
  interface Factory {

    /**
     * Creates a new {@link ServerKickEvent} with the given address and reason.
     *
     * @param address The address for the new event, may be {@code null} if the event happened in
     *     singleplayer
     * @param reason The non-null reason for the kick
     * @return The new event
     */
    ServerKickEvent create(
        @Assisted("address") @Nullable ServerAddress address,
        @Assisted("reason") ChatComponent reason);
  }
}
