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

import net.flintmc.framework.eventbus.event.subscribe.Subscribable;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe.Phase;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.entity.LivingEntity;
import net.flintmc.mcapi.potion.effect.StatusEffect;

/**
 * Fired when a status effect is removed from a {@link LivingEntity}.
 *
 * <p>The event is fired in the {@link Subscribe.Phase#PRE} phase.
 *
 * @see Subscribe.Phase#PRE
 */
@Subscribable(Phase.PRE)
public interface PotionRemoveEvent extends PotionEvent {

  /**
   * Retrieves the status effect to be removed.
   *
   * @return The status effect to be removed.
   */
  StatusEffect getStatusEffect();

  /**
   * Factory for the {@link PotionRemoveEvent}.
   */
  @AssistedFactory(PotionRemoveEvent.class)
  interface Factory {

    /**
     * Creates a new {@link PotionRemoveEvent} with the given {@code livingEntity} and the {@code
     * statusEffect}.
     *
     * @param livingEntity The living entity for which the status effect is to be removed.
     * @param statusEffect The status effect to be removed.
     * @return A created potion remove event.
     */
    PotionRemoveEvent create(
        @Assisted("livingEntity") LivingEntity livingEntity,
        @Assisted("statusEffect") StatusEffect statusEffect);
  }
}
