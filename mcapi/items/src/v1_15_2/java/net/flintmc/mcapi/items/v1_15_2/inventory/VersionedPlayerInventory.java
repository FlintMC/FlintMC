package net.flintmc.mcapi.items.v1_15_2.inventory;

import net.flintmc.mcapi.chat.builder.ComponentBuilder;
import net.flintmc.mcapi.items.ItemRegistry;
import net.flintmc.mcapi.items.ItemStack;
import net.flintmc.mcapi.items.inventory.InventoryDimension;
import net.flintmc.mcapi.items.inventory.InventoryType;
import net.flintmc.mcapi.items.inventory.player.PlayerArmorPart;
import net.flintmc.mcapi.items.inventory.player.PlayerHand;
import net.flintmc.mcapi.items.inventory.player.PlayerInventory;
import net.flintmc.mcapi.items.mapper.MinecraftItemMapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.util.NonNullList;

public class VersionedPlayerInventory extends VersionedInventory implements PlayerInventory {

  private final MinecraftItemMapper itemMapper;

  public VersionedPlayerInventory(ItemRegistry registry, InventoryType type, InventoryDimension dimension, ComponentBuilder.Factory componentFactory, MinecraftItemMapper itemMapper) {
    super(registry, 0, type, dimension, itemMapper, () -> Minecraft.getInstance().player.container, componentFactory.text().text("Player").build());
    this.itemMapper = itemMapper;
  }

  private ItemStack getItem(NonNullList<net.minecraft.item.ItemStack> inventory, int slot) {
    return this.itemMapper.fromMinecraft(inventory.get(slot));
  }

  private ItemStack[] map(NonNullList<net.minecraft.item.ItemStack> inventory) {
    ItemStack[] result = new ItemStack[inventory.size()];
    for (int i = 0; i < inventory.size(); i++) {
      result[i] = this.itemMapper.fromMinecraft(inventory.get(i));
    }
    return result;
  }

  @Override
  public ItemStack getItem(int slot) throws IndexOutOfBoundsException {
    this.validateSlot(slot);

    if (slot >= 0 && slot <= 35) {
      return this.getItem(Minecraft.getInstance().player.inventory.mainInventory, slot);
    }
    if (slot >= 36 && slot <= 39) {
      return this.getItem(Minecraft.getInstance().player.inventory.armorInventory, slot - 36);
    }
    if (this.getHandSlot(PlayerHand.OFF_HAND) == slot) {
      return this.getItem(Minecraft.getInstance().player.inventory.offHandInventory, 0);
    }
    throw new IndexOutOfBoundsException("Invalid slot provided: " + slot);
  }

  @Override
  public void setItem(int slot, ItemStack item) throws IndexOutOfBoundsException {
    Minecraft.getInstance().player.inventory.mainInventory.set(slot, super.mapToVanilla(item));
  }

  @Override
  public ItemStack getArmorPart(PlayerArmorPart part) {
    return this.getItem(Minecraft.getInstance().player.inventory.armorInventory, part.getIndex());
  }

  @Override
  public ItemStack getItemInHand(PlayerHand hand) {
    int slot = this.getHandSlot(hand);
    return slot == -1 ? super.registry.getAirType().createStack() : this.getItem(slot);
  }

  @Override
  public int getHandSlot(PlayerHand hand) {
    if (hand == PlayerHand.OFF_HAND) {
      return 40;
    }
    int currentItem = Minecraft.getInstance().player.inventory.currentItem;
    return currentItem >= 0 && currentItem <= 8 ? currentItem : -1;
  }

  @Override
  public boolean hasHand(PlayerHand hand) {
    return true;
  }

  @Override
  public ItemStack getCursor() {
    Object item = Minecraft.getInstance().player.inventory.getItemStack();
    return item == null ? super.registry.getAirType().createStack() : this.itemMapper.fromMinecraft(item);
  }

  @Override
  public void closeInventory() {
    if (Minecraft.getInstance().currentScreen instanceof ContainerScreen) {
      Minecraft.getInstance().player.closeScreen();
    }
  }

  @Override
  public ItemStack[] getContents() {
    return this.map(Minecraft.getInstance().player.inventory.mainInventory);
  }

  @Override
  public void setContents(ItemStack[] contents) throws IllegalArgumentException {
    super.validateContents(contents);

    for (int i = 0; i < contents.length; i++) {
      this.setItem(i, contents[i]);
    }
  }

}
