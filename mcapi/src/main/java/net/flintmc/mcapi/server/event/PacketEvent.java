/*
 * FlintMC
 * Copyright (C) 2020-2021 LabyMedia GmbH and contributors
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package net.flintmc.mcapi.server.event;

import net.flintmc.framework.eventbus.event.Cancellable;
import net.flintmc.framework.eventbus.event.Event;
import net.flintmc.framework.eventbus.event.subscribe.Subscribable;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe.Phase;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.event.DirectionalEvent;
import net.flintmc.mcapi.server.status.ServerStatus;

/**
 * This event will be fired whenever a Packet is sent/received to/from a server or in singleplayer
 * to the integrated server. It will be fired in the {@link Phase#PRE} and {@link Phase#POST}
 * phases, but cancellation will only have an effect in the {@link Phase#PRE} phase.
 *
 * <p>This event should only be used if really necessary as it is completely version-specific, for
 * some things there are there separate events that are not version-specific.
 *
 * @see Subscribe
 */
@Subscribable({Phase.PRE, Phase.POST})
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

    /**
     * The mainly used phase in the protocol with everything after the login was success.
     */
    PLAY,

    /**
     * The phase when the client requests a {@link ServerStatus}.
     */
    STATUS
  }

  /**
   * Factory for the {@link PacketEvent}.
   */
  @AssistedFactory(PacketEvent.class)
  interface Factory {

    /**
     * Creates a new {@link PacketEvent}.
     *
     * @param packet    The non-null packet of the new event
     * @param phase     The non-null phase in which the given packet has been or is about to be
     *                  sent/received
     * @param direction The non-null direction in which the packet is being transported
     * @return The new non-null {@link PacketEvent}
     */
    PacketEvent create(
        @Assisted("packet") Object packet,
        @Assisted("phase") ProtocolPhase phase,
        @Assisted("direction") Direction direction);
  }
}
