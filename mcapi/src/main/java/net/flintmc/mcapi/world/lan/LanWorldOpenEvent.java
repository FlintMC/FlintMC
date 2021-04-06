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

package net.flintmc.mcapi.world.lan;

import net.flintmc.framework.eventbus.event.Cancellable;
import net.flintmc.framework.eventbus.event.Event;
import net.flintmc.framework.eventbus.event.subscribe.Subscribable;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe.Phase;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;

/**
 * This event will be fired when opening a Singleplayer to the LAN via {@link
 * LanWorldService#openLanWorld(LanWorldOptions)}.
 *
 * @see Subscribe
 */
@Subscribable({Phase.PRE, Phase.POST})
public interface LanWorldOpenEvent extends Event, Cancellable {

  /**
   * Retrieves whether the world has successfully been opened to the LAN. In the {@link Phase#PRE}
   * phase, this will always be {@code false}.
   *
   * @return {@code true} if the world has successfully been opened to the LAN, {@code false}
   * otherwise
   */
  boolean wasSuccess();

  /**
   * Retrieves the port on which the server is running.
   *
   * @return The port of the server
   */
  int getPort();

  /**
   * Changes the port on which the server is running. This may only be used in the {@link Phase#PRE}
   * phase.
   *
   * @param port The new port where the server should be running
   */
  void setPort(int port);

  /**
   * Retrieves the options with which the server should be started.
   *
   * @return The non-null options to bind the server with
   */
  LanWorldOptions getOptions();

  /**
   * Changes the options with which the server should be started.
   *
   * @param options The non-null options to bind the server with
   */
  void setOptions(LanWorldOptions options);

  /**
   * Factory for the {@link LanWorldOpenEvent}.
   */
  @AssistedFactory(LanWorldOpenEvent.class)
  interface Factory {

    /**
     * Creates a new {@link LanWorldOpenEvent}.
     *
     * @param success {@code true} if the world has successfully been opened to the LAN, {@code
     *                false} otherwise
     * @param port    The new port where the server should be running
     * @param options The non-null options to bind the server with
     * @return The new non-null {@link LanWorldOpenEvent}
     */
    LanWorldOpenEvent create(
        @Assisted("success") boolean success,
        @Assisted("port") int port,
        @Assisted("options") LanWorldOptions options);
  }

}
