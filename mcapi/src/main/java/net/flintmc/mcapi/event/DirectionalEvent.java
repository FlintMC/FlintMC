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

package net.flintmc.mcapi.event;

import net.flintmc.framework.eventbus.event.subscribe.Subscribe;

/**
 * The base event for communication between two applications (e.g. a client and server) with a
 * specifiable direction (sending, receiving).
 *
 * @see Subscribe
 */
public interface DirectionalEvent {

  /**
   * Retrieves the direction in which this event has happened.
   *
   * @return The non-null direction of the action of this event
   */
  Direction getDirection();

  /**
   * An enumeration with all possible directions for the {@link DirectionalEvent}.
   */
  enum Direction {

    /**
     * The direction for receiving something from somewhere else (e.g. a server).
     */
    RECEIVE,
    /**
     * The direction for sending something to somewhere else (e.g. a server).
     */
    SEND
  }
}
