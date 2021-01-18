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

import javax.annotation.Nullable;
import net.flintmc.framework.eventbus.event.Event;
import net.flintmc.framework.eventbus.event.subscribe.Subscribable;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe.Phase;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.server.ServerAddress;

/**
 * This event will be fired whenever the user gets kicked from a server. Note that this event will
 * NOT be fired if the user disconnects from the server by himself. It will only be fired in the PRE
 * phase.
 *
 * @see Subscribe
 */
@Subscribable(Phase.PRE)
public interface ServerKickEvent extends Event, ServerAddressEvent, ServerDisconnectEvent {

  /**
   * Retrieves the reason for the kick which has been sent by the server.
   *
   * @return The non-null reason for the kick
   */
  ChatComponent getReason();

  /**
   * Factory for the {@link ServerKickEvent}.
   */
  @AssistedFactory(ServerKickEvent.class)
  interface Factory {

    /**
     * Creates a new {@link ServerKickEvent} with the given address and reason.
     *
     * @param address The address for the new event, may be {@code null}, if the event happened in
     *                singleplayer
     * @param reason  The non-null reason for the kick
     * @return The new event
     */
    ServerKickEvent create(
        @Assisted("address") @Nullable ServerAddress address,
        @Assisted("reason") ChatComponent reason);
  }
}
