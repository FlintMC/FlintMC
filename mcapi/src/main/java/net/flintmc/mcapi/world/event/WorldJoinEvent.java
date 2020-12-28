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

package net.flintmc.mcapi.world.event;

import net.flintmc.framework.eventbus.event.Event;
import net.flintmc.framework.eventbus.event.subscribe.Subscribable;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe.Phase;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;

/**
 * This event will be fired when the user connects to a server in Multiplayer or when the user opens
 * world in Singleplayer.
 *
 * @see Subscribe
 */
@Subscribable({Phase.PRE, Phase.POST})
public interface WorldJoinEvent extends Event {

  /**
   * Retrieves the name of the world the player was joining. In Multiplayer ({@code type ==
   * Type.MULTIPLAYER}), this will be the IP with the port.
   *
   * @return The non-null name of the world or the IP and Port of the server
   */
  String getWorldName();

  /**
   * Retrieves the type of the world that the player is joining.
   *
   * @return The non-null type that the player is joining
   */
  Type getType();

  /**
   * An enumeration of all types of worlds that can be joined in the {@link WorldJoinEvent}.
   */
  enum Type {

    /**
     * A world in the singleplayer.
     */
    SINGLEPLAYER,

    /**
     * A server in the multiplayer.
     */
    MULTIPLAYER

  }

  /**
   * Factory for the {@link WorldJoinEvent}.
   */
  @AssistedFactory(WorldJoinEvent.class)
  interface Factory {

    /**
     * Creates a new {@link WorldJoinEvent} with the given parameters.
     *
     * @param worldName The non-null world name (Singleplayer) or Server IP and port (Multiplayer)
     * @param type      The non-null type of wold that the player is joining
     * @return The new non-null {@link WorldJoinEvent}
     */
    WorldJoinEvent create(@Assisted String worldName, @Assisted Type type);

  }

}
