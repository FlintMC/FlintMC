package net.labyfy.component.initializer.inject.logging;

import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.spi.InjectionPoint;
import net.labyfy.component.inject.logging.InjectLogger;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiFunction;

/**
 * Provides injection for a {@link Logger} annoted with {@link InjectLogger}.
 */
public class AnnotatedLoggerTypeListener implements BiFunction<InjectionPoint, Key<Logger>, Logger> {

  private final AtomicReference<Injector> atomicInjectorReference;

  public AnnotatedLoggerTypeListener(AtomicReference<Injector> atomicInjectorReference) {
    this.atomicInjectorReference = atomicInjectorReference;
  }

  public Logger apply(InjectionPoint injectionPoint, Key<Logger> loggerKey) {
    InjectLogger annotation = (InjectLogger) loggerKey.getAnnotation();
    return atomicInjectorReference.get().getInstance(annotation.provider()).getLogger(injectionPoint.getDeclaringType().getRawType());
  }

}
