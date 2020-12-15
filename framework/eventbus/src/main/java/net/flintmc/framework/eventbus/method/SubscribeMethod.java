package net.flintmc.framework.eventbus.method;

import net.flintmc.framework.eventbus.EventBus;
import net.flintmc.framework.eventbus.event.Event;
import net.flintmc.framework.eventbus.event.EventPriority;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;

/**
 * A subscribed method in an {@link EventBus}.
 *
 * @see Subscribe
 */
public interface SubscribeMethod {

  /**
   * Retrieves the class of the event to which this method is subscribed.
   *
   * @return The non-null class of the event to which this method is subscribed
   */
  Class<? extends Event> getEventClass();

  /**
   * Retrieves the priority of the method. The lower the value, the earlier the event will be
   * called.
   *
   * @return The method priority.
   * @see EventPriority
   */
  byte getPriority();

  /**
   * Retrieves the phase of the subscribed method.
   *
   * @return The phase of teh subscribed method.
   */
  Subscribe.Phase getPhase();

  /**
   * Invokes this event subscriber. Called by the bus when a new event is fired to this subscriber.
   *
   * @param event The event that was fired.
   * @throws Throwable Any exception thrown during handling
   */
  void invoke(Event event, Subscribe.Phase phase) throws Throwable;

  /** A factory class for {@link SubscribeMethod}. */
  @AssistedFactory(SubscribeMethod.class)
  interface Factory {

    /**
     * Creates a new {@link SubscribeMethod} with the given parameters.
     *
     * @param eventClass The non-null class of the event to which this method is subscribed
     * @param priority The priority of the subscribed method.
     * @param phase The phase of the subscribed method.
     * @param executor The non-null supplier for the event executor.
     * @return A created subscribed method.
     */
    SubscribeMethod create(
        @Assisted Class<? extends Event> eventClass,
        @Assisted byte priority,
        @Assisted Subscribe.Phase phase,
        @Assisted EventExecutor<?> executor);
  }
}
