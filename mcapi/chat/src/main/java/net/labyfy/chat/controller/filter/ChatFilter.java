package net.labyfy.chat.controller.filter;

import net.labyfy.chat.controller.ChatController;

import java.util.UUID;

/**
 * Filter for messages in the {@link net.labyfy.chat.controller.Chat}.
 */
public interface ChatFilter {

  /**
   * Retrieves the non-null unique id of this filter.
   *
   * @return The non-null unique id of this filter
   */
  UUID getUniqueId();

  /**
   * Modifies the given message by using this filter
   *
   * @param controller The non-null controller where the given message will be displayed after the filtering
   * @param message    The non-null message to be filtered
   */
  void apply(ChatController controller, FilterableChatMessage message);
}
