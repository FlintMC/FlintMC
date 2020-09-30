package net.labyfy.component.eventbus;

import net.labyfy.component.eventbus.event.subscribe.Subscribe;
import net.labyfy.component.transform.hook.Hook;

/**
 * The EventBus manages all methods with the {@link Subscribe} annotation in the project. The {@link
 * net.labyfy.component.eventbus.event.filter.EventGroup} can be used to group events and filter them before the
 * underlying method is being called.
 */
public interface EventBus {

  /**
   * Fires the given event to the bus.
   *
   * @param event The event to fire.
   * @param phase The phase when the event is fired.
   * @param <E>   The type of the fired event.
   * @return The input event
   */
  <E> E fireEvent(E event, Subscribe.Phase phase);

  /**
   * Fires the given event to the bus.
   *
   * @param event         The event to fire.
   * @param executionTime The execution time of a hooked method.
   * @param <E>           The type of the fired event.
   * @return The input event
   */
  default <E> E fireEvent(E event, Hook.ExecutionTime executionTime) {
    switch (executionTime) {
      case BEFORE:
        return this.fireEvent(event, Subscribe.Phase.PRE);
      case AFTER:
        return this.fireEvent(event, Subscribe.Phase.POST);
      default:
        throw new IllegalStateException("Unexpected value: " + executionTime);
    }
  }


}
