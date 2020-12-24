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
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;

/**
 * This event will be fired when a chunk is unloaded in either a Singleplayer or Multiplayer world.
 * It will not be fired for each chunk when leaving a world or server. It will be fired in both the
 * {@link Subscribe.Phase#PRE} and {@link Subscribe.Phase#POST} phases.
 *
 * @see Subscribe
 */
public interface ChunkUnloadEvent extends Event {

  /**
   * Retrieves the X coordinate of the chunk that has been unloaded.
   *
   * @return The X coordinate of the chunk
   */
  int getX();

  /**
   * Retrieves the Y coordinate of the chunk that has been unloaded.
   *
   * @return The Y coordinate of the chunk
   */
  int getZ();

  /**
   * Factory for the {@link ChunkUnloadEvent}.
   */
  @AssistedFactory(ChunkUnloadEvent.class)
  interface Factory {

    /**
     * Creates a new {@link ChunkUnloadEvent} with the given chunk coordinates.
     *
     * @param x The X coordinate of the chunk
     * @param z The Z coordinate of the chunk
     * @return The new non-null {@link ChunkUnloadEvent}
     */
    ChunkUnloadEvent create(@Assisted("x") int x, @Assisted("z") int z);
  }
}
