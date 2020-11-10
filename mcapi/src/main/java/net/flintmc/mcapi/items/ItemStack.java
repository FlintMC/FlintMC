package net.flintmc.mcapi.items;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.items.meta.ItemMeta;
import net.flintmc.mcapi.items.type.ItemType;

/** Represents a stack of items in an inventory. */
public interface ItemStack {

  /**
   * Retrieves whether this ItemStack already has some metadata like a display name, lore, etc.
   *
   * @return Whether this ItemStack has an {@link ItemMeta} or not
   */
  boolean hasItemMeta();

  /**
   * Retrieves the meta of this ItemStack and creates it if this stack doesn't have a meta and
   * allowed.
   *
   * @param create Whether this method is allowed to create the ItemMeta if it is not present
   * @return The ItemMeta of this ItemStack or {@code null}, if no ItemMeta is present and {@code
   *     create} has been set to {@code false}. This will never be {@code null} if {@code create}
   *     was set to {@code true}.
   * @see #hasItemMeta()
   */
  ItemMeta getItemMeta(boolean create);

  /**
   * Retrieves the meta of this ItemStack and creates it if this stack doesn't have a meta.
   *
   * @return The non-null meta of this item
   */
  ItemMeta getItemMeta();

  /**
   * Gets the amount of items on this stack.
   *
   * @return The amount of items on this stack
   */
  int getStackSize();

  /**
   * Gets the type of the items in this stack.
   *
   * @return The non-null type of the items in this stack
   */
  ItemType getType();

  /** Factory for the {@link ItemStack}. */
  @AssistedFactory(ItemStack.class)
  interface Factory {

    /**
     * Creates a new item stack with the given type and stack size.
     *
     * @param type The non-null type of the items on the new stack
     * @param stackSize The amount of items on the new stack
     * @return The new non-null ItemStack
     */
    ItemStack createItemStack(
        @Assisted("type") ItemType type, @Assisted("stackSize") int stackSize);

    /**
     * Creates a new item stack with the given type, stack size and metadata.
     *
     * @param type The non-null type of the items on the new stack
     * @param stackSize The amount of items on the new stack
     * @param meta The non-null metadata for the items on the new stack
     * @return The new non-null ItemStack
     */
    ItemStack createItemStack(
        @Assisted("type") ItemType type,
        @Assisted("stackSize") int stackSize,
        @Assisted("meta") ItemMeta meta);
  }
}
