package net.labyfy.chat.exception;

import net.labyfy.chat.serializer.ComponentSerializer;

/**
 * This exception will be thrown by the {@link ComponentSerializer} when an error occurred while deserializing a string
 * into a chat component.
 */
public class ComponentDeserializationException extends RuntimeException {
  public ComponentDeserializationException(String message) {
    super(message);
  }

  public ComponentDeserializationException(String message, Throwable cause) {
    super(message, cause);
  }

  public ComponentDeserializationException(Throwable cause) {
    super(cause);
  }
}
