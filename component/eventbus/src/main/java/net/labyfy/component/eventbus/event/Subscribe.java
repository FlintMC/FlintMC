package net.labyfy.component.eventbus.event;

import net.labyfy.component.stereotype.annotation.Transitive;
import net.labyfy.component.stereotype.identifier.Identifier;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a method as an event receiver. The method will then be invoked if the given event has been fired.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Identifier
@Transitive
public @interface Subscribe {

  /**
   * Whether the event is fired asynchronously into the bus.
   *
   * @return {@code true} if the event is fired asynchronously into the bus.
   */
  boolean async() default false;

  /**
   * Retrieves the priority.
   *
   * @return The priority.
   */
  byte priority() default 0;

  Phase phase() default Phase.PRE;

  enum Phase {

    ANY,
    PRE,
    POST

  }

}
