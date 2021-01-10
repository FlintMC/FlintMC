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
import net.flintmc.framework.eventbus.event.subscribe.Subscribe.Phase;
import net.flintmc.mcapi.entity.LivingEntity;

/**
 * Base event for the {@link PotionAddEvent} and {@link PotionRemoveEvent}.
 */
@Subscribable(Phase.PRE)
public interface PotionEvent extends Event {

  /**
   * Retrieves the living entity where the status effect is added or removed.
   *
   * @return The living entity where the status effect is added or removed.
   */
  LivingEntity getLivingEntity();

  /**
   * Retrieves the type when the event is fired.
   *
   * @return The type when the event is fired.
   */
  Type getType();

  enum Type {
    ADD,
    REMOVE
  }
}
