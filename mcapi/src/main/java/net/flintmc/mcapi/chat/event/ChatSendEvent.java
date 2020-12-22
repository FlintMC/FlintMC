package net.flintmc.mcapi.chat.event;

import net.flintmc.framework.eventbus.event.Cancellable;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;

/**
 * This event will be fired whenever a message will be sent to the server (e.g. by typing it in the
 * chat box), it supports both PRE and POST {@link Subscribe.Phase}s but the cancellation will be
 * ignored in the POST phase. If this event has been cancelled in the PRE phase, the message won't
 * be sent to the server anymore.
 */
public interface ChatSendEvent extends ChatMessageEvent, Cancellable {

  /**
   * Retrieves the message that will be sent to the server.
   *
   * @return The non-null message for the server
   */
  String getMessage();

  /**
   * Changes the message that will be sent to the server after this event. This has no effect in the
   * POST phase.
   *
   * @param message The new non-null message for the server
   */
  void setMessage(String message);

  /**
   * Factory for the {@link ChatSendEvent}.
   */
  @AssistedFactory(ChatSendEvent.class)
  interface Factory {

    /**
     * Creates a new {@link ChatSendEvent} with the given message.
     *
     * @param message The non-null message for the new event
     * @return The new non-null event with the given message
     */
    ChatSendEvent create(@Assisted("message") String message);
  }
}
