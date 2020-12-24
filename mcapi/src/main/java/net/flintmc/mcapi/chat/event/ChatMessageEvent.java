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

package net.flintmc.mcapi.chat.event;

import net.flintmc.framework.eventbus.event.Event;

/**
 * The base event for sending/receiving messages in the chat.
 */
public interface ChatMessageEvent extends Event {

  /**
   * Retrieves the type of this chat message event
   *
   * @return The non-null type of this event
   */
  Type getType();

  /**
   * Types for chat message events.
   */
  enum Type {

    /**
     * The type when sending a message from the client to the server.
     */
    SEND,

    /**
     * The type when receiving a message from the server in the client.
     */
    RECEIVE
  }
}
