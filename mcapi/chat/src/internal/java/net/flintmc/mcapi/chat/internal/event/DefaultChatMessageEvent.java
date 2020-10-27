package net.flintmc.mcapi.chat.internal.event;

import net.flintmc.mcapi.chat.event.ChatMessageEvent;

public class DefaultChatMessageEvent implements ChatMessageEvent {

  private final Type type;

  public DefaultChatMessageEvent(Type type) {
    this.type = type;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Type getType() {
    return this.type;
  }
}
