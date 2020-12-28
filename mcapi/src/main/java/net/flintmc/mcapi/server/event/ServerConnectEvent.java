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

import net.flintmc.framework.eventbus.event.Event;
import net.flintmc.framework.eventbus.event.subscribe.Subscribable;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe.Phase;
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
@Subscribable({Phase.PRE, Phase.POST})
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
