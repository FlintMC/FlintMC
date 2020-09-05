package net.labyfy.chat.component.event.content;

import net.labyfy.chat.component.event.HoverEvent;

/**
 * The content of a {@link HoverEvent} which displays an item.
 */
public class HoverItem extends HoverContent {

  private final String id;
  private final int count;
  private final String nbt; // TODO: replace with Labyfy NBT Tags

  /**
   * Creates a new content for a {@link HoverEvent} which displays an item.
   *
   * @param id    The non-null id of the item
   * @param count The amount of items on this stack
   * @param nbt   The NBT tag of the item or {@code null} if the item doesn't have an NBT tag
   */
  public HoverItem(String id, int count, String nbt) {
    this.id = id;
    this.count = count;
    this.nbt = nbt;
  }

  /**
   * Retrieves the non-null id of this item which is used when displaying the item.
   *
   * @return The non-null id of this item
   */
  public String getId() {
    return this.id;
  }

  /**
   * Retrieves the count of this item which is used when displaying the item.
   *
   * @return The non-null count of this item
   */
  public int getCount() {
    return this.count;
  }

  /**
   * Retrieves the NBT tag of this item which is used when displaying the item.
   *
   * @return The NBT tag of this item or {@code null} if no NBT tag has been set
   */
  public String getNbt() {
    return this.nbt;
  }

  @Override
  public HoverEvent.Action getAction() {
    return HoverEvent.Action.SHOW_ITEM;
  }
}
