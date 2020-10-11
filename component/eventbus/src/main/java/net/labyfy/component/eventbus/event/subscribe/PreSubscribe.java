package net.labyfy.component.eventbus.event.subscribe;

import net.labyfy.component.eventbus.event.util.EventPriority;
import net.labyfy.component.processing.autoload.DetectableAnnotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A shortcut to {@code @Subscribe(phase = Subscribe.Phase.PRE)}.
 *
 * @see Subscribe
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@DetectableAnnotation
public @interface PreSubscribe {

  /**
   * Retrieves the priority of the subscribed method.
   *
   * @return The priority of this subscribed method.
   * @see Subscribe#priority()
   */
  byte priority() default EventPriority.NORMAL;
}
