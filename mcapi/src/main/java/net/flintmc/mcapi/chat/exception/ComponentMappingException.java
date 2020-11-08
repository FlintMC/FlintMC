package net.flintmc.mcapi.chat.exception;

import net.flintmc.mcapi.chat.MinecraftComponentMapper;

/** This exception will be thrown when an error occurred in the {@link MinecraftComponentMapper}. */
public class ComponentMappingException extends RuntimeException {
  public ComponentMappingException(String message) {
    super(message);
  }
}
