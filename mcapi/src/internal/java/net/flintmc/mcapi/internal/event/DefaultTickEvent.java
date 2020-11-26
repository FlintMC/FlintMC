package net.flintmc.mcapi.internal.event;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.event.TickEvent;

@Implement(TickEvent.class)
public class DefaultTickEvent implements TickEvent {

  private final Type type;

  @AssistedInject
  public DefaultTickEvent(@Assisted("type") Type type) {
    this.type = type;
  }

  @Override
  public Type getType() {
    return this.type;
  }
}
