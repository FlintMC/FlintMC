package net.labyfy.component.service;

public class ServiceLoadException extends RuntimeException {
  public ServiceLoadException(String message) {
    super(message);
  }

  public ServiceLoadException(String message, Throwable cause) {
    super(message, cause);
  }
}
