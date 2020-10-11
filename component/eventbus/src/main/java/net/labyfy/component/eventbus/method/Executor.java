package net.labyfy.component.eventbus.method;

import javassist.CtClass;
import javassist.CtMethod;

/** An interface that can invoke a defined method on a listener object when an event is fired. */
public interface Executor {

  /**
   * Invokes the appropriate method on the given listener to handle the event.
   *
   * @param listener The listener.
   * @param event The event.
   * @throws Throwable If an exception occurred.
   */
  void invoke(Object listener, Object event) throws Throwable;

  /** Factory for {@link Executor}'s. */
  interface Factory {

    /**
     * Creates an {@link Executor}.
     *
     * @param declaringClass The class declaring the listener method.
     * @param method The method to call on the object.
     * @return An created executor.
     * @throws Exception If an exception occurred while creating an executor.
     * @throws IllegalAccessException If the class or its nullary constructor is not accessible.
     * @throws InstantiationException If this {@link Class} represents an abstract class, an
     *     interface, an array class, a primitive type, or void; or if the class has not nullary
     *     constructor; or if the instantiation fails or some other reason.
     */
    Executor create(CtClass declaringClass, CtMethod method) throws Exception;
  }
}
