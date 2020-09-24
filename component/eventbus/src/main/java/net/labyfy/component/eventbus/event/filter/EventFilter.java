package net.labyfy.component.eventbus.event.filter;

import net.labyfy.component.eventbus.method.SubscribeMethod;

/**
 * A filter that is used to check whether the given event should be called to this method.
 */
public interface EventFilter {

  /**
   * Checks whether the values in the given event match the values in the {@link EventGroup} annotation of the given
   * method.
   *
   * @param event  The non-null event to be checked
   * @param method The non-null method to be checked
   * @return Whether the values in the event match the values in the {@link EventGroup} annotation in the given method
   * or {@code true} if given method doesn't have an annotation that is annotated with {@link EventGroup}.
   */
  boolean matches(Object event, SubscribeMethod method);

}
