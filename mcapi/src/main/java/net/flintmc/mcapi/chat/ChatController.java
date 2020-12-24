package net.flintmc.mcapi.chat;

import java.util.List;
import net.flintmc.mcapi.chat.component.ChatComponent;

/**
 * This class manages the whole chat, it allows the usage of multiple chat windows and filters for
 * the chat.
 */
public interface ChatController {

  /**
   * Sends the given message to the server just as it would be done when the player types the text
   * into the chat. Depending on the minecraft version, the length of the message is limited to
   * {@link #getChatInputLimit()}. If the given message is longer than that, everything after the
   * limit will be removed before sending it to the server. If the client would send a message
   * longer than the limit, the client would get kicked.
   *
   * @param message The non-null message to be sent to the server
   * @return Whether the message was sent successfully or cancelled by an event handler
   */
  boolean dispatchChatInput(String message);

  /**
   * The history of the chat input by the player. This will be filled when the player types
   * something into the chat or by using {@link #dispatchChatInput(String)}. The player can scroll
   * through this history by using the arrow keys.
   * <p>
   * The same message cannot be contained multiple times at the last index, so if one message is
   * typed multiple times without another message between, the sent message will only be contained
   * once in the list.
   *
   * @return The modifiable non-null list containing every message the player has sent.
   */
  List<String> getInputHistory();

  /**
   * Retrieves the maximum length of the message for {@link #dispatchChatInput(String)} depending on
   * the minecraft version. Before minecraft 1.11 the max length was set to 100, since 1.11 it is
   * 256.
   *
   * @return The limit of chat input depending on the minecraft version
   */
  int getChatInputLimit();

  /**
   * Displays the message into the chat. Before it is displayed, all registered filters will be
   * applied.
   *
   * @param location  The non-null location to display the message at
   * @param component The non-null component to be displayed
   */
  void displayChatMessage(ChatLocation location, ChatComponent component);

  /**
   * Retrieves a list of all messages that are displayed in this chat. The size of this list will
   * never be greater than {@link #getMaxMessages()}.
   *
   * @return A non-null and unmodifiable list with all messages
   */
  List<ChatComponent> getReceivedMessages();

  /**
   * Retrieves the max messages that can be stored in this chat.
   *
   * @return An int which is always larger than 0
   */
  int getMaxMessages();

}
