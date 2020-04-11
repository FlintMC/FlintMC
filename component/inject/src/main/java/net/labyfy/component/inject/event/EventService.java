package net.labyfy.component.inject.event;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.inject.Key;
import com.google.inject.name.Names;
import net.labyfy.base.structure.identifier.Identifier;
import net.labyfy.base.structure.service.Service;
import net.labyfy.base.structure.service.ServiceHandler;
import net.labyfy.component.inject.invoke.InjectedInvocationHelper;
import org.apache.groovy.util.Maps;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Singleton
@Service(value = Event.class, priority = -10000)
public class EventService implements ServiceHandler {

  private final InjectedInvocationHelper injectedInvocationHelper;
  private final Multimap<Class<?>, Method> methods = HashMultimap.create();

  @Inject
  private EventService(InjectedInvocationHelper injectedInvocationHelper) {
    this.injectedInvocationHelper = injectedInvocationHelper;
  }

  public void discover(Identifier.Base property) {
    this.methods.put(
        property.getProperty().getLocatedIdentifiedAnnotation().<Event>getAnnotation().value(),
        (property.getProperty().getLocatedIdentifiedAnnotation().getLocation()));
  }

  public void broadcast(Object event) {
    this.broadcast(event, Collections.emptyMap());
  }

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
