package net.labyfy.internal.component.eventbus;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labyfy.component.eventbus.EventBus;
import net.labyfy.component.eventbus.event.Event;
import net.labyfy.component.eventbus.event.subscribe.Subscribe;
import net.labyfy.component.eventbus.method.SubscribeMethod;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.inject.logging.InjectLogger;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/** Default implementation of the {@link EventBus}. */
@Singleton
@Implement(EventBus.class)
public class DefaultEventBus implements EventBus {

  private final Logger logger;
  private final Multimap<String, SubscribeMethod> subscribeMethods;

  @Inject
  private DefaultEventBus(@InjectLogger Logger logger) {
    this.logger = logger;
    this.subscribeMethods = HashMultimap.create();
  }

  /** {@inheritDoc} */
  @Override
  public <E> E fireEvent(E event, Subscribe.Phase phase) {
    if (event == null)
      throw new NullPointerException("An error is occurred because the event is null");

    this.postEvent(event, phase);
    return event;
  }

  /** {@inheritDoc} */
  @Override
  public Multimap<String, SubscribeMethod> getSubscribeMethods() {
    return this.subscribeMethods;
  }

  /**
   * Finds all subscribed method that listen to the given event class.
   *
   * @param eventClass The event class to be searched
   * @return A collection with all subscribed method that listen to the given class.
   */
  private List<SubscribeMethod> findMethods(Class<?> eventClass) {
    List<SubscribeMethod> methods = new ArrayList<>();
    Class<?> currentClass = eventClass;

    while (Event.class.isAssignableFrom(currentClass)) {
      this.searchInterfaces(currentClass, methods);
      currentClass = currentClass.getSuperclass();
    }

    methods.sort(Comparator.comparingInt(SubscribeMethod::getPriority));

    return methods;
  }

  private void searchInterfaces(Class<?> interfaceClass, List<SubscribeMethod> targetMethods) {
    if (Event.class.isAssignableFrom(interfaceClass)) {
      this.copyMethods(interfaceClass, targetMethods);

      for (Class<?> implemented : interfaceClass.getInterfaces()) {
        this.searchInterfaces(implemented, targetMethods);
      }
    }
  }

  private void copyMethods(Class<?> eventClass, List<SubscribeMethod> targetMethods) {
    for (SubscribeMethod subscribeMethod : this.subscribeMethods.get(eventClass.getName())) {
      if (!targetMethods.contains(subscribeMethod)) {
        targetMethods.add(subscribeMethod);
      }
    }
  }

  /**
   * Fires the given event.
   *
   * @param event The event to fire.
   * @param phase The phase when the event is fired.
   * @param <E> The event type.
   */
  private <E> void postEvent(E event, Subscribe.Phase phase) {
    List<SubscribeMethod> methods = this.findMethods(event.getClass());
    if (methods.isEmpty()) {
      return;
    }

    for (SubscribeMethod method : methods) {
      if (method.getPhase() == Subscribe.Phase.ANY || phase == method.getPhase()) {
        this.fireLast(event, method);
      }
    }
  }

  /**
   * Invokes the subscribed method.
   *
   * @param event The fired event.
   * @param method The subscribed method.
   * @param <E> The event type.
   */
  private <E> void fireLast(E event, SubscribeMethod method) {
    try {
      method.invoke(event);
    } catch (Throwable throwable) {
      this.logger.error(
          "Error while posting event "
              + event.getClass().getName()
              + " to method "
              + method.getEventMethod(),
          throwable);
    }
  }
}
