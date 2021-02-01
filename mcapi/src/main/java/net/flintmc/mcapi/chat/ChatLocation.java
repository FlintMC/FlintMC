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

package net.flintmc.mcapi.chat;

import net.flintmc.mcapi.settings.game.settings.ChatVisibility;

/**
 * A location in the chat where components can be displayed by the {@link ChatController}.
 */
public enum ChatLocation {

  /**
   * The default chat.
   */
  CHAT,

  /**
   * The action bar above the hotbar. Every message sent into the action bar will only be displayed
   * for 20 ticks (1 second) and needs to be resent after that if should should stay there.
   */
  ACTION_BAR,

  /**
   * The default chat but only if the user has enabled {@link ChatVisibility#SYSTEM}.
   */
  SYSTEM

}
