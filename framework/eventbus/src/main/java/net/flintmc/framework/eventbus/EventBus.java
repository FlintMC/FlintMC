package net.flintmc.framework.eventbus;

import com.google.common.collect.Multimap;
import net.flintmc.framework.eventbus.event.Event;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.framework.eventbus.method.SubscribeMethod;
import net.flintmc.framework.eventbus.method.SubscribeMethodBuilder;
import net.flintmc.transform.hook.Hook;

/**
 * The EventBus manages all methods with the {@link Subscribe} annotation and custom event handlers
 * in the project.
 */
public interface EventBus {

  /**
   * Fires the given event to the bus.
   *
   * @param event The event to fire.
   * @param phase The phase when the event is fired.
   * @param <E> The type of the fired event.
   * @return The input event
   */
  <E extends Event> E fireEvent(E event, Subscribe.Phase phase);

  /**
   * Fires the given event to the bus.
   *
   * @param event The event to fire.
   * @param executionTime The execution time of a hooked method.
   * @param <E> The type of the fired event.
   * @return The input event
   */
  default <E extends Event> E fireEvent(E event, Hook.ExecutionTime executionTime) {
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
  Multimap<Class<? extends Event>, SubscribeMethod> getSubscribeMethods();

  /**
   * Registers a new {@link SubscribeMethod} to this event bus, the executor in this method will be
   * fired whenever {@link #fireEvent(Event, Subscribe.Phase)} is called. A method may be registered
   * multiple times.
   *
   * @param method The non-null method to be registered
   * @see SubscribeMethodBuilder
   * @see #unregisterSubscribeMethod(SubscribeMethod)
   */
  void registerSubscribeMethod(SubscribeMethod method);

  /**
   * Unregisters a {@link SubscribeMethod} from this event bus that has previously been registered
   * via {@link #registerSubscribeMethod(SubscribeMethod)}. If the method is not registered, nothing
   * will happen.
   *
   * @param method The non-null method to be unregistered
   * @see #registerSubscribeMethod(SubscribeMethod)
   */
  void unregisterSubscribeMethod(SubscribeMethod method);

  /**
   * Unregisters all {@link SubscribeMethod}s that are subscribed to the given {@code eventClass}.
   * If there is no method with the event registered, nothing will happen.
   *
   * @param eventClass The non-null event to unregister every method for
   * @see #registerSubscribeMethod(SubscribeMethod)
   */
  void unregisterSubscribeMethods(Class<? extends Event> eventClass);
}
