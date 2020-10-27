package net.flintmc.mcapi.chat.internal.controller;

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
