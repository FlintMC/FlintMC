package net.flintmc.mcapi.server.event;

import net.flintmc.framework.eventbus.event.Cancellable;
import net.flintmc.framework.eventbus.event.Event;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe.Phase;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.event.DirectionalEvent;
import net.flintmc.mcapi.server.status.ServerStatus;

/**
 * This event will be fired whenever a Packet is sent/received to/from a server or in singleplayer
 * to the integrated server. It will be fired in the {@link Phase#PRE} phase for the directions
 * {@link Direction#SEND} and {@link Direction#RECEIVE} and in the {@link Phase#PRE} phase for the
 * direction {@link Direction#RECEIVE}.
 *
 * <p>This event should only be used if really necessary as it is completely version-specific, for
 * some things there are there separate events that are not version-specific.
 *
 * @see Subscribe
 */
public interface PacketEvent extends Event, DirectionalEvent, Cancellable {

  /**
   * Retrieves the packet that has been sent/received or is about to be sent in this event. This is
   * the version-specific object from Minecraft, not a Flint implementation.
   *
   * @return The non-null packet in this event
   */
  Object getPacket();

  /**
   * Retrieves the phase in which the given packet was sent.
   *
   * @return The non-null phase
   */
  ProtocolPhase getPhase();

  /**
   * Phases when packets are sent. Every type of packet has exactly one phase when it can be sent.
   */
  enum ProtocolPhase {
    /**
     * The handshake phase in the protocol when the server doesn't know whether the client wants to
     * send a {@link #STATUS} or {@link #LOGIN} request.
     */
    HANDSHAKE,

    /**
     * The login phase in the protocol when the client is about to be authorized with mojang, or for
     * offline-mode servers directly join after the client knowing it.
     */
    LOGIN,

    /** The mainly used phase in the protocol with everything after the login was success. */
    PLAY,

    /** The phase when the client requests a {@link ServerStatus}. */
    STATUS
  }

  /** Factory for the {@link PacketEvent}. */
  @AssistedFactory(PacketEvent.class)
  interface Factory {

    /**
     * Creates a new {@link PacketEvent}.
     *
     * @param packet The non-null packet of the new event
     * @param phase The non-null phase in which the given packet has been or is about to be
     *     sent/received
     * @param direction The non-null direction in which the packet is being transported
     * @return The new non-null {@link PacketEvent}
     */
    PacketEvent create(
        @Assisted("packet") Object packet,
        @Assisted("phase") ProtocolPhase phase,
        @Assisted("direction") Direction direction);
  }
}
