package net.labyfy.internal.component.inject;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.inject.Key;
import com.google.inject.name.Names;
import javassist.CtMethod;
import net.labyfy.component.inject.InjectedInvocationHelper;
import net.labyfy.component.inject.event.Event;
import net.labyfy.component.stereotype.identifier.IdentifierMeta;
import net.labyfy.component.stereotype.service.CtResolver;
import net.labyfy.component.stereotype.service.Service;
import net.labyfy.component.stereotype.service.ServiceHandler;
import net.labyfy.component.stereotype.service.ServiceNotFoundException;
import org.apache.groovy.util.Maps;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.lang.reflect.InvocationTargetException;
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
public class EventService implements ServiceHandler<Event> {
  private final InjectedInvocationHelper injectedInvocationHelper;
  private final Multimap<Class<?>, CtMethod> methods = HashMultimap.create();

  @Inject
  private EventService(InjectedInvocationHelper injectedInvocationHelper) {
    this.injectedInvocationHelper = injectedInvocationHelper;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void discover(IdentifierMeta<Event> identifierMeta) throws ServiceNotFoundException {
    this.methods.put(identifierMeta.getAnnotation().value(), identifierMeta.getTarget());
  }

  /**
   * Sends an event to all receivers which have subscribed the given event.
   *
   * @param event The event to send
   * @throws InvocationTargetException If the invoked method threw an exception.
   * @throws IllegalAccessException    If the method definition could not be accessed.
   */
  public void broadcast(Object event) throws InvocationTargetException, IllegalAccessException {
    this.broadcast(event, Collections.emptyMap());
  }

  /**
   * Sends an event with parameters to all receivers which have subscribed the event.
   *
   * @param event The event to send
   * @param customParameters The parameter bindings to make available to the event receivers
   * @throws InvocationTargetException If the invoked method threw an exception.
   * @throws IllegalAccessException    If the method definition could not be accessed.
   */
  public void broadcast(Object event, Map<Key<?>, Object> customParameters) throws InvocationTargetException, IllegalAccessException {
    Map<Key<?>, Object> parameters =
        new HashMap<>(
            Maps.of(
                Key.get(event.getClass()),
                event,
                Key.get(Object.class, Names.named("event")),
                event));
    parameters.putAll(customParameters);

    for (CtMethod method : methods.get(event.getClass())) {
      injectedInvocationHelper.invokeMethod(CtResolver.get(method), parameters);
    }
  }
}
