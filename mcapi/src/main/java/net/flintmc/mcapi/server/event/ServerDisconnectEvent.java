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
@Subscribable(Phase.POST)
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
