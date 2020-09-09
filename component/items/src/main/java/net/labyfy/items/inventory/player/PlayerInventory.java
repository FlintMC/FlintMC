package net.labyfy.items.inventory.player;

import net.labyfy.items.ItemStack;
import net.labyfy.items.inventory.Inventory;

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

}
