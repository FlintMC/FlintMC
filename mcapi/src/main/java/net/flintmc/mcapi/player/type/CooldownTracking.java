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

package net.flintmc.mcapi.player.type;

/**
 * Represents a cooldown tracker
 */
public interface CooldownTracking {

  /**
   * Whether the item has a cooldown.
   *
   * @param item The item to be checked
   * @return {@code true} if the item has a cooldown, otherwise {@code false}
   */
  // TODO: 05.09.2020 Replaces the Object to Item when the (Item API?) is ready
  boolean hasCooldown(Object item);

  /**
   * Retrieves the cooldown of the given item.
   *
   * @param item         The item to get the cooldown
   * @param partialTicks The period of time, in fractions of a tick, that has passed since the last
   *                     full tick.
   * @return The cooldown of this given item.
   */
  // TODO: 05.09.2020 Replaces the Object to Item when the (Item API?) is ready
  float getCooldown(Object item, float partialTicks);

  /**
   * Sets the for the cooldown tracking.
   *
   * @param item  The item for setting the cooldown.
   * @param ticks The ticks, how long the cooldown lasts.
   */
  // TODO: 05.09.2020 Replaces the Object to Item when the (Item API?) is ready
  void setCooldown(Object item, int ticks);

  /**
   * Removes the item from the cooldown tracking.
   *
   * @param item The item to be removed
   */
  // TODO: 05.09.2020 Replaces the Object to Item when the (Item API?) is ready
  void removeCooldown(Object item);
}
