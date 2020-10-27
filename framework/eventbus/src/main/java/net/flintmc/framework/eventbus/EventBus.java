package net.flintmc.framework.eventbus;

import com.google.common.collect.Multimap;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.framework.eventbus.method.SubscribeMethod;
import net.labyfy.component.transform.hook.Hook;

/** The EventBus manages all methods with the {@link Subscribe} annotation in the project. */
public interface EventBus {

  /**
   * Fires the given event to the bus.
   *
   * @param event The event to fire.
   * @param phase The phase when the event is fired.
   * @param <E> The type of the fired event.
   * @return The input event
   */
  <E> E fireEvent(E event, Subscribe.Phase phase);

  /**
   * Fires the given event to the bus.
   *
   * @param event The event to fire.
   * @param executionTime The execution time of a hooked method.
   * @param <E> The type of the fired event.
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

  /**
   * Retrieves a {@link Multimap} that contains all subscribe methods.
   *
   * @return A multimap that contains all subscribe methods.
   */
  Multimap<String, SubscribeMethod> getSubscribeMethods();
}
