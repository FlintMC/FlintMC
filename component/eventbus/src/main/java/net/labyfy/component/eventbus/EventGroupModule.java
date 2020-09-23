package net.labyfy.component.eventbus;

import net.labyfy.component.eventbus.event.Subscribe;

import java.lang.reflect.Method;

public interface EventGroupModule {

  boolean handleAnnotation(Method method, Object event);

}
