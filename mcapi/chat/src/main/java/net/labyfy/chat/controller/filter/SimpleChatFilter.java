package net.labyfy.chat.controller.filter;

import net.labyfy.chat.controller.ChatController;

import java.util.function.BiConsumer;

/**
 * Implementation of the {@link ChatFilter} which uses a {@link BiConsumer} for easier usage.
 */
public class SimpleChatFilter extends BasicChatFilter {

  private final BiConsumer<ChatController, FilterableChatMessage> consumer;

  private SimpleChatFilter(BiConsumer<ChatController, FilterableChatMessage> consumer) {
    this.consumer = consumer;
  }

  /**
   * Creates a new {@link ChatFilter} with a random {@code uniqueId} and the given {@code consumer} which will be
   * applied every time this filter receives a new message.
   *
   * @param consumer The non-null consumer for filtering the messages
   * @return The new non-null chat filter
   */
  public static SimpleChatFilter create(BiConsumer<ChatController, FilterableChatMessage> consumer) {
    return new SimpleChatFilter(consumer);
  }

  @Override
  public void apply(ChatController controller, FilterableChatMessage message) {
    this.consumer.accept(controller, message);
  }
}
