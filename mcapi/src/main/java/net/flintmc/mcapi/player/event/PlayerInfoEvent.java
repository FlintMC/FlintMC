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

package net.flintmc.mcapi.player.event;

import net.flintmc.framework.eventbus.event.Event;
import net.flintmc.framework.eventbus.event.subscribe.Subscribable;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe.Phase;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.player.network.NetworkPlayerInfo;

/**
 * This event will be fired when a {@link NetworkPlayerInfo} is added/removed/updated on a
 * Multiplayer server, it will never be fired in Singleplayer and it will only be fired in the
 * {@link Subscribe.Phase#POST} phase
 *
 * @see Subscribe
 */
@Subscribable(Phase.POST)
public interface PlayerInfoEvent extends Event {

  /**
   * Retrieves the type why this event has been fired.
   *
   * @return The non-null type of this event
   */
  Type getType();

  /**
   * Retrieves the player info that has been updated.
   *
   * @return The non-null player info that has been updated
   */
  NetworkPlayerInfo getPlayerInfo();

  /**
   * Types as reasons for a {@link PlayerInfoEvent} being fired.
   */
  enum Type {
    ADD(false),
    REMOVE(false),
    UPDATE_PING(true),
    UPDATE_DISPLAY_NAME(true),
    UPDATE_GAME_MODE(true);

    private final boolean update;

    Type(boolean update) {
      this.update = update;
    }

    /**
     * Retrieves whether this type will be fired as an update of an info or when an info is
     * added/removed.
     *
     * @return {@code true} if it will be fired as an update, {@code false} if it will be fired as
     * an add/remove
     */
    public boolean isUpdate() {
      return this.update;
    }
  }

  /**
   * Factory for {@link PlayerInfoEvent}.
   */
  @AssistedFactory(PlayerInfoEvent.class)
  interface Factory {

    /**
     * Creates a new {@link PlayerInfoEvent}.
     *
     * @param type       The non-null type of the event
     * @param playerInfo The non-null player info that has been updated
     * @return The new non-null {@link PlayerInfoEvent}
     */
    PlayerInfoEvent create(@Assisted Type type, @Assisted NetworkPlayerInfo playerInfo);
  }
}
