package net.labyfy.component.eventbus.method;

import net.labyfy.component.eventbus.event.Subscribe;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * A subscribed method in an {@link net.labyfy.component.eventbus.EventBus}.
 */
public class SubscribeMethod {

  private final byte priority;
  private final Subscribe.Phase phase;
  private final Object instance;
  private final Executor executor;
  private final Method eventMethod;
  private final Annotation groupAnnotation;

  /**
   * Constructs a new subscribed method.
   *
   * @param priority        The priority of the subscribed method.
   * @param phase           The phase of the subscribed method.
   * @param instance        The owner of the event method.
   * @param executor        The event executor.
   * @param eventMethod     The subscribed method.
   * @param groupAnnotation Extra annotations for the subscribed method.
   */
  public SubscribeMethod(
      byte priority,
      Subscribe.Phase phase,
      Object instance, Executor executor,
      Method eventMethod,
      Annotation groupAnnotation
  ) {
    this.priority = priority;
    this.phase = phase;
    this.instance = instance;
    this.executor = executor;
    this.eventMethod = eventMethod;
    this.groupAnnotation = groupAnnotation;
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
   * @return The group annotation of the subscribed method or {@code null} if the method isn't annotated with an
   * annotation that is annotated with the {@link net.labyfy.component.eventbus.event.filter.EventGroup} annotation.
   */
  public Annotation getGroupAnnotation() {
    return this.groupAnnotation;
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
