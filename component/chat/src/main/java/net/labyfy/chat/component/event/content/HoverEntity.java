package net.labyfy.chat.component.event.content;

import net.labyfy.chat.component.ChatComponent;
import net.labyfy.chat.component.event.HoverEvent;

import java.util.UUID;

/**
 * The content of a {@link HoverEvent} which displays an entity.
 */
public class HoverEntity extends HoverContent {

  private final UUID uniqueId;
  private final String type;
  private final ChatComponent displayName;

  /**
   * Creates a new content for a {@link HoverEvent} which displays an entity.
   *
   * @param uniqueId    The non-null uniqueId of the entity
   * @param type        The non-null type of the entity
   * @param displayName The display name of the entity or {@code null} if the entity doesn't have a specific display
   *                    name
   */
  public HoverEntity(UUID uniqueId, String type, ChatComponent displayName) {
    this.uniqueId = uniqueId;
    this.type = type;
    this.displayName = displayName;
  }

  /**
   * Retrieves the non-null type of this entity which is used when displaying the entity.
   *
   * @return The non-null type of this entity
   */
  public String getType() {
    return this.type;
  }

  /**
   * Retrieves the non-null uniqueId of this entity which is used when displaying the entity.
   *
   * @return The non-null uniqueId of this entity
   */
  public UUID getUniqueId() {
    return this.uniqueId;
  }

  /**
   * Retrieves the display name of this entity which is used when displaying the entity.
   *
   * @return The display name of this entity or {@code null} if no display name has been set
   */
  public ChatComponent getDisplayName() {
    return this.displayName;
  }

  @Override
  public HoverEvent.Action getAction() {
    return HoverEvent.Action.SHOW_ENTITY;
  }
}
