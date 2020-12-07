package net.flintmc.framework.eventbus.method;

import net.flintmc.framework.eventbus.event.Event;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;

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
   * @param holderMethod the subscribe method holding this executor
   * @throws Throwable If an exception occurred.
   */
  void invoke(E event, Subscribe.Phase phase, SubscribeMethod holderMethod) throws Throwable;
}
