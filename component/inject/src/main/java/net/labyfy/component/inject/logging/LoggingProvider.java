package net.labyfy.component.inject.logging;

import org.apache.logging.log4j.Logger;

/** Provides Log4J Logger instances. */
public interface LoggingProvider {

  /**
   * Gets the logger that should be injected into a given class.
   *
   * @param clazz owner of the Logger
   * @return a Log4J Logger
   */
  Logger getLogger(Class<?> clazz);
}
