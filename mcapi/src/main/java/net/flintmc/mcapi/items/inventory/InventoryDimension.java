package net.flintmc.mcapi.items.inventory;

import com.google.common.base.Preconditions;
import java.util.Objects;

public class InventoryDimension {

  private final int width;
  private final int height;
  private final int slotCount;

  private InventoryDimension(int width, int height, int slotCount) {
    this.width = width;
    this.height = height;
    this.slotCount = slotCount;
  }

  /**
   * Creates a new inventory dimension that represents a rectangle inventory like chests and
   * dispensers.
   *
   * @param width  The width of the inventory in slots
   * @param height The height of the inventory in slots
   * @return The new non-null dimension with the given width and height and width * height and the
   * number of slots
   * @throws IllegalArgumentException If the width is zero or lower
   * @throws IllegalArgumentException If the height is zero or lower
   */
  public static InventoryDimension rect(int width, int height) {
    Preconditions.checkArgument(width > 0, "Width has to be greater than zero");
    Preconditions.checkArgument(height > 0, "Height has to be greater than zero");

    return new InventoryDimension(width, height, width * height);
  }

  /**
   * Creates a new inventory dimension that represents any inventory like merchants and enchantment
   * tables.
   *
   * @param slotCount The number of slots in the inventory
   * @return The new non-null dimension with the given slot count and -1 as the width and height
   * @throws IllegalArgumentException If the given slotCount is zero or lower
   */
  public static InventoryDimension other(int slotCount) {
    Preconditions.checkArgument(slotCount > 0, "SlotCount has to be greater than zero");

    return new InventoryDimension(-1, -1, slotCount);
  }

  /**
   * Retrieves the width of this inventory measured in slots or -1 if this inventory is no
   * rectangle.
   *
   * @return The width of this inventory measured in slots
   */
  public int getWidth() {
    return this.width;
  }

  /**
   * Retrieves the height of this inventory measured in slots or -1 if this inventory is no
   * rectangle.
   *
   * @return The height of this inventory measured in slots
   */
  public int getHeight() {
    return this.height;
  }

  /**
   * Retrieves the number of all slots in this inventory ignoring the contents of the slots.
   *
   * @return The number of slots in this inventory
   */
  public int getSlotCount() {
    return this.slotCount;
  }

  /**
   * Retrieves whether this dimension represents a rectangle or not. For example chests and hoppers
   * are rectangles, anvils are not.
   *
   * @return Whether this dimension is a rectangle or not
   */
  public boolean isRectangle() {
    return this.width != -1 && this.height != -1;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    InventoryDimension that = (InventoryDimension) o;
    return width == that.width && height == that.height && slotCount == that.slotCount;
  }

  @Override
  public int hashCode() {
    return Objects.hash(width, height, slotCount);
  }
}
