package net.labyfy.chat.event;

import com.google.inject.assistedinject.Assisted;
import net.labyfy.chat.component.ChatComponent;
import net.labyfy.component.eventbus.event.Cancellable;
import net.labyfy.component.inject.assisted.AssistedFactory;

/**
 * This event will be fired whenever a message will be displayed in the chat, it supports both PRE and POST {@link
 * net.labyfy.component.eventbus.event.subscribe.Subscribe.Phase}s but the cancellation will be ignored in the POST phase. If this
 * event has been cancelled in the PRE phase, the message won't be displayed anymore in the chat.
 */
public interface ChatReceiveEvent extends ChatMessageEvent, Cancellable {

  /**
   * Retrieves the message that will be displayed in the client.
   *
   * @return The non-null component
   */
  ChatComponent getMessage();

  /**
   * Changes the message that will be displayed by the client. This has no effect in the POST phase.
   *
   * @param message The new non-null message to be displayed
   */
  void setMessage(ChatComponent message);

  /**
   * Factory for the {@link ChatReceiveEvent}.
   */
  @AssistedFactory(ChatReceiveEvent.class)
  interface Factory {

    /**
     * Creates a new {@link ChatReceiveEvent} with the given message.
     *
     * @param message The non-null message for the new event
     * @return The new non-null event with the given message
     */
    ChatReceiveEvent create(@Assisted("message") ChatComponent message);

  }


}
