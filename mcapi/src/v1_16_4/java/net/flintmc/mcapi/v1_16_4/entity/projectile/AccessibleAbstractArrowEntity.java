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

package net.flintmc.mcapi.v1_16_4.entity.projectile;

import net.flintmc.mcapi.items.ItemStack;
import net.flintmc.transform.shadow.FieldGetter;
import net.flintmc.transform.shadow.FieldSetter;
import net.flintmc.transform.shadow.MethodProxy;
import net.flintmc.transform.shadow.Shadow;
import net.minecraft.entity.projectile.AbstractArrowEntity;

/** A shadow interface for the abstract arrow entity. */
@Shadow("net.minecraft.entity.projectile.AbstractArrowEntity")
public interface AccessibleAbstractArrowEntity {

  /**
   * Retrieves the pickup status of the arrow entity.
   *
   * @return The arrow entity pickup status.
   */
  @FieldGetter("pickupStatus")
  Object getPickupStatus();

  /**
   * Changes the pickup status of the arrow entity.
   *
   * @param status The new pickup status.
   */
  @FieldSetter("pickupStatus")
  void setPickupStatus(AbstractArrowEntity.PickupStatus status);

  /**
   * Retrieves the knockback strength of the arrow entity.
   *
   * @return The knockback strength.
   */
  @FieldGetter("knockbackStrength")
  int getKnockbackStrength();

  /**
   * Retrieves the arrow as an {@link ItemStack}.
   *
   * @return The arrow as an item stack.
   */
  @MethodProxy
  Object getArrowStack();

  /**
   * Retrieves the water drag of the arrow entity.
   *
   * @return The water drag of the arrow entity.
   */
  @MethodProxy
  float getWaterDrag();
}
