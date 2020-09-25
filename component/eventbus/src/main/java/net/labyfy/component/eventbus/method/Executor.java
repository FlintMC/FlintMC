package net.labyfy.component.eventbus.method;

import java.lang.reflect.Method;

/**
 * A functional interface that can invoke a defined method on a listener object when an event is fired.
 */
@FunctionalInterface
public interface Executor {

  /**
   * Invokes the appropriate method on the given listener to handle the event.
   *
   * @param listener The listener.
   * @param event    The event.
   * @throws Throwable If an exception occurred.
   */
  void invoke(Object listener, Object event) throws Throwable;

  /**
   * Factory for {@link Executor}'s.
   */
  @FunctionalInterface
  interface Factory {

    /**
     * Creates an {@link Executor}.
     *
     * @param listener The listener object
     * @param method   The method to call on the object.
     * @return An created executor.
     * @throws Exception If an exception occurred while creating an executor.
     */
    Executor create(Object listener, Method method) throws Exception;

  }

}
