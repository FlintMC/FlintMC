package net.labyfy.items.inventory;

import net.labyfy.component.stereotype.NameSpacedKey;
import net.labyfy.items.inventory.player.PlayerInventory;

public interface InventoryController {

  Inventory getOpenInventory();

  PlayerInventory getPlayerInventory();

  InventoryType[] getTypes();

  InventoryType getType(NameSpacedKey registryName);

  void registerType(InventoryType type);

  void showInventory(Inventory inventory);

  boolean canOpenInventories();

  /**
   * Sends a click in this inventory in the given slot to the server.
   *
   * @param click The non-null type of click to be sent to the server
   * @param slot  The slot to be clicked or -1 to simulate a click outside of the inventory
   */
  void performClick(InventoryClick click, int slot);

  /**
   * Sends a hotkey press in this inventory in the given slot to the server to move a specific item between two slots.
   *
   * @param hotkey The hotkey from 0 - 8 to be simulated
   * @param slot   The target slot to simulate the hotkey press
   * @throws IndexOutOfBoundsException If the slot is either smaller than 0, or greater or equal to the highest possible
   *                                   slot in this inventory
   */
  void performHotkeyPress(int hotkey, int slot) throws IndexOutOfBoundsException;

}
