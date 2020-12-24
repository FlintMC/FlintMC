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

import net.flintmc.framework.eventbus.event.Cancellable;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.chat.component.ChatComponent;

/**
 * This event will be fired whenever a message will be displayed in the chat, it supports both PRE
 * and POST {@link Subscribe.Phase}s but the cancellation will be ignored in the POST phase. If this
 * event has been cancelled in the PRE phase, the message won't be displayed anymore in the chat.
 */
public interface ChatReceiveEvent extends ChatMessageEvent, Cancellable {

  /**
   * Retrieves the message that will be displayed in the client.
   *
   * @return The non-null component
   */
  ChatComponent getMessage();

  /**
   * Changes the message that will be displayed by the client. This has no effect in the POST
   * phase.
   *
   * @param message The new non-null message to be displayed
   */
  void setMessage(ChatComponent message);

  /**
   * Factory for the {@link ChatReceiveEvent}.
   */
  @AssistedFactory(ChatReceiveEvent.class)
  interface Factory {

    /**
     * Creates a new {@link ChatReceiveEvent} with the given message.
     *
     * @param message The non-null message for the new event
     * @return The new non-null event with the given message
     */
    ChatReceiveEvent create(@Assisted("message") ChatComponent message);
  }
}
