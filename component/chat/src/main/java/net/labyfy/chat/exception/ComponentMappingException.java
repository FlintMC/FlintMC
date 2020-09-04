package net.labyfy.chat.exception;

import net.labyfy.chat.MinecraftComponentMapper;

/**
 * This exception will be thrown when an error occurred in the {@link MinecraftComponentMapper}.
 */
public class ComponentMappingException extends RuntimeException {
  public ComponentMappingException(String message) {
    super(message);
  }
}
