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
import net.labyfy.component.eventbus.method.SubscribeMethod;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.stereotype.identifier.Identifier;
import net.labyfy.component.stereotype.identifier.LocatedIdentifiedAnnotation;
import net.labyfy.component.stereotype.service.Service;
import net.labyfy.component.stereotype.service.ServiceHandler;
import net.labyfy.component.stereotype.service.ServiceNotFoundException;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Service for sending events to receivers.
 */
@Singleton
@Service(value = Subscribe.class, priority = -10000)
@Implement(EventBus.class)
public class EventBusService implements ServiceHandler, EventBus {

  private final Multimap<Class<?>, SubscribeMethod> subscribeMethods;
  private final ExecutorService executorService;
  private final EventFilter eventFilter;
  private final AtomicReference<Injector> injectorReference;

  @Inject
  public EventBusService(EventFilter eventFilter, @Named("injectorReference") AtomicReference injectorReference) {
    this.eventFilter = eventFilter;
    this.injectorReference = injectorReference;
    this.subscribeMethods = HashMultimap.create();
    this.executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void discover(Identifier.Base property) throws ServiceNotFoundException {

    LocatedIdentifiedAnnotation locatedIdentifiedAnnotation = property.getProperty().getLocatedIdentifiedAnnotation();
    Subscribe subscribe = locatedIdentifiedAnnotation.getAnnotation();
    Method method = locatedIdentifiedAnnotation.getLocation();
    Annotation extraAnnotation = null;

    for (Annotation annotation : method.getDeclaredAnnotations()) {
      if (annotation.annotationType().isAnnotationPresent(EventGroup.class)) {
        extraAnnotation = annotation;
        break;
      }
    }

    if (method.getParameterCount() != 1) {
      throw new IllegalArgumentException("Method " + method.getName() + " in " + method.getDeclaringClass().getName() + " doesn't have exactly one parameter");
    }

    // Initializes a new subscribe method
    SubscribeMethod subscribeMethod = new SubscribeMethod(
            subscribe.async(),
            subscribe.priority(),
            subscribe.phase(),
            this.injectorReference.get().getInstance(method.getDeclaringClass()),
            method,
            extraAnnotation
    );

    this.subscribeMethods.put(subscribeMethod.getEventClass(), subscribeMethod);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <E> CompletableFuture<E> fire(E event, Subscribe.Phase phase) {
    if (event == null) throw new NullPointerException("An error is occurred because the event is null");
    CompletableFuture<E> eventFuture = new CompletableFuture<>();

    this.fireEvent(event, phase, eventFuture);

    return eventFuture;
  }

  /**
   * Finds all subscribed method that listen to the given event class.
   *
   * @param eventClass The event class to be searched
   * @return A collection with all subscribed method that listen to the given class.
   */
  private List<SubscribeMethod> findMethods(Class<?> eventClass) {
    // TODO optimize?

    List<SubscribeMethod> methods = new ArrayList<>();
    Class<?> currentClass = eventClass;

    do {
      methods.addAll(this.subscribeMethods.get(currentClass));
    } while ((currentClass = currentClass.getSuperclass()) != Object.class);

    methods.sort(Comparator.comparingInt(SubscribeMethod::getPriority));

    return methods;
  }

  /**
   * Fires the given event.
   *
   * @param event       The event to fire.
   * @param phase       The phase when the event is fired.
   * @param eventFuture The {@link CompletableFuture} that represents the fired event.
   * @param <E>         The event type.
   */
  private <E> void fireEvent(E event, Subscribe.Phase phase, CompletableFuture<E> eventFuture) {
    if (event == null) throw new NullPointerException("An error is occurred because the event is null");

    List<SubscribeMethod> methods = this.findMethods(event.getClass());

    for (SubscribeMethod method : methods) {
      if ((method.getPhase() == Subscribe.Phase.ANY || phase == method.getPhase()) && this.eventFilter.matches(event, method)) {

        if (method.isAsynchronously()) {
          this.executorService.execute(() -> this.fireLast(event, method, eventFuture));
        } else {
          this.fireLast(event, method, eventFuture);
        }

      }
    }
  }

  /**
   * Invokes the subscribed method.
   *
   * @param event       The fired event.
   * @param method      The subscribed method.
   * @param eventFuture The {@link CompletableFuture} that represents the fired event.
   * @param <E>         The event type.
   */
  private <E> void fireLast(E event, SubscribeMethod method, CompletableFuture<E> eventFuture) {
    try {
      method.getEventMethod().invoke(method.getInstance(), event);
    } catch (InvocationTargetException | IllegalAccessException e) {
      e.printStackTrace();
    }
    eventFuture.complete(event);
  }

}
