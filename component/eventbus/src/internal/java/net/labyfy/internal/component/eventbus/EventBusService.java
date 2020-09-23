package net.labyfy.internal.component.eventbus;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.inject.Inject;
import com.google.inject.Key;
import com.google.inject.Singleton;
import com.google.inject.name.Names;
import net.labyfy.component.eventbus.EventBus;
import net.labyfy.component.eventbus.event.Subscribe;
import net.labyfy.component.eventbus.method.SubscribeMethod;
import net.labyfy.component.inject.InjectedInvocationHelper;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.stereotype.identifier.Identifier;
import net.labyfy.component.stereotype.identifier.LocatedIdentifiedAnnotation;
import net.labyfy.component.stereotype.service.Service;
import net.labyfy.component.stereotype.service.ServiceHandler;
import net.labyfy.component.stereotype.service.ServiceNotFoundException;
import org.apache.groovy.util.Maps;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Singleton
@Service(value = Subscribe.class, priority = -10000)
@Implement(EventBus.class)
public class EventBusService implements ServiceHandler, EventBus {

  private final InjectedInvocationHelper injectedInvocationHelper;
  private final Multimap<Class<?>, SubscribeMethod> registry;
  private final ExecutorService executorService;
  private final EventGroupModuleService eventGroupModuleService;

  @Inject
  public EventBusService(InjectedInvocationHelper injectedInvocationHelper, EventGroupModuleService eventGroupModuleService) {
    this.injectedInvocationHelper = injectedInvocationHelper;
    this.eventGroupModuleService = eventGroupModuleService;
    this.registry = HashMultimap.create();
    this.executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
  }

  @Override
  public void discover(Identifier.Base property) throws ServiceNotFoundException {

    LocatedIdentifiedAnnotation locatedIdentifiedAnnotation = property.getProperty().getLocatedIdentifiedAnnotation();
    Subscribe subscribe = locatedIdentifiedAnnotation.getAnnotation();

    SubscribeMethod subscribeMethod = new SubscribeMethod(
            subscribe.async(),
            subscribe.priority(),
            subscribe.phase(),
            locatedIdentifiedAnnotation.getLocation()
    );

    this.registry.put(
            subscribeMethod.getEventClass(),
            subscribeMethod
    );
  }

  @Override
  public <E> CompletableFuture<E> fire(E event, Subscribe.Phase phase) {
    if (event == null) throw new NullPointerException("An error is occurred because the event is null");
    CompletableFuture<E> eventFuture = new CompletableFuture<>();

    this.fireEvent(event, phase, eventFuture);

    return eventFuture;
  }

  @Override
  public <E> CompletableFuture<E> fire(E event, Subscribe.Phase phase, Map<Key<?>, Object> customParameters) {
    if (event == null) throw new NullPointerException("An error is occurred because the event is null");
    CompletableFuture<E> eventFuture = new CompletableFuture<>();

    this.fireEvent(event, phase, eventFuture, customParameters);

    return eventFuture;
  }


  private <E> void fireEvent(E event, Subscribe.Phase phase, CompletableFuture<E> eventFuture) {
    this.fireEvent(event, phase, eventFuture, Collections.emptyMap());
  }


  private <E> void fireEvent(E event, Subscribe.Phase phase, CompletableFuture<E> eventFuture, Map<Key<?>, Object> customParameters) {
    if (event == null) throw new NullPointerException("An error is occurred because the event is null");

    Map<Key<?>, Object> parameters = new HashMap<>(
            Maps.of(
                    Key.get(event.getClass()),
                    event,
                    Key.get(Object.class, Names.named("event")),
                    event
            )
    );
    parameters.putAll(customParameters);

    List<SubscribeMethod> sortedList = new ArrayList<>(this.registry.get(event.getClass()));
    sortedList.sort((o1, o2) -> o2.getPriority() - o1.getPriority());

    // TODO: 23.09.2020 Optimize..
    for (SubscribeMethod subscribeMethod : sortedList) {
      if(phase == subscribeMethod.getPhase()) {
        if (this.handleAnnotationLogic(subscribeMethod.getEventMethod(), event))
          if (subscribeMethod.asynchronously()) {
            this.executorService.execute(() -> {
              eventFuture.complete(event);
              try {
                this.injectedInvocationHelper.invokeMethod(subscribeMethod.getEventMethod(), parameters);
              } catch (InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
              }
            });
          } else {
            eventFuture.complete(event);
            try {
              this.injectedInvocationHelper.invokeMethod(subscribeMethod.getEventMethod(), parameters);
            } catch (InvocationTargetException | IllegalAccessException e) {
              e.printStackTrace();
            }
          }
      }
    }
  }

  private boolean handleAnnotationLogic(Method method, Object event) {
    return this.eventGroupModuleService.handleAnnotation(method, event);
  }

}
