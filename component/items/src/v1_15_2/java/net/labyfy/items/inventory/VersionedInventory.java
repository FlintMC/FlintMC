package net.labyfy.items.inventory;

import net.labyfy.chat.component.ChatComponent;
import net.labyfy.items.ItemRegistry;
import net.labyfy.items.ItemStack;
import net.minecraft.inventory.container.Container;

public class VersionedInventory extends DefaultInventory {

  private final Container container;

  public VersionedInventory(ItemRegistry registry, int windowId, InventoryType type, InventoryDimension dimension, Container container) {
    super(registry, windowId, type, dimension);
    this.container = container;
  }

  @Override
  public ChatComponent getTitle() {
    return null;
  }

  @Override
  public void setTitle(ChatComponent component) {

  }

  @Override
  public ItemStack[] getContents() {
    return new ItemStack[0];
  }

  @Override
  public void setContents(ItemStack[] contents) throws IllegalArgumentException {

  }

  @Override
  public ItemStack getItem(int slot) throws IndexOutOfBoundsException {
    return null;
  }

}
