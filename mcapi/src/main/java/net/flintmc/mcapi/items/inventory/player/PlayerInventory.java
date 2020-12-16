package net.flintmc.mcapi.items.inventory.player;

import net.flintmc.mcapi.items.ItemStack;
import net.flintmc.mcapi.items.inventory.Inventory;

public interface PlayerInventory extends Inventory {

  /**
   * Retrieves a part of the armor of this inventory.
   *
   * @param part The non-null part to get the armor from
   * @return The non-null item stack in the given slot
   */
  ItemStack getArmorPart(PlayerArmorPart part);

  /**
   * Retrieves an item from the player's hand.
   *
   * @param hand The hand to get the item from
   * @return The non-null item stack in the given hand
   */
  ItemStack getItemInHand(PlayerHand hand);

  /**
   * Retrieves an item in a specific slot in this inventory.
   *
   * @param slot The slot to get the item from. Slots from 36 to 44 are hotbar slots. Slots from 6
   *             to 35 are the main inventory. Slots from 0 to 4 are the crafting inventory.
   * @return The non-null item out of this slot
   * @throws IndexOutOfBoundsException If the slot is either smaller than 0, or greater or equal to
   *                                   the highest possible slot in this inventory
   */
  @Override
  ItemStack getItem(int slot) throws IndexOutOfBoundsException;

  /**
   * Retrieves the slot from the player's hand.
   *
   * @param hand The hand to get the slot from
   * @return The slot from the given hand
   */
  int getHandSlot(PlayerHand hand);

  /**
   * Whether the player has this hand.
   *
   * @param hand The hand to check
   * @return {@code true} when the player has the given hand, otherwise {@code false}
   */
  boolean hasHand(PlayerHand hand);

  /**
   * Retrieves the item that is currently held on the cursor by the player.
   *
   * @return The item on the cursor or {@code null} if the player has no item on the cursor
   */
  ItemStack getCursor();

  /**
   * Closes the currently opened inventory in the client. If no inventory is opened, this method
   * does nothing.
   */
  void closeInventory();
}
