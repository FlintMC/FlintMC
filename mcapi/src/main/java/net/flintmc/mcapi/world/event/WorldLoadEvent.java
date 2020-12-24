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
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe.Phase;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;

/**
 * This event will be fired when a world is being loaded (e.g. when clicking on "Play selected
 * world" in the Singleplayer screen). It will only be fired in the {@link Phase#POST} phase.
 *
 * @see Subscribe
 */
public interface WorldLoadEvent extends Event {

  /**
   * Retrieves the name of the world that is being loaded.
   *
   * @return The non-null name of the world
   */
  String getWorldName();

  /**
   * Retrieves the current state in the loading phase.
   *
   * @return The non-null state in this event
   */
  State getState();

  /**
   * The percentage of the loading phase.
   *
   * <p>If the {@link #getState() state} is {@link State#START}, this will be 0.
   *
   * <p>If the {@link #getState() state} is {@link State#END}, this will be 100.
   *
   * @return The percentage from 0 to 100
   */
  float getProcessPercentage();

  /**
   * States in the loading phase in a {@link WorldLoadEvent}.
   */
  enum State {
    /**
     * The loading of the world has just started.
     */
    START,
    /**
     * Another chunk has been loaded and the percentage has been updated.
     */
    UPDATE,
    /**
     * All required chunks have been loaded the loading is done.
     */
    END
  }

  /**
   * Factory for the {@link WorldLoadEvent}.
   */
  @AssistedFactory(WorldLoadEvent.class)
  interface Factory {

    /**
     * Creates a new {@link WorldLoadEvent}.
     *
     * @param worldName         The non-null name of the world that is being loaded
     * @param state             The non-null state in loading the world
     * @param processPercentage The percentage of the completion in loading the world from 0 to 100
     * @return The new non-null {@link WorldLoadEvent}
     */
    WorldLoadEvent create(
        @Assisted String worldName, @Assisted State state, @Assisted float processPercentage);
  }
}
