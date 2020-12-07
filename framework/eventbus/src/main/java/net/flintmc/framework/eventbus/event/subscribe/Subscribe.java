package net.flintmc.framework.eventbus.event.subscribe;

import net.flintmc.framework.eventbus.event.Event;
import net.flintmc.framework.eventbus.event.EventPriority;
import net.flintmc.processing.autoload.DetectableAnnotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a method as an event receiver. The method will then be invoked if the given event has been
 * fired. The method needs to declare exactly one parameter which has to be an {@link Event}.
 *
 * <p>For simpler modification of the {@link #phase()}, {@link PreSubscribe} and {@link
 * PostSubscribe} can also be used.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@DetectableAnnotation
public @interface Subscribe {

  /**
   * Retrieves the priority of the subscribed method.
   *
   * @return The priority of this subscribed method.
   */
  byte priority() default EventPriority.NORMAL;

  /**
   * Retrieves the phase of the subscribed method.
   *
   * @return The subscribed method phase.
   */
  Phase phase() default Phase.PRE;

  /** An enumeration representing all available phases. */
  enum Phase {

    /**
     * Subscribed methods with this phase will be fired with the {@link #PRE} and {@link #POST}
     * phases.
     */
    ANY,
    /** Defines the fired event as pre/before. */
    PRE,
    /** Defines the fired event as post/after. */
    POST
  }
}
