package net.flintmc.processing.exception;

import javax.lang.model.element.Element;

/**
 * Represents a general, fatal failure while processing. This should only be used for cases such as
 * failing to write a file due to an {@link java.io.IOException} or similar, whereas invalid
 * annotations should be signaled as errored via the current {@link
 * javax.annotation.processing.Messager}
 */
public class ProcessingException extends RuntimeException {
  private final Element sourceElement;

  /**
   * Creates a new {@link ProcessingException} with the specified message. The {@link Element}
   * causing this exception is unknown.
   *
   * @param message The message to pass to the {@link RuntimeException}
   */
  public ProcessingException(String message) {
    super(message);
    this.sourceElement = null;
  }

  /**
   * Creates a new {@link ProcessingException} with the specified message and cause. The {@link
   * Element} causing this exception is unknown.
   *
   * @param message The message to pass to the {@link RuntimeException}
   * @param cause The cause to pass to the {@link RuntimeException}
   */
  public ProcessingException(String message, Throwable cause) {
    super(message, cause);
    this.sourceElement = null;
  }

  /**
   * Creates a new {@link ProcessingException} with the specified message and an element indicating
   * the source of the exception.
   *
   * @param message The message to pass to the {@link RuntimeException}
   * @param sourceElement The element during which processing the exception occurred
   */
  public ProcessingException(String message, Element sourceElement) {
    super(message);
    this.sourceElement = sourceElement;
  }

  /**
   * Creates a new {@link ProcessingException} with the specified message, a cause and an element
   * indicating the source of the exception.
   *
   * @param message The message to pass to the {@link RuntimeException}
   * @param cause The cause to pass to the {@link RuntimeException}
   * @param sourceElement The element during which processing the exception occurred
   */
  public ProcessingException(String message, Throwable cause, Element sourceElement) {
    super(message, cause);
    this.sourceElement = sourceElement;
  }

  /**
   * Retrieves the element during which processing the exception occurred.
   *
   * @return The element which processing caused the exception, or null if not caused by handling of
   *     an element
   */
  public Element getSourceElement() {
    return sourceElement;
  }
}
