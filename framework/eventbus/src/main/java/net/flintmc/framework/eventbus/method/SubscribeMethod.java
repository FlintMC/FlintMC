package net.flintmc.framework.eventbus.method;

import com.google.inject.assistedinject.Assisted;
import javassist.CtClass;
import javassist.CtMethod;
import net.flintmc.framework.eventbus.EventBus;
import net.flintmc.framework.eventbus.event.EventPriority;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.framework.inject.assisted.AssistedFactory;

import java.util.function.Supplier;

/** A subscribed method in an {@link EventBus}. */
public interface SubscribeMethod {

  /**
   * Retrieves the priority of the method. The lower the value, the earlier the event will be
   * called.
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
  CtMethod getEventMethod();

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
  void invoke(Object event) throws Throwable;

  /** A factory class for {@link SubscribeMethod}. */
  @AssistedFactory(SubscribeMethod.class)
  interface Factory {

    /**
     * Creates a new {@link SubscribeMethod} with the given parameters.
     *
     * @param priority The priority of the subscribed method.
     * @param phase The phase of the subscribed method.
     * @param ctClass The owner of the event method.
     * @param executorSupplier Supplier for the event executor.
     * @param eventMethod The subscribed method.
     * @return A created subscribed method.
     */
    SubscribeMethod create(
        @Assisted("priority") byte priority,
        @Assisted("phase") Subscribe.Phase phase,
        @Assisted("declaringClass") CtClass ctClass,
        @Assisted("executorSupplier") Supplier<Executor> executorSupplier,
        @Assisted("eventMethod") CtMethod eventMethod);
  }
}
