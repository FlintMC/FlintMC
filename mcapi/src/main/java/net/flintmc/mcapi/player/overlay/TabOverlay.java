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

package net.flintmc.mcapi.player.overlay;

import net.flintmc.mcapi.chat.component.ChatComponent;

/**
 * Represents the tab overlay
 */
public interface TabOverlay {

  /**
   * Retrieves the header of this player.
   *
   * @return The header of this player
   */
  ChatComponent getHeader();

  /**
   * Updates the header of this player.
   *
   * @param header The new header content
   */
  void updateHeader(ChatComponent header);

  /**
   * Retrieves the footer of this player.
   *
   * @return The footer of this player.
   */
  ChatComponent getFooter();

  /**
   * Updates the footer of this player.
   *
   * @param footer The new footer content
   */
  void updateFooter(ChatComponent footer);
}
