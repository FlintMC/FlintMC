package net.labyfy.internal.webgui.ultralight;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labyfy.component.inject.logging.InjectLogger;
import net.labymedia.ultralight.plugin.logging.UltralightLogLevel;
import net.labymedia.ultralight.plugin.logging.UltralightLogger;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

/**
 * Simple logging bridge to convert Ultralight log messages to Log4J messages.
 */
@Singleton
public class UltralightLoggingBridge implements UltralightLogger {
  private final Logger logger;

  @Inject
  private UltralightLoggingBridge(@InjectLogger Logger logger) {
    this.logger = logger;
  }

  @Override
  public void logMessage(UltralightLogLevel level, String message) {
    Level log4jLevel = null;

    // Translate the log level from Ultralight to Log4J
    switch(level) {
      case INFO:
        log4jLevel = Level.INFO;
        break;

      case WARNING:
        log4jLevel = Level.WARN;
        break;

      case ERROR:
        log4jLevel = Level.ERROR;
        break;
    }

    // Dispatch the message
    logger.log(log4jLevel, message);
  }
}
