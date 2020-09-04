package net.labyfy.chat.component.event.content;

import net.labyfy.chat.component.ChatComponent;
import net.labyfy.chat.component.event.HoverEvent;

import java.util.UUID;

/**
 * The content of a {@link HoverEvent}.
 */
public abstract class HoverContent {

  /**
   * Creates a new content for a {@link HoverEvent} which displays a text.
   *
   * @param component The non-null component for the content
   * @return The new non-null content to be used in a {@link HoverEvent}
   */
  public static HoverContent text(ChatComponent component) {
    return new HoverText(component);
  }

  /**
   * Creates a new content for a {@link HoverEvent} which displays an entity.
   *
   * @param uniqueId The non-null uniqueId of the entity
   * @param type     The non-null type of the entity
   * @return The new non-null content to be used in a {@link HoverEvent}
   */
  public static HoverContent entity(UUID uniqueId, String type) {
    return entity(uniqueId, type, null);
  }

  /**
   * Creates a new content for a {@link HoverEvent} which displays an entity.
   *
   * @param uniqueId    The non-null uniqueId of the entity
   * @param type        The non-null type of the entity
   * @param displayName The display name of the entity or {@code null} if the entity doesn't have a specific display
   *                    name
   * @return The new non-null content to be used in a {@link HoverEvent}
   */
  public static HoverContent entity(UUID uniqueId, String type, ChatComponent displayName) {
    return new HoverEntity(uniqueId, type, displayName);
  }

  /**
   * Creates a new content for a {@link HoverEvent} which displays an item with the count 1 and no NBT tag.
   *
   * @param id The non-null id of the item
   * @return The new non-null content to be used in a {@link HoverEvent}
   */
  public static HoverContent item(String id) {
    return item(id, 1);
  }

  /**
   * Creates a new content for a {@link HoverEvent} which displays an item without an NBT tag.
   *
   * @param id    The non-null id of the item
   * @param count The amount of items on this stack
   * @return The new non-null content to be used in a {@link HoverEvent}
   */
  public static HoverContent item(String id, int count) {
    return item(id, count, null);
  }

  /**
   * Creates a new content for a {@link HoverEvent} which displays an item.
   *
   * @param id    The non-null id of the item
   * @param count The amount of items on this stack
   * @param nbt   The NBT tag of the item or {@code null} if the item doesn't have an NBT tag
   * @return The new non-null content to be used in a {@link HoverEvent}
   */
  public static HoverContent item(String id, int count, String nbt) {
    return new HoverItem(id, count, nbt);
  }

  /**
   * Retrieves the action which is used to identify the type of this content.
   *
   * @return The non-null action of this content
   */
  public abstract HoverEvent.Action getAction();

}
