package net.labyfy.component.inject.logging;

import org.apache.logging.log4j.Logger;

import java.util.function.Function;

/** Provides Log4J Logger instances. */
public interface LoggingProvider {

  /**
   * Gets the logger that should be injected into a given class.
   *
   * @param clazz owner of the Logger
   * @return a Log4J Logger
   */
  Logger getLogger(Class<?> clazz);

  /**
   * Sets the package name provide callback which will be used by the logger
   * to resolve the prefix for a specific class.
   *
   * @param prefixProvider the prefix provider to be used
   */
  void setPrefixProvider(Function<Class<?>, String> prefixProvider);
}
