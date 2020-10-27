package net.flintmc.mcapi.chat.controller;

import java.util.List;

/**
 * This class defines a chat window on the screen which contains messages. A {@link ChatController} can contain multiple
 * {@link Chat}s.
 */
public interface Chat {

  /**
   * Retrieves the id of this chat. The id is unique for every chat in a controller.
   *
   * @return The id of this chat
   */
  int getId();

  /**
   * Retrieves a list of all messages that are displayed in this chat. The size of this list will never be greater than
   * {@link #getMaxMessages()}.
   *
   * @return A non-null and unmodifiable list with all messages
   */
  List<ChatMessage> getReceivedMessages();

  /**
   * Retrieves the max messages that can be stored in this chat.
   *
   * @return An int which is always larger than 0
   */
  int getMaxMessages();

  /**
   * Displays the given message in this chat and removes the last messages out of the received messages in this chat if
   * there are more messages than {@link #getMaxMessages()}.
   *
   * @param message The new non-null message to be displayed
   * @throws IllegalArgumentException If the {@link ChatMessage#getTargetChat()} is not the same as this chat
   */
  void displayChatMessage(ChatMessage message);
}
