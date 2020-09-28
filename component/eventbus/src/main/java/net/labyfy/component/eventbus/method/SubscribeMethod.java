package net.labyfy.component.eventbus.method;

import net.labyfy.component.eventbus.event.Subscribe;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collection;

/**
 * A subscribed method in an {@link net.labyfy.component.eventbus.EventBus}.
 */
public class SubscribeMethod {

  private final byte priority;
  private final Subscribe.Phase phase;
  private final Object instance;
  private final Executor executor;
  private final Method eventMethod;
  private final Collection<Annotation> groupAnnotations;

  /**
   * Constructs a new subscribed method.
   *
   * @param priority         The priority of the subscribed method.
   * @param phase            The phase of the subscribed method.
   * @param instance         The owner of the event method.
   * @param executor         The event executor.
   * @param eventMethod      The subscribed method.
   * @param groupAnnotations Extra annotations for the subscribed method.
   */
  public SubscribeMethod(
      byte priority,
      Subscribe.Phase phase,
      Object instance, Executor executor,
      Method eventMethod,
      Collection<Annotation> groupAnnotations
  ) {
    this.priority = priority;
    this.phase = phase;
    this.instance = instance;
    this.executor = executor;
    this.eventMethod = eventMethod;
    this.groupAnnotations = groupAnnotations;
  }

  /**
   * Retrieves the priority of the method. The lower the value, the earlier the event will be called.
   *
   * @return The method priority.
   * @see net.labyfy.component.eventbus.event.util.EventPriority
   */
  public byte getPriority() {
    return this.priority;
  }

  /**
   * Retrieves the event method.
   *
   * @return The event method.
   */
  public Method getEventMethod() {
    return this.eventMethod;
  }

  /**
   * Retrieves the phase of the subscribed method.
   *
   * @return The phase of teh subscribed method.
   */
  public Subscribe.Phase getPhase() {
    return this.phase;
  }

  /**
   * Retrieves an group annotation of the subscribed method.
   *
   * @return The non-null group annotations of the subscribed method or an empty collection if the method isn't
   * annotated with any annotation that is annotated with the {@link net.labyfy.component.eventbus.event.filter.EventGroup}
   * annotation.
   */
  public Collection<Annotation> getGroupAnnotations() {
    return this.groupAnnotations;
  }

  /**
   * Invokes this event subscriber. Called by the bus when a new event is fired to this subscriber.
   *
   * @param event The event that was fired.
   * @throws Throwable Any exception thrown during handling
   */
  public void invoke(Object event) throws Throwable {
    this.executor.invoke(this.instance, event);
  }

}
