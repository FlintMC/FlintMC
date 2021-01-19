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
 * This event will be fired when the server has sent the login success packet. It will be fired in
 * both PRE and POST phases. In this case PRE/POST means before/after the client changes the message
 * in the GUI and switches the Protocol for incoming/outgoing packets to PLAY.
 *
 * @see Subscribe
 */
@Subscribable({Phase.PRE, Phase.POST})
public interface ServerLoginSuccessEvent extends Event, ServerAddressEvent {

  @AssistedFactory(ServerLoginSuccessEvent.class)
  interface Factory {

    /**
     * Creates a new {@link ServerLoginSuccessEvent} with the given address.
     *
     * @param address The address for the new event, may be {@code null}, if the event happened in
     *                singleplayer
     * @return The new event
     */
    ServerLoginSuccessEvent create(@Assisted("address") @Nullable ServerAddress address);
  }
}
