package net.labyfy.items.inventory;

import com.google.common.primitives.Ints;
import net.labyfy.items.ItemRegistry;
import net.labyfy.items.ItemStack;
import net.labyfy.items.type.ItemType;

import java.util.ArrayList;
import java.util.Collection;

public abstract class DefaultInventory implements Inventory {

  protected final ItemRegistry registry;
  private final int windowId;
  private final InventoryType type;
  private final InventoryDimension dimension;

  public DefaultInventory(ItemRegistry registry, int windowId, InventoryType type, InventoryDimension dimension) {
    this.registry = registry;
    this.windowId = windowId;
    this.type = type;
    this.dimension = dimension;
  }

  @Override
  public int getWindowId() {
    return this.windowId;
  }

  @Override
  public InventoryType getType() {
    return this.type;
  }

  @Override
  public InventoryDimension getDimension() {
    return this.dimension;
  }

  protected void validateSlot(int slot) throws IndexOutOfBoundsException {
    if (slot < 0 || slot >= this.dimension.getSlotCount()) {
      throw new IndexOutOfBoundsException("Slot " + slot + " is out of bounds for " + this.dimension.getSlotCount() + " available slots");
    }
  }

  @Override
  public int countItems(ItemType type) {
    int count = 0;
    for (ItemStack content : this.getContents()) {
      if (content != null && content.getType().equals(type)) {
        count += content.getStackSize();
      }
    }

    return count;
  }

  @Override
  public int[] findSlots(ItemType type) {
    Collection<Integer> slots = new ArrayList<>();

    ItemStack[] contents = this.getContents();
    for (int i = 0; i < contents.length; i++) {
      ItemStack content = contents[i];
      if (content != null && content.getType().equals(type)) {
        slots.add(i);
      }
    }

    return Ints.toArray(slots);
  }

}
