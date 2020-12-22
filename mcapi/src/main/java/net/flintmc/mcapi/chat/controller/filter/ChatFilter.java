package net.flintmc.mcapi.chat.controller.filter;

import java.util.UUID;
import net.flintmc.mcapi.chat.controller.Chat;
import net.flintmc.mcapi.chat.controller.ChatController;

/**
 * Filter for messages in the {@link Chat}.
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
   * @param controller The non-null controller where the given message will be displayed after the
   *                   filtering
   * @param message    The non-null message to be filtered
   */
  void apply(ChatController controller, FilterableChatMessage message);
}
