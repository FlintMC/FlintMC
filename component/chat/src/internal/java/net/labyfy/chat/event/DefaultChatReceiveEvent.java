package net.labyfy.chat.event;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.labyfy.chat.component.ChatComponent;
import net.labyfy.component.inject.implement.Implement;

@Implement(ChatReceiveEvent.class)
public class DefaultChatReceiveEvent extends DefaultChatMessageEvent implements ChatReceiveEvent {

  private ChatComponent message;
  private boolean cancelled;

  @AssistedInject
  public DefaultChatReceiveEvent(@Assisted("message") ChatComponent message) {
    super(Type.RECEIVE);
    this.message = message;
  }

  @Override
  public ChatComponent getMessage() {
    return this.message;
  }

  @Override
  public void setMessage(ChatComponent message) {
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
