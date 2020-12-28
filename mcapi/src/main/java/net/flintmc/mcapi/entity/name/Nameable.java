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

package net.flintmc.mcapi.entity.name;

import net.flintmc.mcapi.chat.component.ChatComponent;

/**
 * Serves as name interface for objects.
 */
public interface Nameable {

  /**
   * Retrieves the name of the object.
   *
   * @return The object name.
   */
  ChatComponent getName();

  /**
   * Whether the object has a custom name.
   *
   * @return {@code true} if the object has a custom name, otherwise {@code false}.
   */
  default boolean hasCustomName() {
    return this.getCustomName() != null;
  }

  /**
   * Retrieves the display name of the object.
   *
   * @return The object display name.
   */
  default ChatComponent getDisplayName() {
    return this.getName();
  }

  /**
   * Retrieves the custom name of the object.
   *
   * @return The object custom name.
   */
  default ChatComponent getCustomName() {
    return null;
  }
}
