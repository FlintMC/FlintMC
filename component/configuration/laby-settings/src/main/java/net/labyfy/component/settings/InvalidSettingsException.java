package net.labyfy.component.settings;

public class InvalidSettingsException extends RuntimeException {
  public InvalidSettingsException(String message) {
    super(message);
  }

  public InvalidSettingsException(String message, Throwable cause) {
    super(message, cause);
  }
}
