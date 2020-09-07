package net.labyfy.items.inventory;

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
  MOVE

}
