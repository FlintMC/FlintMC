package net.flintmc.mcapi.internal.event;

import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.event.IngameMenuCloseEvent;

@Implement(IngameMenuCloseEvent.class)
public class DefaultIngameMenuCloseEvent implements IngameMenuCloseEvent {
  @AssistedInject
  public DefaultIngameMenuCloseEvent() {}
}
