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
  private DefaultChatReceiveEvent(@Assisted("message") ChatComponent message) {
    super(Type.RECEIVE);
    this.message = message;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ChatComponent getMessage() {
    return this.message;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setMessage(ChatComponent message) {
    this.message = message;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isCancelled() {
    return this.cancelled;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setCancelled(boolean cancelled) {
    this.cancelled = cancelled;
  }
}
