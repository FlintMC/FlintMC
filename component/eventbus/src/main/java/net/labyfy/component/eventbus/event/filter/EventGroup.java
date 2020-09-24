package net.labyfy.component.eventbus.event.filter;

import net.labyfy.component.stereotype.annotation.Transitive;
import net.labyfy.component.stereotype.identifier.Identifier;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Bounds a custom annotation to an event class.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
@Identifier
@Transitive
public @interface EventGroup {

  /**
   * Retrieves the event class.
   *
   * @return An event class
   */
  Class<?> groupEvent();

}
