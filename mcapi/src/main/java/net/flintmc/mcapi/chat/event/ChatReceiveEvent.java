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

import java.util.UUID;
import net.flintmc.framework.eventbus.event.Cancellable;
import net.flintmc.framework.eventbus.event.subscribe.Subscribable;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe.Phase;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.chat.ChatLocation;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.settings.game.configuration.ChatConfiguration;

/**
 * This event will be fired whenever a message will be displayed in the chat, it supports both PRE
 * and POST {@link Subscribe.Phase}s but the cancellation will be ignored in the POST phase. If this
 * event has been cancelled in the PRE phase, the message won't be displayed anymore in the chat.
 *
 * @see Subscribe
 */
@Subscribable({Phase.PRE, Phase.POST})
public interface ChatReceiveEvent extends ChatMessageEvent, Cancellable {

  /**
   * Retrieves the location in the chat where the message has been sent.
   *
   * @return The non-null location in the chat where the message has been sent
   */
  ChatLocation getLocation();

  /**
   * Retrieves whether this message should be visible in the chat. This depends on the {@link
   * #getLocation() location} of this message and the {@link ChatConfiguration#getChatVisibility()
   * configuration of the user}.
   *
   * @return {@code true} if this message should be visible, {@code false} otherwise
   */
  boolean isVisible();

  /**
   * Retrieves the UUID of the player who has sent the message or a UUID with the most and least
   * significant bits being 0 if the sender is unknown or there is no sender.
   * <p>
   * For versions below 1.16, this will always be the UUID for an unknown sender.
   *
   * @return The non-null UUID of the sender of the received message
   */
  UUID getSenderId();

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
     * @param location The non-null location in the chat where the message has been sent
     * @param message  The non-null message for the new event
     * @param senderId The non-null UUID of the player who has sent the message or a UUID with the
     *                 most and least significant bits being 0 if the sender is unknown or there is
     *                 no sender
     * @return The new non-null event with the given message
     */
    ChatReceiveEvent create(
        @Assisted("location") ChatLocation location,
        @Assisted("message") ChatComponent message,
        @Assisted("senderId") UUID senderId);
  }
}
