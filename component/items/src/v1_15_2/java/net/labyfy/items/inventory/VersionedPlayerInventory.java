package net.labyfy.items.inventory;

import net.labyfy.chat.builder.ComponentBuilder;
import net.labyfy.items.ItemRegistry;
import net.labyfy.items.ItemStack;
import net.labyfy.items.inventory.player.PlayerHand;
import net.labyfy.items.mapper.MinecraftItemMapper;
import net.minecraft.client.Minecraft;
import net.minecraft.util.NonNullList;

public class VersionedPlayerInventory extends DefaultPlayerInventory {

  private final MinecraftItemMapper itemMapper;

  public VersionedPlayerInventory(ItemRegistry registry, InventoryType type, InventoryDimension dimension, ComponentBuilder.Factory componentFactory, MinecraftItemMapper itemMapper) {
    super(registry, type, dimension, componentFactory);
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
  protected ItemStack getArmorSlot(int slot) {
    return this.getItem(Minecraft.getInstance().player.inventory.armorInventory, slot);
  }

  @Override
  protected ItemStack getMainSlot(int slot) {
    return this.getItem(Minecraft.getInstance().player.inventory.mainInventory, slot);
  }

  @Override
  protected ItemStack getOffHandSlot() {
    return this.getItem(Minecraft.getInstance().player.inventory.offHandInventory, 0);
  }

  @Override
  public ItemStack[] getArmorContents() {
    return this.map(Minecraft.getInstance().player.inventory.armorInventory);
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
    return null;
  }

  @Override
  public ItemStack[] getContents() {
    return this.map(Minecraft.getInstance().player.inventory.mainInventory);
  }

  @Override
  public void setContents(ItemStack[] contents) throws IllegalArgumentException {
    // TODO implement?
  }

}
