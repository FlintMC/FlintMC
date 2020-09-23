package net.labyfy.internal.component.eventbus.module;

import com.google.inject.name.Named;
import net.labyfy.component.eventbus.EventGroupModule;
import net.labyfy.component.eventbus.event.EventGroup;
import net.labyfy.component.eventbus.event.Subscribe;
import net.labyfy.component.eventbus.event.client.TickEvent;
import net.labyfy.component.inject.implement.Implement;

import java.lang.reflect.Method;

/**
 *
 */
@Implement(EventGroupModule.class)
public class TestEventGroupModule implements EventGroupModule {

  @Override
  @EventGroup(module = TestEventGroupModule.class, groupEvent = TickEvent.class)
  public boolean handleAnnotation(@Named("method") Method method, @Named("event") Object event) {
    if (event instanceof TickEvent) {
      TickEvent tickEvent = (TickEvent) event;

      if (method.isAnnotationPresent(TickEvent.TickPhase.class)) {
        TickEvent.TickPhase tick = method.getAnnotation(TickEvent.TickPhase.class);
        return tick.type() == tickEvent.getType();
      }

    }
    return true;
  }
}
