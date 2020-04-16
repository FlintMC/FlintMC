package net.labyfy.component.inject.logging;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** Marks where an appropriate Log4J Logger should be injected. */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface InjectLogger {

  /**
   * @return a Class that implements LoggingProvider of which an instance can be created using
   *     Guice.
   */
  Class<? extends LoggingProvider> provider() default LabyLoggingProvider.class;
}
