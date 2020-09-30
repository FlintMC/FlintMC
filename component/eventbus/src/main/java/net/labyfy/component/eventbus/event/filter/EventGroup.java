package net.labyfy.component.eventbus.event.filter;

import net.labyfy.component.eventbus.event.subscribe.Subscribe;
import net.labyfy.component.stereotype.annotation.Transitive;
import net.labyfy.component.stereotype.identifier.Identifier;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Bounds a custom annotation to an event class. Annotations that are annotated with this annotation can be used on a
 * method that is annotated with {@link Subscribe} to filter the events that will be posted to the underlying method.
 * <p>
 * The values of the annotation can be connected with methods in the event by either using the {@link
 * com.google.inject.name.Named} annotation by Guice or if this annotation is not present on the method in the
 * annotation, the event class will be searched for methods with the same return type.
 *
 * @see Subscribe
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
@Identifier
@Transitive
public @interface EventGroup {

  /**
   * Retrieves the event class which marks this group. Methods that shouldn't ignore this group, need to declare exactly
   * one parameter that has this class as one of its superclasses.
   *
   * @return The non-null event class of this group
   */
  Class<?> groupEvent();

}
