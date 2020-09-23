package net.labyfy.component.eventbus.method;

import com.google.inject.assistedinject.Assisted;
import net.labyfy.component.eventbus.event.Subscribe;
import net.labyfy.component.inject.assisted.AssistedFactory;

import java.lang.reflect.Method;

/**
 * Represents a subscribed method.
 */
public class SubscribeMethod {

  private final boolean asynchronously;
  private final byte priority;
  private final Subscribe.Phase phase;
  private final Method eventMethod;

  public SubscribeMethod(boolean asynchronously, byte priority, Subscribe.Phase phase, Method eventMethod) {
    this.asynchronously = asynchronously;
    this.priority = priority;
    this.phase = phase;
    this.eventMethod = eventMethod;
  }

  /**
   * Whether the subscribed method asynchronously.
   *
   * @return {@code true} if the subscribed method asynchronous, otherwise {@code false}.
   */
  public boolean asynchronously() {
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

  /**
   * Retrieves the event method.
   *
   * @return The event method.
   */
  public Method getEventMethod() {
    return this.eventMethod;
  }

  public Subscribe.Phase getPhase() {
    return phase;
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
