package net.labyfy.component.eventbus.event.filter;

import net.labyfy.component.eventbus.method.SubscribeMethod;

public interface EventFilter {

  boolean matches(Object event, SubscribeMethod method);

}
