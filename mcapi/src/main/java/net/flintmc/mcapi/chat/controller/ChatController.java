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
import java.util.UUID;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.chat.controller.filter.ChatFilter;
import net.flintmc.mcapi.chat.controller.filter.FilterableChatMessage;

/**
 * This class manages the whole chat, it allows the usage of multiple chat windows and filters for
 * the chat.
 */
public interface ChatController {

  /**
   * Sends the given message to the server just as it would be done when the player types the text
   * into the chat. Depending on the minecraft version, the length of the message is limited to
   * {@link #getChatInputLimit()}. If the given message is longer than that, everything after the
   * limit will be removed before sending it to the server. If the client would send a message
   * longer than the limit, the client would get kicked.
   *
   * @param message The non-null message to be sent to the server
   * @return Whether the message was sent successfully or cancelled by an event handler
   */
  boolean dispatchChatInput(String message);

  /**
   * Retrieves the maximum length of the message for {@link #dispatchChatInput(String)} depending on
   * the minecraft version. Before minecraft 1.11 the max length was set to 100, since 1.11 it is
   * 256.
   *
   * @return The limit of chat input depending on the minecraft version
   */
  int getChatInputLimit();

  /**
   * Displays the message into the chat. Before it is displayed, all registered filters will be
   * applied.
   *
   * @param component The non-null component to be displayed
   * @return {@code true} if the message will be displayed in the chat or {@code false} if it has
   * been blocked by a {@link ChatFilter}.
   */
  boolean displayChatMessage(ChatComponent component);

  /**
   * Displays the message into the chat. Before it is displayed, all registered filters will be
   * applied.
   *
   * @param component      The non-null component to be displayed
   * @param senderUniqueId The non-null UUID of the sender or a UUID with 0 as the most- and
   *                       leastSignificantBits if this message doesn't have a sender
   * @return {@code true} if the message will be displayed in the chat or {@code false} if it has
   * been blocked by a {@link ChatFilter}.
   */
  boolean displayChatMessage(ChatComponent component, UUID senderUniqueId);

  /**
   * The history of the chat input by the player. By default, this will only be filled when the
   * player types a message into the chat. The player can scroll through this history by using the
   * arrow keys.
   *
   * @return The modifiable non-null list containing every message the player has sent.
   */
  List<String> getInputHistory();

  /**
   * Retrieves an array of all filters for this chat controller. Modifications to the array will
   * have no effect.
   *
   * @return The non-null array of all filters in this chat controller
   */
  ChatFilter[] getFilters();

  /**
   * Posts the given message to all registered {@link ChatFilter}s.
   *
   * @param message The non-null message for the filters
   */
  void processFilters(FilterableChatMessage message);

  /**
   * Adds a new filter to this chat controller which will be called before any message is displayed
   * through this controller.
   *
   * @param filter The new non-null filter
   */
  void addFilter(ChatFilter filter);

  /**
   * Removes the given filter out of this chat controller, if there is no filter present matching
   * the given filter, nothing will happen
   *
   * @param filter The non-null filter to be removed
   */
  void removeFilter(ChatFilter filter);

  /**
   * Adds a new chat to this chat controller which can receive messages. By default, only the main
   * chat will receive messages, this can be changed by the filters.
   *
   * @param chat The new non-null chat
   * @throws IllegalStateException if a chat with the id of the given chat is already registered
   */
  void addChat(Chat chat);

  /**
   * Creates a new chat with the given id, it is only created, to add the chat, you have to use
   * {@link #addChat(Chat)}.
   *
   * @param id The id of the new chat
   * @return The new non-null chat with the given id
   * @see #addChat(Chat)
   */
  Chat createChat(int id);

  /**
   * Retrieves the main chat for this controller. The main chat usually has the id {@code 0}.
   *
   * @return The non-null main chat
   * @throws IllegalStateException If no main chat is registered, this should never happen
   */
  Chat getMainChat();

  /**
   * Retrieves a chat by the given id out of this controller.
   *
   * @param id The id of the chat
   * @return The chat or {@code null} if no chat with the given id exists
   */
  Chat getChat(int id);

  /**
   * Retrieves an array of all chats that are available in this controller. Modifications to this
   * array won't have any effect.
   *
   * @return The non-null array of all chats in this chat controller
   */
  Chat[] getChats();
}
