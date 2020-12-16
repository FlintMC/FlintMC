package net.flintmc.mcapi.chat.exception;

import net.flintmc.mcapi.chat.builder.SelectorComponentBuilder;

/**
 * This exception will be thrown by {@link SelectorComponentBuilder#parse(String)} if a wrong format
 * has been provided.
 */
public class InvalidSelectorException extends RuntimeException {

  public InvalidSelectorException(String message) {
    super(message);
  }
}
