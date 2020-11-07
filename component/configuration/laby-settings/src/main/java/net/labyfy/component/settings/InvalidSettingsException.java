package net.labyfy.component.settings;

import net.labyfy.component.config.annotation.Config;
import net.labyfy.component.config.generator.method.ConfigObjectReference;
import net.labyfy.component.settings.annotation.ApplicableSetting;

/**
 * This exception will be thrown when an entry in a {@link Config} is being discovered as a setting but the {@link
 * ConfigObjectReference#getSerializedType()} and {@link ApplicableSetting#types()} don't match.
 */
public class InvalidSettingsException extends RuntimeException {

  /**
   * {@inheritDoc}
   */
  public InvalidSettingsException(String message) {
    super(message);
  }

  /**
   * {@inheritDoc}
   */
  public InvalidSettingsException(String message, Throwable cause) {
    super(message, cause);
  }

}
