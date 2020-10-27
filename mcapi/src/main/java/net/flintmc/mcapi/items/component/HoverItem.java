package net.flintmc.mcapi.items.component;

import net.flintmc.mcapi.chat.component.event.HoverEvent;
import net.flintmc.mcapi.chat.component.event.content.HoverContent;
import net.flintmc.mcapi.items.ItemStack;

/** The content of a {@link HoverEvent} which displays an item. */
public class HoverItem extends HoverContent {

  private final ItemStack itemStack;

  /**
   * Creates a new content for a {@link HoverEvent} which displays an item.
   *
   * @param itemStack The non-null item to be displayed
   */
  public HoverItem(ItemStack itemStack) {
    this.itemStack = itemStack;
  }

  /**
   * Retrieves the item that will be used when displaying the item.
   *
   * @return The non-null item
   */
  public ItemStack getItemStack() {
    return this.itemStack;
  }

  @Override
  public HoverEvent.Action getAction() {
    return HoverEvent.Action.SHOW_ITEM;
  }
}
