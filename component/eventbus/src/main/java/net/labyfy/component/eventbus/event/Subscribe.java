package net.labyfy.component.eventbus.event;

import net.labyfy.component.eventbus.event.filter.EventGroup;
import net.labyfy.component.eventbus.event.util.EventPriority;
import net.labyfy.component.stereotype.annotation.Transitive;
import net.labyfy.component.stereotype.identifier.Identifier;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a method as an event receiver. The method will then be invoked if the given event has been fired. The method
 * needs to declare exactly one parameter. To filter the events that will be posted to this method, {@link EventGroup}
 * can be used.
 *
 * @see EventGroup
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Identifier
@Transitive
public @interface Subscribe {

  /**
   * Retrieves the priority of the subscribed method.
   *
   * @return The the subscribed method priority.
   */
  byte priority() default EventPriority.NORMAL;

  /**
   * Retrieves the phase of the subscribed method.
   *
   * @return The subscribed method phase.
   */
  Phase phase() default Phase.ANY;

  /**
   * An enumeration representing all available phases.
   */
  enum Phase {

    /**
     * If the subscribed method has {@link #ANY} as phase, the events {@link #PRE} and {@link #POST} are fired.
     */
    ANY,
    /**
     * Defines the fired event as pre/before.
     */
    PRE,
    /**
     * Defines the fired event as post/after.
     */
    POST

  }

}
