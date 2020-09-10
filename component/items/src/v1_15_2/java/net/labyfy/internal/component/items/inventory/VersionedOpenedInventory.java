package net.labyfy.internal.component.items.inventory;

import net.labyfy.chat.component.ChatComponent;
import net.labyfy.component.items.ItemRegistry;
import net.labyfy.component.items.ItemStack;
import net.labyfy.component.items.inventory.InventoryDimension;
import net.labyfy.component.items.inventory.InventoryType;
import net.labyfy.component.items.mapper.MinecraftItemMapper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.IContainerListener;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.NonNullList;

import java.util.List;

public class VersionedOpenedInventory extends VersionedInventory {

  private ItemStack[] contents;

  public VersionedOpenedInventory(ItemRegistry registry, InventoryType type, InventoryDimension dimension, MinecraftItemMapper mapper, Container container, ChatComponent title) {
    super(registry, container.windowId, type, dimension, mapper, () -> container, title);
    container.addListener(new ContainerListener());
  }

  @Override
  public ItemStack[] getContents() {
    if (this.contents != null) {
      return this.contents;
    }

    List<Slot> slots = this.getContainer().inventorySlots;
    // the size of the slots list doesn't match the size of this inventory, the player inventory is included in this list
    ItemStack[] contents = new ItemStack[super.getDimension().getSlotCount()];

    for (int i = 0; i < contents.length; i++) {
      contents[i] = super.mapper.fromMinecraft(slots.get(i).getStack());
    }

    return this.contents = contents;
  }

  @Override
  public void setContents(ItemStack[] contents) {
    super.setContents(contents);
    this.invalidate();
  }

  @Override
  public void setItem(int slot, ItemStack item) throws IndexOutOfBoundsException {
    super.setItem(slot, item);
    this.invalidate();
  }

  private void invalidate() {
    this.contents = null;
  }

  private class ContainerListener implements IContainerListener {

    @Override
    public void sendAllContents(Container containerToSend, NonNullList<net.minecraft.item.ItemStack> itemsList) {
      invalidate();
    }

    @Override
    public void sendSlotContents(Container containerToSend, int slotInd, net.minecraft.item.ItemStack stack) {
      invalidate();
    }

    @Override
    public void sendWindowProperty(Container containerIn, int varToUpdate, int newValue) {
    }

  }

}
