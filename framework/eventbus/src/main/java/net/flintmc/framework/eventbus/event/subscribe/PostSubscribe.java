package net.flintmc.framework.eventbus.event.subscribe;

import net.flintmc.framework.eventbus.event.EventPriority;
import net.flintmc.processing.autoload.DetectableAnnotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A shortcut to {@code @Subscribe(phase = Subscribe.Phase.POST)}.
 *
 * @see Subscribe
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@DetectableAnnotation
public @interface PostSubscribe {

  /**
   * Retrieves the priority of the subscribed method.
   *
   * @return The priority of this subscribed method.
   * @see Subscribe#priority()
   */
  byte priority() default EventPriority.NORMAL;
}
