package net.labyfy.component.processing.exception;

import javax.lang.model.element.Element;

public class ProcessingException extends RuntimeException {
  private final Element sourceElement;

  public ProcessingException(String message) {
    super(message);
    this.sourceElement = null;
  }

  public ProcessingException(String message, Throwable cause) {
    super(message, cause);
    this.sourceElement = null;
  }

  public ProcessingException(String message, Element sourceElement) {
    super(message);
    this.sourceElement = sourceElement;
  }

  public ProcessingException(String message, Throwable cause, Element sourceElement) {
    super(message, cause);
    this.sourceElement = sourceElement;
  }

  public Element getSourceElement() {
    return sourceElement;
  }
}
