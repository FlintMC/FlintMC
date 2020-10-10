package net.labyfy.component.inject.event;

import net.labyfy.component.processing.autoload.DetectableAnnotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a method as an event receiver. The method will then be invoked if the given
 * event has been fired.
 *
 * @deprecated The event system will change and this class will be removed
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@DetectableAnnotation
@Deprecated
public @interface Event {
  /**
   * The class of the event to listen to, will also be available as a parameter to the method.
   *
   * @return The class of the event to listen to
   */
  Class<?> value();
}
