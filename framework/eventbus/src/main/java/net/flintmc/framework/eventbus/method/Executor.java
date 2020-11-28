package net.flintmc.framework.eventbus.method;

import javassist.CtMethod;
import net.flintmc.framework.eventbus.event.Event;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;

import java.util.function.Supplier;

/**
 * An interface that can invoke a defined method on a listener object when an event is fired.
 *
 * @param <E> The type of the event that can be executed by this executor
 */
public interface Executor<E extends Event> {

  /**
   * Invokes the appropriate method on the given listener to handle the event.
   *
   * @param event The event.
   * @param phase The phase in which the given event has been fired
   * @throws Throwable If an exception occurred.
   */
  void invoke(E event, Subscribe.Phase phase) throws Throwable;


}
