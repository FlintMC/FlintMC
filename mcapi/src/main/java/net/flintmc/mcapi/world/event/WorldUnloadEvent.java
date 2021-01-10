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
import net.flintmc.framework.inject.assisted.AssistedFactory;

/**
 * This event will be fired when a world is being unloaded. It will only be fired in the {@link
 * Subscribe.Phase#POST} phase.
 *
 * @see Subscribe
 */
@Subscribable(Phase.POST)
public interface WorldUnloadEvent extends Event {

  /**
   * A factory for {@link WorldUnloadEvent}.
   */
  @AssistedFactory(WorldUnloadEvent.class)
  interface Factory {

    /**
     * Creates a new {@link WorldUnloadEvent}.
     *
     * @return A created world unload event.
     */
    WorldUnloadEvent create();

  }

}
