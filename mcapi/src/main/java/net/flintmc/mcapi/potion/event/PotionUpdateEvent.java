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

package net.flintmc.mcapi.potion.event;

import net.flintmc.framework.eventbus.event.Event;
import net.flintmc.framework.eventbus.event.subscribe.Subscribable;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe.Phase;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.entity.LivingEntity;

/**
 * Fired when the potions are to be updated.
 *
 * <p>The event is fired in the {@link Subscribe.Phase#PRE} and {@link Subscribe.Phase#POST} phase.
 */
@Subscribable({Phase.PRE, Phase.POST})
public interface PotionUpdateEvent extends Event {

  /**
   * Retrieves the living entity where the potion are updated.
   *
   * @return The living entity where the potion are updated.
   */
  LivingEntity getLivingEntity();

  /**
   * Factory for {@link PotionUpdateEvent}.
   */
  @AssistedFactory(PotionUpdateEvent.class)
  interface Factory {

    /**
     * Creates a new {@link PotionUpdateEvent} with the given {@code livingEntity}.
     *
     * @param livingEntity The living entity where the potions are updated.
     * @return A created potion update event.
     */
    PotionUpdateEvent create(@Assisted("livingEntity") LivingEntity livingEntity);
  }
}
