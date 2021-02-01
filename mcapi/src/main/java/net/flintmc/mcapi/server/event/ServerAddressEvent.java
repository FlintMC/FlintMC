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
import net.flintmc.mcapi.server.ServerAddress;

/**
 * The base event for everything happening with a server or in the singleplayer with the integrated
 * server.
 *
 * @see Subscribe
 */
@Subscribable({Phase.PRE, Phase.POST})
public interface ServerAddressEvent extends Event {

  /**
   * Retrieves the address of the server where this event has happened.
   *
   * @return The address of the server or {@code null}, if the event happend in singleplayer with the
   * integrated server
   */
  ServerAddress getAddress();

  /**
   * Factory for the {@link ServerAddressEvent}.
   */
  @AssistedFactory(ServerAddressEvent.class)
  interface Factory {

    /**
     * Creates a new {@link ServerAddressEvent} with the given address.
     *
     * @param address The address for the new event, may be {@code null}, if the event happened in
     *                singleplayer
     * @return The new event
     */
    ServerAddressEvent create(@Assisted("address") @Nullable ServerAddress address);
  }
}
