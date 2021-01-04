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

package net.flintmc.mcapi.v1_16_4.chat;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.chat.ChatController;
import net.flintmc.mcapi.chat.ChatLocation;
import net.flintmc.mcapi.chat.MinecraftComponentMapper;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.ITextComponent;

@Singleton
@Implement(value = ChatController.class, version = "1.16.4")
public class VersionedChatController implements ChatController {

  private static final int MAX_MESSAGES = 100;
  private static final int MAX_INPUT_LENGTH = 256;

  private final MinecraftComponentMapper componentMapper;

  @Inject
  private VersionedChatController(MinecraftComponentMapper componentMapper) {
    this.componentMapper = componentMapper;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean dispatchChatInput(String message) {
    if (message.length() >= this.getChatInputLimit()) {
      // the message is longer than the maximum allowed, servers would kick the player when sending
      // this
      message = message.substring(0, this.getChatInputLimit());
    }

    Minecraft.getInstance().player.sendChatMessage(message);
    Minecraft.getInstance().ingameGUI.getChatGUI().addToSentMessages(message);
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getChatInputLimit() {
    return MAX_INPUT_LENGTH;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void displayChatMessage(ChatLocation location, ChatComponent component) {
    this.displayChatMessage(location, component, UUID.randomUUID());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void displayChatMessage(
      ChatLocation location, ChatComponent component, UUID senderUniqueId) {
    if (Minecraft.getInstance().ingameGUI == null) {
      return;
    }

    ChatType type;

    switch (location) {
      case CHAT:
        type = ChatType.CHAT;
        break;
      case ACTION_BAR:
        type = ChatType.GAME_INFO;
        break;
      case SYSTEM:
        type = ChatType.SYSTEM;
        break;
      default:
        throw new IllegalStateException("Unexpected value: " + location);
    }

    ITextComponent mapped = (ITextComponent) this.componentMapper.toMinecraft(component);
    Minecraft.getInstance().ingameGUI.func_238450_a_(type, mapped, senderUniqueId);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<String> getInputHistory() {
    if (Minecraft.getInstance().ingameGUI == null) {
      return new ArrayList<>();
    }
    return Minecraft.getInstance().ingameGUI.getChatGUI().getSentMessages();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<ChatComponent> getReceivedMessages() {
    ChatGuiShadow shadow = (ChatGuiShadow) Minecraft.getInstance().ingameGUI.getChatGUI();
    List<ChatComponent> components = new ArrayList<>(shadow.getLines().size());

    for (ChatLine line : shadow.getLines()) {
      components.add(this.componentMapper.fromMinecraft(line.getLineString()));
    }

    return components;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getMaxMessages() {
    return MAX_MESSAGES;
  }
}
