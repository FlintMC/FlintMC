package net.labyfy.component.inject.logging;

import com.google.inject.Singleton;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Singleton
public class LabyLoggingProvider implements LoggingProvider {

  @Override
  public Logger getLogger(Class<?> clazz) {
    return LogManager.getLogger(clazz);
  }
}
