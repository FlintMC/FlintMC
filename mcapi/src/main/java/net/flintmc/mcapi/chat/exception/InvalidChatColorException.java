package net.flintmc.mcapi.chat.exception;

import net.flintmc.mcapi.chat.format.ChatColor;

/**
 * This exception will be thrown by the {@link ChatColor} if a wrong color has been provided for
 * parsing.
 */
public class InvalidChatColorException extends RuntimeException {
  public InvalidChatColorException(String message) {
    super(message);
  }

  public InvalidChatColorException(String message, Throwable cause) {
    super(message, cause);
  }
}
