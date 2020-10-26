package net.labyfy.chat.controller;

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
