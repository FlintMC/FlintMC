package net.labyfy.component.eventbus.method;

import net.labyfy.component.eventbus.event.Subscribe;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * A subscribed method in an {@link net.labyfy.component.eventbus.EventBus}.
 */
public class SubscribeMethod {

  private final boolean asynchronously;
  private final byte priority;
  private final Subscribe.Phase phase;
  private final Object instance;
  private final Method eventMethod;
  private final Class<?> eventClass;
  private final Annotation groupAnnotation;

  /**
   * Constructs a new subscribed method.
   *
   * @param asynchronously  Whether the subscribed method is fired asynchronously.
   * @param priority        The priority of the subscribed method.
   * @param phase           The phase of the subscribed method.
   * @param instance        The owner of the event method.
   * @param eventMethod     The subscribed method.
   * @param eventClass      The class of the event which is subscribed by this method
   * @param groupAnnotation Extra annotations for the subscribed method.
   */
  public SubscribeMethod(
      boolean asynchronously,
      byte priority,
      Subscribe.Phase phase,
      Object instance,
      Method eventMethod,
      Class<?> eventClass,
      Annotation groupAnnotation
  ) {
    this.asynchronously = asynchronously;
    this.priority = priority;
    this.phase = phase;
    this.instance = instance;
    this.eventMethod = eventMethod;
    this.eventClass = eventClass;
    this.groupAnnotation = groupAnnotation;
  }

  /**
   * Whether the subscribed method asynchronously.
   *
   * @return {@code true} if the subscribed method asynchronous, otherwise {@code false}.
   */
  public boolean isAsynchronously() {
    return this.asynchronously;
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
   * Retrieves the owner of the subscribed method.
   *
   * @return The owner of the subscribed method.
   */
  public Object getInstance() {
    return this.instance;
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
   * Retrieves the first parameter type of the event method.
   *
   * @return The first parameter type of the event method.
   */
  public Class<?> getEventClass() {
    return this.eventClass;
  }
}
