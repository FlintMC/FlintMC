package net.labyfy.component.initializer.inject.logging;

import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.google.inject.spi.InjectionPoint;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import net.labyfy.component.inject.logging.InjectLogger;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiFunction;
import java.util.function.Function;

public class Log4JTypeListener implements BiFunction<InjectionPoint, Key<Logger>, Logger> {

  private final AtomicReference<Injector> atomicInjectorReference;

  public Log4JTypeListener(AtomicReference<Injector> atomicInjectorReference) {

    this.atomicInjectorReference = atomicInjectorReference;
  }

  public Logger apply(InjectionPoint injectionPoint, Key<Logger> loggerKey) {
    InjectLogger annotation = (InjectLogger) loggerKey.getAnnotation();
    return atomicInjectorReference.get().getInstance(annotation.provider()).getLogger(injectionPoint.getDeclaringType().getRawType());
  }

}
