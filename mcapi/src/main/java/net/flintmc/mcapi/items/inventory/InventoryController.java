package net.flintmc.mcapi.items.inventory;

import net.flintmc.framework.stereotype.NameSpacedKey;
import net.flintmc.mcapi.items.inventory.player.PlayerInventory;

/** Represents a controller for inventories. */
public interface InventoryController {

  /**
   * Retrieves the opened inventory.
   *
   * @return the opened inventory.
   */
  Inventory getOpenInventory();

  /**
   * Retrieves the player's inventory.
   *
   * @return the player's inventory.
   */
  PlayerInventory getPlayerInventory();

  /**
   * Retrieves whether the player is currently looking at an inventory. The type of the inventory
   * doesn't matter.
   *
   * @return {@code true} if there is an inventory opened, otherwise {@code false}
   */
  boolean hasInventoryOpened();

  /**
   * Retrieves an array of all registered inventory types.
   *
   * @return an array of all registered inventory types.
   */
  InventoryType[] getTypes();

  /**
   * Retrieves the inventory type by the given registry name.
   *
   * @param registryName The registry name of an inventory type
   * @return an inventory with the registry name or {@code null}
   */
  InventoryType getType(NameSpacedKey registryName);

  /**
   * Registers an inventory type to the controller.
   *
   * @param type The inventory type to register
   */
  void registerType(InventoryType type);

  /**
   * Whether inventories can be opened.
   *
   * @return {@code true} if inventories can be opened, otherwise {@code false}
   */
  boolean canOpenInventories();

  /**
   * Sends a click in this inventory in the given slot to the server.
   *
   * @param click The non-null type of click to be sent to the server
   * @param slot The slot to be clicked or -1 to simulate a click outside of the inventory
   * @throws IllegalArgumentException If currently no inventory is opened to be clicked
   */
  void performClick(InventoryClick click, int slot);

  /**
   * Sends a hotkey press in this inventory in the given slot to the server to move a specific item
   * between two slots.
   *
   * @param hotkey The hotkey from 0 - 8 to be pressed
   * @param slot The target slot to simulate the hotkey press
   * @throws IndexOutOfBoundsException If the slot is either smaller than 0, or greater or equal to
   *     the highest possible slot in this inventory
   * @throws IllegalArgumentException If the given hotkey is not in the range from 0 - 8
   * @throws IllegalArgumentException If currently no inventory is opened to be clicked
   */
  void performHotkeyPress(int hotkey, int slot) throws IndexOutOfBoundsException;
}
