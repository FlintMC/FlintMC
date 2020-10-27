package net.flintmc.mcapi.items.inventory;

import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.items.ItemStack;
import net.flintmc.mcapi.items.type.ItemType;

/** Represents an inventory in Minecraft which can contain items. */
public interface Inventory {

  /**
   * Retrieves the window id of this inventory, which is defined by the server. WindowId 1 is the
   * player inventory.
   *
   * @return The id of this inventory
   */
  int getWindowId();

  /**
   * Retrieves the type of this inventory.
   *
   * @return The non-null type of this inventory
   */
  InventoryType getType();

  /**
   * Retrieves the unmodifiable title of this inventory which will be displayed at the top.
   *
   * @return The non-null title of this inventory
   */
  ChatComponent getTitle();

  /**
   * Retrieves the dimension of this inventory, which contains the size of the inventory.
   *
   * @return the size of this inventory.
   */
  InventoryDimension getDimension();

  /**
   * Retrieves every item in this inventory ordered by their slot. Modifications to this array will
   * have no effect.
   *
   * @return The non-null array of non-null contents in this inventory
   */
  ItemStack[] getContents();

  /**
   * Sets the contents of this inventory.
   *
   * @param contents The non-null array of contents for this inventory, {@code null} can be used to
   *     represent air
   * @throws IllegalArgumentException If the array of contents is too large for this inventory
   */
  void setContents(ItemStack[] contents) throws IllegalArgumentException;

  /**
   * Retrieves an item in a specific slot in this inventory.
   *
   * @param slot The slot to get the item from
   * @return The non-null item out of this slot
   * @throws IndexOutOfBoundsException If the slot is either smaller than 0, or greater or equal to
   *     the highest possible slot in this inventory
   */
  ItemStack getItem(int slot) throws IndexOutOfBoundsException;

  /**
   * Sets the given stack of items in the slot.
   *
   * @param slot The slot to which the stack of items to be set
   * @param item The stack of items to set
   * @throws IndexOutOfBoundsException If the slot is either smaller than 0, or greater or equal to
   *     the highest possible slot in this inventory.
   */
  void setItem(int slot, ItemStack item) throws IndexOutOfBoundsException;

  /**
   * Counts all items in this inventory matching the given type. If a stack has multiple items on
   * it, every single one will count.
   *
   * @param type The type to count the items for
   * @return The number of items in this inventory matching the given type.
   */
  int countItems(ItemType type);

  /**
   * Finds the slots of all items in this inventory matching the given type.
   *
   * @param type The type to find the items for
   * @return A non-null array containing all slots that contain an item with the given type in this
   *     inventory
   */
  int[] findSlots(ItemType type);
}
