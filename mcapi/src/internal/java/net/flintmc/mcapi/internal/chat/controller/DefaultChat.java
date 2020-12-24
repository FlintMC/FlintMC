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

import net.flintmc.mcapi.chat.controller.Chat;
import net.flintmc.mcapi.chat.controller.ChatMessage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DefaultChat implements Chat {

  private static final int MAX_MESSAGES = 100;

  private final int id;

  private final List<ChatMessage> messages = new ArrayList<>();

  public DefaultChat(int id) {
    this.id = id;
  }

  @Override
  public int getId() {
    return this.id;
  }

  @Override
  public List<ChatMessage> getReceivedMessages() {
    return Collections.unmodifiableList(this.messages);
  }

  @Override
  public int getMaxMessages() {
    return MAX_MESSAGES;
  }

  @Override
  public void displayChatMessage(ChatMessage message) {
    if (message.getTargetChat() != this) {
      throw new IllegalArgumentException("The given message is not applicable for this chat");
    }

    this.messages.add(0, message);

    while (this.messages.size() > this.getMaxMessages()) {
      this.messages.remove(this.messages.size() - 1);
    }

    // TODO display the message in the ChatGui
  }
}
