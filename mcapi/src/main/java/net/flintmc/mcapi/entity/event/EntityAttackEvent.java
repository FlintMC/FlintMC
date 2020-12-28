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

package net.flintmc.mcapi.entity.event;

import net.flintmc.framework.eventbus.event.Event;
import net.flintmc.framework.eventbus.event.subscribe.Subscribable;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe.Phase;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.entity.Entity;

/**
 * This event will be fired when the user attacks (left click) another entity. This entity can be
 * anything, for example another player, zombie, ... It will also be fired when the attack is not
 * successful, which can for example happen when the user is attacking a player who is in creative
 * mode. The gamemode of the client doesn't matter, it will also be fired in spetator mode. It will
 * only be fired in the {@link Subscribe.Phase#PRE} phase.
 *
 * @see Subscribe
 */
@Subscribable(Phase.PRE)
public interface EntityAttackEvent extends Event {

  /**
   * Retrieves the entity that has been attacked.
   *
   * @return The non-null entity that has been attacked
   */
  Entity getAttacked();

  /**
   * Factory for the {@link EntityAttackEvent}.
   */
  @AssistedFactory(EntityAttackEvent.class)
  interface Factory {

    /**
     * Creates a new {@link EntityAttackEvent} with the given entity as the attacked one.
     *
     * @param attacked The non-null entity that has been attacked
     * @return The new non-null {@link EntityAttackEvent}
     */
    EntityAttackEvent create(@Assisted Entity attacked);
  }
}
