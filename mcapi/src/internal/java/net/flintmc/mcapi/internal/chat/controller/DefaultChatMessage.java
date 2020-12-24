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

package net.flintmc.mcapi.internal.chat.controller;

import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.chat.controller.Chat;
import net.flintmc.mcapi.chat.controller.ChatMessage;

import java.util.UUID;

public class DefaultChatMessage implements ChatMessage {

  private final ChatComponent component;
  private final Chat targetChat;
  private final UUID senderUniqueId;
  private final long timestamp;
  private int highlightColor;
  private boolean highlighted;

  public DefaultChatMessage(ChatComponent component, Chat targetChat, UUID senderUniqueId) {
    this.component = component;
    this.targetChat = targetChat;
    this.senderUniqueId = senderUniqueId;
    this.timestamp = System.currentTimeMillis();
  }

  @Override
  public ChatComponent getComponent() {
    return this.component;
  }

  @Override
  public int getHighlightColor() {
    return this.highlightColor;
  }

  @Override
  public void setHighlightColor(int color) {
    this.highlightColor = color;
  }

  @Override
  public boolean isHighlighted() {
    return this.highlighted;
  }

  @Override
  public void setHighlighted(boolean highlighted) {
    this.highlighted = highlighted;
  }

  @Override
  public Chat getTargetChat() {
    return this.targetChat;
  }

  @Override
  public long getTimestamp() {
    return this.timestamp;
  }

  @Override
  public UUID getSenderUniqueId() {
    return this.senderUniqueId;
  }
}
