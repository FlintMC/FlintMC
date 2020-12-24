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

package net.flintmc.mcapi.chat.controller.filter;

import net.flintmc.mcapi.chat.controller.Chat;
import net.flintmc.mcapi.chat.controller.ChatMessage;

/**
 * The message before it is being displayed in the chat. In this message, the {@link Chat} to
 * display the message to can still be modified.
 */
public interface FilterableChatMessage extends ChatMessage {

  /**
   * Sets the chat where this message will be displayed.
   *
   * @param chat The non-null chat to display this message
   */
  void setTargetChat(Chat chat);

  /**
   * Retrieves whether this message will be displayed in the target chat or not.
   *
   * @return whether this message will be displayed or not
   */
  boolean isAllowed();

  /**
   * Sets whether this message will be displayed in the target chat or not.
   *
   * @param allowed {@code true} if the message should be displayed or {@code false} if not
   */
  void setAllowed(boolean allowed);
}
