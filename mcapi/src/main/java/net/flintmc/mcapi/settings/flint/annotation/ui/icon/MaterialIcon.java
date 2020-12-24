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

package net.flintmc.mcapi.settings.flint.annotation.ui.icon;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * An icon which displays an item stack with a specified type, amount an enchantment.
 *
 * @see Icon
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface MaterialIcon {

  /**
   * Retrieves the type of the item to be displayed, for example 'minecraft:stone'.
   *
   * @return The type of the item
   */
  String value();

  /**
   * Retrieves the amount of items on the given stack to be displayed.
   *
   * @return The amount of items
   */
  int amount() default 1;

  /**
   * Retrieves whether the displayed item should be enchanted or not.
   *
   * @return {@code true} if the displayed item should be enchanted, {@code false} otherwise
   */
  boolean enchanted() default false;
}
