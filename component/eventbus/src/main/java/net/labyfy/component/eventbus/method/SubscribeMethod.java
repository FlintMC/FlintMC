package net.labyfy.component.eventbus.method;

import com.google.inject.assistedinject.Assisted;
import net.labyfy.component.eventbus.EventBus;
import net.labyfy.component.eventbus.event.Subscribe;
import net.labyfy.component.eventbus.event.filter.EventGroup;
import net.labyfy.component.eventbus.event.util.EventPriority;
import net.labyfy.component.inject.assisted.AssistedFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * A subscribed method in an {@link EventBus}.
 */
public interface SubscribeMethod {

  /**
   * Retrieves the priority of the method. The lower the value, the earlier the event will be called.
   *
   * @return The method priority.
   * @see EventPriority
   */
  byte getPriority();

  /**
   * Retrieves the event method.
   *
   * @return The event method.
   */
  Method getEventMethod();

  /**
   * Retrieves the phase of the subscribed method.
   *
   * @return The phase of teh subscribed method.
   */
  Subscribe.Phase getPhase();

  /**
   * Retrieves an group annotation of the subscribed method.
   *
   * @return The group annotation of the subscribed method or {@code null} if the method isn't annotated with an
   * annotation that is annotated with the {@link EventGroup} annotation.
   */
  Annotation getGroupAnnotation();

  /**
   * Invokes this event subscriber. Called by the bus when a new event is fired to this subscriber.
   *
   * @param event The event that was fired.
   * @throws Throwable Any exception thrown during handling
   */
  void invoke(Object event) throws Throwable;

  /**
   * A factory class for {@link SubscribeMethod}.
   */
  @AssistedFactory(SubscribeMethod.class)
  interface Factory {

    /**
     * Creates a new {@link SubscribeMethod} with the given parameters.
     *
     * @param priority        The priority of the subscribed method.
     * @param phase           The phase of the subscribed method.
     * @param instance        The owner of the event method.
     * @param executor        The event executor.
     * @param eventMethod     The subscribed method.
     * @param groupAnnotation An extra annotation for the subscribed method.
     * @return A created subscribed method.
     */
    SubscribeMethod create(
            @Assisted("priority") byte priority,
            @Assisted("phase") Subscribe.Phase phase,
            @Assisted("instance") Object instance,
            @Assisted("executor") Executor executor,
            @Assisted("eventMethod") Method eventMethod,
            @Assisted("groupAnnotation") Annotation groupAnnotation
    );

  }
}
