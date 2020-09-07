package net.labyfy.items.inventory;

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

  public static InventoryDimension rect(int width, int height) {
    return new InventoryDimension(width, height, width * height);
  }

  public static InventoryDimension other(int slotCount) {
    return new InventoryDimension(-1, -1, slotCount);
  }

  /**
   * Retrieves the width of this inventory measured in slots or -1 if this inventory is no rectangle.
   *
   * @return The width of this inventory measured in slots
   */
  public int getWidth() {
    return this.width;
  }

  /**
   * Retrieves the height of this inventory measured in slots or -1 if this inventory is no rectangle.
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
   * Retrieves whether this dimension represents a rectangle or not. For example chests and hoppers are rectangles,
   * anvils are not.
   *
   * @return Whether this dimension is a rectangle or not
   */
  public boolean isRectangle() {
    return this.width != -1 && this.height != -1;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    InventoryDimension that = (InventoryDimension) o;
    return width == that.width &&
        height == that.height &&
        slotCount == that.slotCount;
  }

  @Override
  public int hashCode() {
    return Objects.hash(width, height, slotCount);
  }
}
