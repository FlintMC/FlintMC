package net.flintmc.mcapi.chat.controller.filter;

import net.flintmc.mcapi.chat.controller.Chat;
import net.flintmc.mcapi.chat.controller.ChatMessage;

/**
 * The message before it is being displayed in the chat. In this message, the {@link Chat} to
 * display the message to can still be modified.
 */
public interface FilterableChatMessage extends ChatMessage {

  /**
   * Sets the chat where this message will be displayed.
   *
   * @param chat The non-null chat to display this message
   */
  void setTargetChat(Chat chat);

  /**
   * Retrieves whether this message will be displayed in the target chat or not.
   *
   * @return whether this message will be displayed or not
   */
  boolean isAllowed();

  /**
   * Sets whether this message will be displayed in the target chat or not.
   *
   * @param allowed {@code true} if the message should be displayed or {@code false} if not
   */
  void setAllowed(boolean allowed);
}
