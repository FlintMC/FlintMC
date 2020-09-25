package net.labyfy.chat.event;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.labyfy.component.inject.implement.Implement;

@Implement(ChatSendEvent.class)
public class DefaultChatSendEvent extends DefaultChatMessageEvent implements ChatSendEvent {

  private String message;
  private boolean cancelled;

  @AssistedInject
  public DefaultChatSendEvent(@Assisted("message") String message) {
    super(Type.SEND);
    this.message = message;
  }

  @Override
  public String getMessage() {
    return this.message;
  }

  @Override
  public void setMessage(String message) {
    this.message = message;
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
