package net.flintmc.mcapi.items.v1_15_2.inventory;

import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.items.ItemRegistry;
import net.flintmc.mcapi.items.ItemStack;
import net.flintmc.mcapi.items.inventory.InventoryDimension;
import net.flintmc.mcapi.items.inventory.InventoryType;
import net.flintmc.mcapi.items.mapper.MinecraftItemMapper;
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
    super.validateContents(contents);

    for (int i = 0; i < contents.length; i++) {
      this.getContainer().getSlot(i).putStack(super.mapToVanilla(contents[i]));
    }

    this.invalidate();
  }

  @Override
  public void setItem(int slot, ItemStack item) throws IndexOutOfBoundsException {
    this.getContainer().getSlot(slot).putStack(super.mapToVanilla(item));
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
