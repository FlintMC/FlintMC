package net.labyfy.chat.controller;

import net.labyfy.chat.component.ChatComponent;
import net.labyfy.chat.controller.filter.FilterableChatMessage;

import java.util.UUID;

public class DefaultFilterableChatMessage extends DefaultChatMessage implements FilterableChatMessage {

  private Chat targetChat;
  private boolean allowed = true;

  public DefaultFilterableChatMessage(ChatComponent component, Chat targetChat, UUID senderUniqueId) {
    super(component, targetChat, senderUniqueId);
    this.targetChat = targetChat;
  }

  @Override
  public Chat getTargetChat() {
    return this.targetChat;
  }

  @Override
  public void setTargetChat(Chat targetChat) {
    this.targetChat = targetChat;
  }

  @Override
  public boolean isAllowed() {
    return this.allowed;
  }

  @Override
  public void setAllowed(boolean allowed) {
    this.allowed = allowed;
  }
}
