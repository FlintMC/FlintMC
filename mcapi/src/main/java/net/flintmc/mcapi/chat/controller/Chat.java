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

package net.flintmc.mcapi.chat.controller;

import java.util.List;

/**
 * This class defines a chat window on the screen which contains messages. A {@link ChatController}
 * can contain multiple {@link Chat}s.
 */
public interface Chat {

  /**
   * Retrieves the id of this chat. The id is unique for every chat in a controller.
   *
   * @return The id of this chat
   */
  int getId();

  /**
   * Retrieves a list of all messages that are displayed in this chat. The size of this list will
   * never be greater than {@link #getMaxMessages()}.
   *
   * @return A non-null and unmodifiable list with all messages
   */
  List<ChatMessage> getReceivedMessages();

  /**
   * Retrieves the max messages that can be stored in this chat.
   *
   * @return An int which is always larger than 0
   */
  int getMaxMessages();

  /**
   * Displays the given message in this chat and removes the last messages out of the received
   * messages in this chat if there are more messages than {@link #getMaxMessages()}.
   *
   * @param message The new non-null message to be displayed
   * @throws IllegalArgumentException If the {@link ChatMessage#getTargetChat()} is not the same as
   *                                  this chat
   */
  void displayChatMessage(ChatMessage message);
}
