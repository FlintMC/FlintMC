package net.flintmc.framework.config.annotation;

import net.flintmc.framework.eventbus.event.Event;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation can be used along with {@link Config} so that the config will be read from the
 * storages after a specific {@link Event} in a specific {@link Subscribe.Phase} was fired.
 *
 * <p>If this annotation is not provided, the config will directly be read after it has been
 * discovered.
 *
 * @see Config
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ConfigInit {

  /**
   * The class of the event that triggers the config initialization.
   *
   * @return The non-null class of the event
   */
  Class<? extends Event> eventClass();

  /**
   * The event phase that triggers the config initialization.
   *
   * @return The non-null phase
   */
  Subscribe.Phase eventPhase();
}
