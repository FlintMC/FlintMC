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

package net.flintmc.mcapi.v1_15_2.chat;

import com.google.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.chat.controller.ChatController;
import net.flintmc.mcapi.chat.controller.filter.FilterableChatMessage;
import net.flintmc.mcapi.internal.chat.controller.DefaultChatController;
import net.flintmc.mcapi.internal.chat.controller.DefaultFilterableChatMessage;
import net.minecraft.client.Minecraft;

@Singleton
@Implement(value = ChatController.class, version = "1.15.2")
public class VersionedDefaultChatController extends DefaultChatController {

  @Override
  public boolean dispatchChatInput(String message) {
    if (message.length() >= this.getChatInputLimit()) {
      // the message is longer than the maximum allowed, servers would kick the player when sending
      // this
      message = message.substring(0, this.getChatInputLimit());
    }

    Minecraft.getInstance().player.sendChatMessage(message);
    return true;
  }

  @Override
  public int getChatInputLimit() {
    return 256;
  }

  @Override
  public boolean displayChatMessage(ChatComponent component, UUID senderUniqueId) {
    if (Minecraft.getInstance().ingameGUI == null) {
      return false;
    }

    FilterableChatMessage message =
        new DefaultFilterableChatMessage(component, this.getMainChat(), senderUniqueId);
    return super.processMessage(message);
  }

  @Override
  public List<String> getInputHistory() {
    if (Minecraft.getInstance().ingameGUI == null) {
      return new ArrayList<>();
    }
    return Minecraft.getInstance().ingameGUI.getChatGUI().getSentMessages();
  }
}
