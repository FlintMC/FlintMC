package net.labyfy.component.inject.logging;

import com.google.inject.Singleton;
import net.labyfy.component.inject.implement.Implement;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.AbstractMessageFactory;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.ParameterizedMessage;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

@Singleton
@Implement(LoggingProvider.class)
public class LabyLoggingProvider implements LoggingProvider {
  private static final String LABYFY_PREFIX = "Labyfy";

  private Function<Class<?>, String> prefixProvider = (clazz) -> null;

  private final Map<Class<?>, Logger> loggerCache;

  public LabyLoggingProvider() {
    loggerCache = new ConcurrentHashMap<>();
  }

  @Override
  public Logger getLogger(Class<?> clazz) {
    return loggerCache.computeIfAbsent(clazz, this::createLogger);
  }

  @Override
  public void setPrefixProvider(Function<Class<?>, String> prefixProvider) {
    this.prefixProvider = prefixProvider;
  }

  private Logger createLogger(Class<?> clazz) {
    return LogManager.getLogger(clazz, new AbstractMessageFactory() {
      @Override
      public Message newMessage(Object message) {
        String prefix = prefixProvider.apply(clazz);
        if (prefix == null) {
          return new ParameterizedMessage("[" + LABYFY_PREFIX + " ]: {}", message);
        }

        return new ParameterizedMessage("[" + prefix + "]: {}", message);
      }

      @Override
      public Message newMessage(String message) {
        String prefix = prefixProvider.apply(clazz);
        if (prefix == null) {
          return new ParameterizedMessage("[" + LABYFY_PREFIX + "]: {}", message);
        }

        return new ParameterizedMessage("[" + prefix + "]: {}", message);
      }

      @Override
      public Message newMessage(String message, Object... params) {
        String prefix = prefixProvider.apply(clazz);
        if (prefix == null) {
          return new ParameterizedMessage("[" + LABYFY_PREFIX + "]: " + message, params);
        }

        return new ParameterizedMessage("[" + prefix + "]: " + message, params);
      }
    });
  }
}
