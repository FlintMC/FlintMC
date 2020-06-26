package net.labyfy.component.initializer.inject.logging;

import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.spi.InjectionPoint;
import net.labyfy.component.inject.logging.LoggingProvider;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiFunction;

/**
 * Provides injection for a {@link Logger}.
 */
public class LoggerTypeListener implements BiFunction<InjectionPoint, Key<Logger>, Logger> {

  private final AtomicReference<Injector> atomicInjectorReference;

  public LoggerTypeListener(AtomicReference<Injector> atomicInjectorReference) {
    this.atomicInjectorReference = atomicInjectorReference;
  }

  public Logger apply(InjectionPoint injectionPoint, Key<Logger> loggerKey) {
    return atomicInjectorReference.get().getInstance(LoggingProvider.class).getLogger(injectionPoint.getDeclaringType().getRawType());
  }
}
