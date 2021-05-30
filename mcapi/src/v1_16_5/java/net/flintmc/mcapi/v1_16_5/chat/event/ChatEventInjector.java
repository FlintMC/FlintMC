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

package net.flintmc.mcapi.v1_16_5.chat.event;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import java.util.UUID;
import net.flintmc.framework.eventbus.EventBus;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.framework.stereotype.type.Type;
import net.flintmc.mcapi.chat.ChatLocation;
import net.flintmc.mcapi.chat.MinecraftComponentMapper;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.chat.event.ChatReceiveEvent;
import net.flintmc.mcapi.chat.event.ChatSendEvent;
import net.flintmc.transform.hook.Hook;
import net.flintmc.transform.hook.Hook.ExecutionTime;
import net.flintmc.transform.hook.HookFilter;
import net.flintmc.transform.hook.HookFilters;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.ITextComponent;

@Singleton
public class ChatEventInjector {

  private static final UUID NO_SENDER = new UUID(0, 0);

  private final EventBus eventBus;
  private final MinecraftComponentMapper componentMapper;

  private final ChatSendEvent.Factory sendFactory;
  private final ChatReceiveEvent.Factory receiveFactory;

  private ReceivedChatMessage lastMessage;

  @Inject
  private ChatEventInjector(
      EventBus eventBus,
      MinecraftComponentMapper componentMapper,
      ChatSendEvent.Factory sendFactory,
      ChatReceiveEvent.Factory receiveFactory) {
    this.eventBus = eventBus;
    this.componentMapper = componentMapper;
    this.sendFactory = sendFactory;
    this.receiveFactory = receiveFactory;
  }

  @HookFilter(
      value = HookFilters.SUBCLASS_OF,
      type = @Type(typeName = "net.minecraft.client.gui.chat.IChatListener"))
  @Hook(
      methodName = "say",
      parameters = {
          @Type(typeName = "net.minecraft.util.text.ChatType"),
          @Type(typeName = "net.minecraft.util.text.ITextComponent"),
          @Type(reference = UUID.class)
      },
      executionTime = ExecutionTime.BEFORE)
  public void handleMessage(@Named("args") Object[] args) {
    ITextComponent component = (ITextComponent) args[1];
    if (this.lastMessage != null && this.lastMessage.getComponent() == component) {
      // This method may be called multiple times but this is only necessary once per message
      return;
    }

    ChatType type = (ChatType) args[0];

    ChatLocation location;
    switch (type) {
      case CHAT:
        location = ChatLocation.CHAT;
        break;
      case SYSTEM:
        location = ChatLocation.SYSTEM;
        break;
      case GAME_INFO:
        location = ChatLocation.ACTION_BAR;
        break;
      default:
        throw new IllegalStateException("Unexpected value: " + type);
    }

    ChatComponent flintComponent = this.componentMapper.fromMinecraft(component);
    UUID senderId = (UUID) args[2];
    this.lastMessage = new ReceivedChatMessage(component, flintComponent, location, senderId);
  }

  public ITextComponent handleChatReceive(
      ITextComponent component, Subscribe.Phase phase) {
    ChatLocation location;
    ChatComponent flintComponent;
    UUID senderId;
    if (this.lastMessage != null && this.lastMessage.getMinecraftComponent() == component) {
      location = this.lastMessage.getLocation();
      flintComponent = this.lastMessage.getComponent();
      senderId = this.lastMessage.getSenderId();
    } else {
      location = ChatLocation.SYSTEM;
      flintComponent = this.componentMapper.fromMinecraft(component);
      senderId = NO_SENDER;
    }

    ChatReceiveEvent event = this.receiveFactory.create(location, flintComponent, senderId);
    this.eventBus.fireEvent(event, phase);

    if (phase != Subscribe.Phase.PRE || event.isCancelled()) {
      return null;
    }

    ITextComponent result = (ITextComponent) this.componentMapper.toMinecraft(event.getMessage());
    this.lastMessage = new ReceivedChatMessage(result, event.getMessage(), location, senderId);
    return result;
  }

  public String handleChatSend(String message, Subscribe.Phase phase) {
    ChatSendEvent event = this.sendFactory.create(message);
    this.eventBus.fireEvent(event, phase);
    return event.isCancelled() ? null : event.getMessage();
  }
}
