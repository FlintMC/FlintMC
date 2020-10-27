package net.flintmc.mcapi.chat.controller.filter;

import java.util.UUID;

/**
 * Implementation of the {@link ChatFilter} which already creates the {@code uniqueId} for this filter.
 */
public abstract class BasicChatFilter implements ChatFilter {

  private final UUID uniqueId = UUID.randomUUID();

  @Override
  public UUID getUniqueId() {
    return this.uniqueId;
  }
}
