/*
 * FlintMC
 * Copyright (C) 2020-2021 LabyMedia GmbH and contributors
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package net.flintmc.mcapi.chat.controller.filter;

import java.util.function.BiConsumer;
import net.flintmc.mcapi.chat.controller.ChatController;

/**
 * Implementation of the {@link ChatFilter} which uses a {@link BiConsumer} for easier usage.
 */
public class SimpleChatFilter extends BasicChatFilter {

  private final BiConsumer<ChatController, FilterableChatMessage> consumer;

  private SimpleChatFilter(BiConsumer<ChatController, FilterableChatMessage> consumer) {
    this.consumer = consumer;
  }

  /**
   * Creates a new {@link ChatFilter} with a random {@code uniqueId} and the given {@code consumer}
   * which will be applied every time this filter receives a new message.
   *
   * @param consumer The non-null consumer for filtering the messages
   * @return The new non-null chat filter
   */
  public static SimpleChatFilter create(
      BiConsumer<ChatController, FilterableChatMessage> consumer) {
    return new SimpleChatFilter(consumer);
  }

  @Override
  public void apply(ChatController controller, FilterableChatMessage message) {
    this.consumer.accept(controller, message);
  }
}
