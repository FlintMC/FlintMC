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

import java.util.UUID;
import net.flintmc.mcapi.chat.component.ChatComponent;

/**
 * This class defines a message in the chat. If a message consists of multiple lines, it will still
 * be one {@link ChatMessage}.
 */
public interface ChatMessage {

  /**
   * Retrieves the component of this chat message.
   *
   * @return The non-null component
   */
  ChatComponent getComponent();

  /**
   * Retrieves the RGB value of the highlight color of this message or -1 if no color has been set.
   *
   * @return The RGB value of the highlight color as an int
   * @see #isHighlighted()
   */
  int getHighlightColor();

  /**
   * Changes or removes the highlight color of this message.
   *
   * @param color The RGB value of the new highlight color for this message
   */
  void setHighlightColor(int color);

  /**
   * Retrieves whether this message is highlighted in the chat in the {@link #getHighlightColor()}.
   *
   * @return {@code true} if the message should be highlighted or {@code false} if not
   */
  boolean isHighlighted();

  /**
   * Sets whether this message is highlighted in the chat in the {@link #setHighlightColor(int)}.
   *
   * @param highlighted Whether this message should be highlighted or not
   */
  void setHighlighted(boolean highlighted);

  /**
   * Retrieves the chat where this line is being displayed.
   *
   * @return The non-null chat
   */
  Chat getTargetChat();

  /**
   * Retrieves the timestamp when this message has been created.
   *
   * @return The timestamp in milliseconds since 01/01/1970
   */
  long getTimestamp();

  /**
   * Retrieves the UUID of the sender of this message. If the message has no sender or this client
   * doesn't use 1.16+, the most- and leastSignificantBits in the UUID will both be 0.
   *
   * <p>This feature is available since Minecraft 1.16.
   *
   * @return The non-null UUID of the sender of this message
   */
  UUID getSenderUniqueId();
}
