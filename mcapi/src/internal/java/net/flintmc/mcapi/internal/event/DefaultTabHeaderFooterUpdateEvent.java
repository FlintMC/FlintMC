package net.flintmc.mcapi.internal.event;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.event.TabHeaderFooterUpdateEvent;

import javax.annotation.Nullable;

@Implement(TabHeaderFooterUpdateEvent.class)
public class DefaultTabHeaderFooterUpdateEvent implements TabHeaderFooterUpdateEvent {

  private final ChatComponent newValue;
  private final Type type;
  private boolean cancelled;

  @AssistedInject
  private DefaultTabHeaderFooterUpdateEvent(
      @Assisted("new") @Nullable ChatComponent newValue, @Assisted Type type) {
    this.newValue = newValue;
    this.type = type;
  }

  @Override
  public ChatComponent getNewValue() {
    return this.newValue;
  }

  @Override
  public Type getType() {
    return this.type;
  }

  @Override
  public boolean isCancelled() {
    return this.cancelled;
  }

  @Override
  public void setCancelled(boolean cancelled) {
    this.cancelled = cancelled;
  }
}
