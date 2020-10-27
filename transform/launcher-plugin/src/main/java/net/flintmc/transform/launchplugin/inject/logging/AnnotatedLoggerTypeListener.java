package net.flintmc.transform.launchplugin.inject.logging;

import com.google.inject.Key;
import com.google.inject.spi.InjectionPoint;
import net.flintmc.framework.inject.logging.InjectLogger;
import net.flintmc.framework.inject.primitive.InjectionHolder;
import org.apache.logging.log4j.Logger;

import java.util.function.BiFunction;

/**
 * Provides injection for a {@link Logger} annoted with {@link InjectLogger}.
 */
public class AnnotatedLoggerTypeListener implements BiFunction<InjectionPoint, Key<Logger>, Logger> {

  public AnnotatedLoggerTypeListener() {
  }

  public Logger apply(InjectionPoint injectionPoint, Key<Logger> loggerKey) {
    InjectLogger annotation = (InjectLogger) loggerKey.getAnnotation();
    return InjectionHolder.getInjectedInstance(annotation.provider()).getLogger(injectionPoint.getDeclaringType().getRawType());
  }

}
