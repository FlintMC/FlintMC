package net.flintmc.framework.inject.method;

/**
 * This exception will be thrown when an error occurred while generating a {@link MethodInjector}.
 *
 * @see MethodInjector
 * @see MethodInjector.Factory
 */
public class MethodInjectorGenerationException extends RuntimeException {

  /** {@inheritDoc} */
  public MethodInjectorGenerationException(String message) {
    super(message);
  }

  /** {@inheritDoc} */
  public MethodInjectorGenerationException(String message, Throwable cause) {
    super(message, cause);
  }
}
