package net.labyfy.chat.exception;

/**
 * This exception will be thrown by the {@link net.labyfy.chat.format.ChatColor} if a wrong color has been provided for
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
