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

package net.flintmc.mcapi.items.meta.enchantment;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.chat.component.ChatComponent;

/**
 * An Enchantment of an Item.
 */
public interface Enchantment {

  /**
   * Retrieves the type of this enchantment which has been set when creating this enchantment.
   *
   * @return The non-null type of this enchantment
   */
  EnchantmentType getType();

  /**
   * Retrieves the level of this enchantment which is always at least 1.
   *
   * @return The level of this enchantment
   */
  int getLevel();

  /**
   * Retrieves the display name of this enchantment. It will be constructed by chaining the display
   * name of the type, a space and the level of this enchantment.
   *
   * @return The non-null display name of the enchantment
   */
  ChatComponent getDisplayName();

  /**
   * Factory for {@link Enchantment}.
   */
  @AssistedFactory(Enchantment.class)
  interface Factory {

    /**
     * Creates a new enchantment with the given type and level.
     *
     * @param type  The non-null type of the new enchantment
     * @param level The level of the new enchantment, has to be at least 1
     * @return The new non-null enchantment
     */
    Enchantment createEnchantment(
        @Assisted("type") EnchantmentType type, @Assisted("level") int level);
  }
}
