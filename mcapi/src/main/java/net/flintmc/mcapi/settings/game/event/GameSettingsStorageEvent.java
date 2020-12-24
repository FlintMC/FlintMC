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

package net.flintmc.mcapi.settings.game.event;

import net.flintmc.framework.eventbus.event.Event;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;

/**
 * Fired when Minecraft loads or saves the its game settings. It will be fired on both the {@link
 * Subscribe.Phase#PRE} and {@link Subscribe.Phase#POST} phases.
 *
 * @see Subscribe
 */
public interface GameSettingsStorageEvent extends Event {

  /**
   * Retrieves the state of this event.
   *
   * @return The current state.
   */
  State getState();

  /**
   * An enumeration that representing all states for the configuration.
   */
  enum State {

    /**
     * When the configuration is loaded.
     */
    LOAD,
    /**
     * When the configuration is saved.
     */
    SAVE
  }

  /**
   * A factory class for the {@link GameSettingsStorageEvent}.
   */
  @AssistedFactory(GameSettingsStorageEvent.class)
  interface Factory {

    /**
     * Creates a new {@link GameSettingsStorageEvent} with the given parameters.
     *
     * @param state The state when the event is fired.
     * @return A created configuration event.
     */
    GameSettingsStorageEvent create(@Assisted("state") State state);
  }
}
