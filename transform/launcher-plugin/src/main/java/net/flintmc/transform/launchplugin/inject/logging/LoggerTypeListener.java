package net.flintmc.transform.launchplugin.inject.logging;

import com.google.inject.Key;
import com.google.inject.spi.InjectionPoint;
import net.flintmc.framework.inject.logging.LoggingProvider;
import net.flintmc.framework.inject.primitive.InjectionHolder;
import org.apache.logging.log4j.Logger;

import java.util.function.BiFunction;

/** Provides injection for a {@link Logger}. */
public class LoggerTypeListener implements BiFunction<InjectionPoint, Key<Logger>, Logger> {

  public Logger apply(InjectionPoint injectionPoint, Key<Logger> loggerKey) {
    return InjectionHolder.getInjectedInstance(LoggingProvider.class)
        .getLogger(injectionPoint.getDeclaringType().getRawType());
  }
}
