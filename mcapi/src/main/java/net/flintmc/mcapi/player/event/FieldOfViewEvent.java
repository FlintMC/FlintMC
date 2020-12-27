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
import net.flintmc.framework.eventbus.event.subscribe.Subscribe.Phase;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;

/**
 * Fired when the player's field of view is rendered.
 */
@Subscribable({Phase.PRE, Phase.POST})
public interface FieldOfViewEvent extends Event {

  /**
   * Retrieves the field of view of the player entity.
   *
   * @return The player's field of view.
   */
  float getFov();

  /**
   * Changes the field of view of the player entity.
   *
   * @param fov The new field of view.
   */
  void setFov(float fov);

  /**
   * A factory class for the {@link FieldOfViewEvent}.
   */
  @AssistedFactory(FieldOfViewEvent.class)
  interface Factory {

    /**
     * Creates a new field of view event.
     *
     * @param fov The field of view.
     * @return The created field of view event.
     */
    FieldOfViewEvent create(@Assisted("fov") float fov);
  }
}
