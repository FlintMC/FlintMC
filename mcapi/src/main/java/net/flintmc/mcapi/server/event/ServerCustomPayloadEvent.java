package net.flintmc.mcapi.server.event;

import net.flintmc.framework.eventbus.event.Cancellable;
import net.flintmc.framework.eventbus.event.Event;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.framework.stereotype.NameSpacedKey;
import net.flintmc.mcapi.event.DirectionalEvent;
import net.flintmc.mcapi.server.ServerAddress;

import javax.annotation.Nullable;

/**
 * This event will be fired whenever a custom payload is being sent to (or received from) the
 * server. It will be fired in both PRE and POST phases, but cancellation only has an effect in the
 * PRE phase.
 *
 * @see Subscribe
 */
public interface ServerCustomPayloadEvent
    extends Event, ServerAddressEvent, DirectionalEvent, Cancellable {

  /**
   * Retrieves the identifier of this custom payload.
   *
   * @return The non-null identifier
   */
  NameSpacedKey getIdentifier();

  /**
   * Retrieves the contents of this custom payload.
   *
   * @return The non-null contents
   */
  byte[] getData();

  /** Factory for the {@link ServerCustomPayloadEvent}. */
  @AssistedFactory(ServerCustomPayloadEvent.class)
  interface Factory {

    /**
     * Creates a new {@link ServerCustomPayloadEvent} with the given address.
     *
     * @param address The address for the new event, may be {@code null} if the event happened in
     *     singleplayer
     * @return The new event
     */
    ServerCustomPayloadEvent create(
        @Assisted("address") @Nullable ServerAddress address,
        @Assisted("direction") Direction direction,
        @Assisted("identifier") NameSpacedKey identifier,
        @Assisted("data") byte[] data);
  }
}
