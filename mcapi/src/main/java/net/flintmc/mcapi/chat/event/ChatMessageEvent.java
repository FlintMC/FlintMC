package net.flintmc.mcapi.chat.event;

import net.flintmc.framework.eventbus.event.Event;

/** The base event for sending/receiving messages in the chat. */
public interface ChatMessageEvent extends Event {

  /**
   * Retrieves the type of this chat message event
   *
   * @return The non-null type of this event
   */
  Type getType();

  /** Types for chat message events. */
  enum Type {

    /** The type when sending a message from the client to the server. */
    SEND,

    /** The type when receiving a message from the server in the client. */
    RECEIVE
  }
}
