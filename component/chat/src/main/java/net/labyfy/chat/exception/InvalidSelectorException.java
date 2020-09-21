package net.labyfy.chat.exception;

/**
 * This exception will be thrown by {@link net.labyfy.chat.builder.SelectorComponentBuilder#parse(String)} if a wrong
 * format has been provided.
 */
public class InvalidSelectorException extends RuntimeException {
  public InvalidSelectorException(String message) {
    super(message);
  }
}
