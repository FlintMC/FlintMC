package net.labyfy.component.eventbus.method;

import net.labyfy.component.eventbus.event.Subscribe;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * Represents a subscribed method.
 */
public class SubscribeMethod {

  private final boolean asynchronously;
  private final byte priority;
  private final Subscribe.Phase phase;
  private final Object instance;
  private final Method eventMethod;
  private final Annotation extraAnnotation;

  public SubscribeMethod(boolean asynchronously, byte priority, Subscribe.Phase phase, Object instance, Method eventMethod, Annotation extraAnnotation) {
    this.asynchronously = asynchronously;
    this.priority = priority;
    this.phase = phase;
    this.instance = instance;
    this.eventMethod = eventMethod;
    this.extraAnnotation = extraAnnotation;
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
   * Retrieves the priority of the method.
   *
   * @return The method priority.
   */
  public byte getPriority() {
    return this.priority;
  }

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

  public Subscribe.Phase getPhase() {
    return this.phase;
  }

  public Annotation getExtraAnnotation() {
    return this.extraAnnotation;
  }

  /**
   * Retrieves the first parameter type of the event method.
   *
   * @return The first parameter type of the event method.
   */
  public Class<?> getEventClass() {
    return this.getEventMethod().getParameterTypes()[0];
  }
}
