package net.labyfy.component.items.inventory;

/**
 * An enumeration of all inventory clicks.
 */
public enum InventoryClick {

  /**
   * Drop all items on the selected slot.
   * <p>
   * The default keybind is Ctrl + Q (Command + Q on Mac).
   */
  DROP_ALL,

  /**
   * Drop one item on the selected slot.
   * <p>
   * The default keybind is Q.
   */
  DROP,

  /**
   * Clone the items on a specific slot in the creative mode.
   * <p>
   * The default keybind is Middle Mouse Button.
   */
  CLONE,

  /**
   * Pickup all items on a specific slot.
   * <p>
   * The default keybind is Left Mouse Button.
   */
  PICKUP_ALL,

  /**
   * Pickup half of the items on a specific slot.
   * <p>
   * The default keybind is Right Mouse Button.
   */
  PICKUP_HALF,

  /**
   * Move all items on a specific slot into another inventory.
   * <p>
   * The default keybind is Left and Right Mouse Button.
   */
  MOVE,

  /**
   * Collect one stack (or less, depending on the contents of the inventory) of a specific item in an inventory.
   * <p>
   * The default keybind is Double Left Mouse Button.
   */
  MERGE_ALL

}
