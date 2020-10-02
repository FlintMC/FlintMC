package net.labyfy.internal.component.items.inventory;

import net.labyfy.chat.component.ChatComponent;
import net.labyfy.component.items.ItemRegistry;
import net.labyfy.component.items.ItemStack;
import net.labyfy.component.items.inventory.InventoryDimension;
import net.labyfy.component.items.inventory.InventoryType;
import net.labyfy.component.items.mapper.MinecraftItemMapper;
import net.minecraft.inventory.container.Container;

import java.util.function.Supplier;

public abstract class VersionedInventory extends DefaultInventory {

  protected final MinecraftItemMapper mapper;
  private final Supplier<Container> containerSupplier;
  private final ChatComponent title;

  public VersionedInventory(ItemRegistry registry, int windowId, InventoryType type, InventoryDimension dimension,
                            MinecraftItemMapper mapper, Supplier<Container> containerSupplier, ChatComponent title) {
    super(registry, windowId, type, dimension);
    this.mapper = mapper;
    this.containerSupplier = containerSupplier;
    this.title = title;
  }

  public Container getContainer() {
    return this.containerSupplier.get();
  }

  @Override
  public ChatComponent getTitle() {
    return this.title;
  }

  protected void validateContents(ItemStack[] contents) throws IllegalArgumentException {
    if (contents.length > super.getDimension().getSlotCount()) {
      throw new IllegalArgumentException(contents.length + " are too many contents for an inventory with a size of " + super.getDimension().getSlotCount());
    }
  }

  @Override
  public ItemStack getItem(int slot) throws IndexOutOfBoundsException {
    return this.mapper.fromMinecraft(this.getContainer().getInventory().get(slot));
  }

  protected net.minecraft.item.ItemStack mapToVanilla(ItemStack item) {
    return (net.minecraft.item.ItemStack) this.mapper.toMinecraft(item == null ? this.registry.getAirType().createStack() : item);
  }

}
