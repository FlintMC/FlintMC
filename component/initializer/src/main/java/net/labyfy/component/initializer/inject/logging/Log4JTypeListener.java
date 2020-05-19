package net.labyfy.component.initializer.inject.logging;

import com.google.inject.Injector;
import com.google.inject.TypeLiteral;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import net.labyfy.component.inject.logging.InjectLogger;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicReference;

public class Log4JTypeListener implements TypeListener {

  private final AtomicReference<Injector> atomicInjectorReference;

  public Log4JTypeListener(AtomicReference<Injector> atomicInjectorReference) {

    this.atomicInjectorReference = atomicInjectorReference;
  }

  @Override
  public <T> void hear(TypeLiteral<T> typeLiteral, TypeEncounter<T> typeEncounter) {
    Class<?> clazz = typeLiteral.getRawType();
    while (clazz != null) {
      for (Field field : clazz.getDeclaredFields()) {
        if (field.getType() == Logger.class && field.isAnnotationPresent(InjectLogger.class)) {
          InjectLogger injectLogger = field.getAnnotation(InjectLogger.class);
          typeEncounter.register(
              new Log4JMembersInjector<>(
                  field, atomicInjectorReference.get().getInstance(injectLogger.provider())));
        }
      }
      clazz = clazz.getSuperclass();
    }
  }
}
