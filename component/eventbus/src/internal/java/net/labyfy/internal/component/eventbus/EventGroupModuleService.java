package net.labyfy.internal.component.eventbus;


import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.inject.Inject;
import com.google.inject.Key;
import com.google.inject.Singleton;
import com.google.inject.name.Names;
import net.labyfy.component.eventbus.event.EventGroup;
import net.labyfy.component.eventbus.event.Subscribe;
import net.labyfy.component.inject.InjectedInvocationHelper;
import net.labyfy.component.stereotype.identifier.Identifier;
import net.labyfy.component.stereotype.identifier.LocatedIdentifiedAnnotation;
import net.labyfy.component.stereotype.service.Service;
import net.labyfy.component.stereotype.service.ServiceHandler;
import net.labyfy.component.stereotype.service.ServiceNotFoundException;
import org.apache.groovy.util.Maps;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Singleton
@Service(value = EventGroup.class, priority = -9999)
public class EventGroupModuleService implements ServiceHandler {

  private InjectedInvocationHelper injectedInvocationHelper;
  private Multimap<Class<?>, Method> registry;

  @Inject
  public EventGroupModuleService(InjectedInvocationHelper injectedInvocationHelper) {
    this.injectedInvocationHelper = injectedInvocationHelper;
    this.registry = HashMultimap.create();
  }

  @Override
  public void discover(Identifier.Base property) throws ServiceNotFoundException {
    LocatedIdentifiedAnnotation locatedIdentifiedAnnotation = property.getProperty().getLocatedIdentifiedAnnotation();
    EventGroup eventGroup = locatedIdentifiedAnnotation.getAnnotation();

    System.out.println(eventGroup.groupEvent());
    this.registry.put(
            eventGroup.groupEvent(),
            locatedIdentifiedAnnotation.getLocation()
    );
  }

  public boolean handleAnnotation(Method method, Object event) {
    Map<Key<?>, Object> parameters = new HashMap<>(
            Maps.of(
                    Key.get(method.getClass()),
                    method,
                    Key.get(Method.class, Names.named("method")),
                    method,
                    Key.get(event.getClass()),
                    event,
                    Key.get(Object.class, Names.named("event")),
                    event
            )
    );

    for (Method handleAnnotation : this.registry.get(event.getClass().getEnclosingClass())) {
      try {
        return this.injectedInvocationHelper.invokeMethod(handleAnnotation, parameters);
      } catch (InvocationTargetException | IllegalAccessException e) {
      return false;
      }
    }

    return true;
  }


}
