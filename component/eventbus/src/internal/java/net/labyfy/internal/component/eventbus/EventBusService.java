package net.labyfy.internal.component.eventbus;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import net.labyfy.component.eventbus.EventBus;
import net.labyfy.component.eventbus.event.Subscribe;
import net.labyfy.component.eventbus.event.filter.EventFilter;
import net.labyfy.component.eventbus.event.filter.EventGroup;
import net.labyfy.component.eventbus.method.Executor;
import net.labyfy.component.eventbus.method.SubscribeMethod;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.stereotype.identifier.Identifier;
import net.labyfy.component.stereotype.identifier.LocatedIdentifiedAnnotation;
import net.labyfy.component.stereotype.service.Service;
import net.labyfy.component.stereotype.service.ServiceHandler;
import net.labyfy.internal.component.eventbus.exception.ExecutorGenerationException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Service for sending events to receivers.
 */
@Singleton
@Service(value = Subscribe.class, priority = -10000)
@Implement(EventBus.class)
public class EventBusService implements ServiceHandler, EventBus {

  private final Multimap<Class<?>, SubscribeMethod> subscribeMethods;
  private final EventFilter eventFilter;
  private final AtomicReference<Injector> injectorReference;
  private final Executor.Factory factory;

  @Inject
  public EventBusService(EventFilter eventFilter, @Named("injectorReference") AtomicReference injectorReference, Executor.Factory executorFactory) {
    this.eventFilter = eventFilter;
    this.injectorReference = injectorReference;
    this.subscribeMethods = HashMultimap.create();
    this.factory = executorFactory;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void discover(Identifier.Base property) {

    LocatedIdentifiedAnnotation locatedIdentifiedAnnotation = property.getProperty().getLocatedIdentifiedAnnotation();
    Subscribe subscribe = locatedIdentifiedAnnotation.getAnnotation();
    Method method = locatedIdentifiedAnnotation.getLocation();

    if (method.getParameterCount() != 1) {
      throw new IllegalArgumentException("Method " + method.getName() + " in " + method.getDeclaringClass().getName() + " doesn't have exactly one parameter");
    }

    Class<?> eventClass = method.getParameterTypes()[0];
    Collection<Annotation> groupAnnotations = new ArrayList<>();

    for (Annotation annotation : method.getDeclaredAnnotations()) {
      Class<? extends Annotation> type = annotation.annotationType();
      if (type.isAnnotationPresent(EventGroup.class) &&
          type.getAnnotation(EventGroup.class).groupEvent().isAssignableFrom(eventClass)) {
        groupAnnotations.add(annotation);
      }
    }

    Object instance = this.injectorReference.get().getInstance(method.getDeclaringClass());

    Executor executor;

    try {
      executor = this.factory.create(instance, method);
    } catch (Throwable throwable) {
      throw new ExecutorGenerationException("Encountered an exception while creating an event subscriber for method \"" + method + "\"!", throwable);
    }
    // Initializes a new subscribe method
    SubscribeMethod subscribeMethod = new SubscribeMethod(
        subscribe.priority(),
        subscribe.phase(),
        instance,
        executor,
        method,
        groupAnnotations
    );

    this.subscribeMethods.put(eventClass, subscribeMethod);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <E> E fireEvent(E event, Subscribe.Phase phase) {
    if (event == null) throw new NullPointerException("An error is occurred because the event is null");

    this.postEvent(event, phase);
    return event;
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

    do {
      this.copyMethods(currentClass, methods);
      for (Class<?> implemented : eventClass.getInterfaces()) {
        this.copyMethods(implemented, methods);
      }
    } while ((currentClass = currentClass.getSuperclass()) != Object.class);

    methods.sort(Comparator.comparingInt(SubscribeMethod::getPriority));

    return methods;
  }

  private void copyMethods(Class<?> eventClass, List<SubscribeMethod> targetMethods) {
    for (SubscribeMethod subscribeMethod : this.subscribeMethods.get(eventClass)) {
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
   * @param <E>   The event type.
   */
  private <E> void postEvent(E event, Subscribe.Phase phase) {
    List<SubscribeMethod> methods = this.findMethods(event.getClass());
    if (methods.isEmpty()) {
      return;
    }

    for (SubscribeMethod method : methods) {
      if ((method.getPhase() == Subscribe.Phase.ANY || phase == method.getPhase()) &&
          this.eventFilter.matches(event, method)) {

        this.fireLast(event, method);

      }
    }
  }

  /**
   * Invokes the subscribed method.
   *
   * @param event  The fired event.
   * @param method The subscribed method.
   * @param <E>    The event type.
   */
  private <E> void fireLast(E event, SubscribeMethod method) {
    try {
      method.invoke(event);
    } catch (Throwable throwable) {
      throwable.printStackTrace();
    }
  }

}
