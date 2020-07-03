package net.labyfy.internal.component.inject;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.inject.Key;
import com.google.inject.name.Names;
import net.labyfy.component.inject.InjectedInvocationHelper;
import net.labyfy.component.inject.event.Event;
import net.labyfy.component.stereotype.identifier.Identifier;
import net.labyfy.component.stereotype.service.Service;
import net.labyfy.component.stereotype.service.ServiceHandler;
import org.apache.groovy.util.Maps;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Service for sending events to receivers.
 *
 * @deprecated The event system will change and this class will be removed
 */
@Singleton
@Service(value = Event.class, priority = -10000)
@Deprecated
public class EventService implements ServiceHandler {
  private final InjectedInvocationHelper injectedInvocationHelper;
  private final Multimap<Class<?>, Method> methods = HashMultimap.create();

  @Inject
  private EventService(InjectedInvocationHelper injectedInvocationHelper) {
    this.injectedInvocationHelper = injectedInvocationHelper;
  }

  /**
   * {@inheritDoc}
   */
  public void discover(Identifier.Base property) {
    this.methods.put(
        property.getProperty().getLocatedIdentifiedAnnotation().<Event>getAnnotation().value(),
        (property.getProperty().getLocatedIdentifiedAnnotation().getLocation()));
  }

  /**
   * Sends an event to all receivers which have subscribed the given event.
   *
   * @param event The event to send
   */
  public void broadcast(Object event) {
    this.broadcast(event, Collections.emptyMap());
  }

  /**
   * Sends an event with parameters to all receivers which have subscribed the event.
   *
   * @param event The event to send
   * @param customParameters The parameter bindings to make available to the event receivers
   */
  public void broadcast(Object event, Map<Key<?>, Object> customParameters) {
    Map<Key<?>, Object> parameters =
        new HashMap<>(
            Maps.of(
                Key.get(event.getClass()),
                event,
                Key.get(Object.class, Names.named("event")),
                event));
    parameters.putAll(customParameters);
    this.methods
        .get(event.getClass())
        .forEach(method -> injectedInvocationHelper.invokeMethod(method, parameters));
  }
}