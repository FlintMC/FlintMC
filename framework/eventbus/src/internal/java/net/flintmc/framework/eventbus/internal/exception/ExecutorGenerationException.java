package net.flintmc.framework.eventbus.internal.exception;

import net.flintmc.framework.eventbus.method.EventExecutor;

import java.lang.reflect.Method;

/**
 * Exception thrown when an {@link EventExecutor} cannot be generated for a {@link Method} at runtime.
 */
public class ExecutorGenerationException extends RuntimeException {

  public ExecutorGenerationException(String message) {
    super(message);
  }

  public ExecutorGenerationException(String message, Throwable cause) {
    super(message, cause);
  }
}
