package net.flintmc.mcapi.settings.flint;

import net.flintmc.framework.config.annotation.Config;
import net.flintmc.framework.config.generator.method.ConfigObjectReference;
import net.flintmc.mcapi.settings.flint.annotation.ApplicableSetting;

/**
 * This exception will be thrown when an entry in a {@link Config} is being discovered as a setting
 * but the {@link ConfigObjectReference#getSerializedType()} and {@link ApplicableSetting#types()}
 * don't match.
 */
public class InvalidSettingsException extends RuntimeException {

  /** {@inheritDoc} */
  public InvalidSettingsException(String message) {
    super(message);
  }

  /** {@inheritDoc} */
  public InvalidSettingsException(String message, Throwable cause) {
    super(message, cause);
  }
}
