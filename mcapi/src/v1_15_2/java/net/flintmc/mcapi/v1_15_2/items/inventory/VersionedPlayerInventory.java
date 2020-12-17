package net.flintmc.mcapi.v1_15_2.items.inventory;

import net.flintmc.mcapi.chat.builder.ComponentBuilder;
import net.flintmc.mcapi.items.ItemRegistry;
import net.flintmc.mcapi.items.ItemStack;
import net.flintmc.mcapi.items.inventory.EquipmentSlotType;
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

  private static final EquipmentSlotType[] SLOT_TYPES = EquipmentSlotType.values();

  private final MinecraftItemMapper itemMapper;

  public VersionedPlayerInventory(
      ItemRegistry registry,
      InventoryType type,
      InventoryDimension dimension,
      ComponentBuilder.Factory componentFactory,
      MinecraftItemMapper itemMapper) {
    super(
        registry,
        0,
        type,
        dimension,
        itemMapper,
        () -> Minecraft.getInstance().player.openContainer,
        componentFactory.text().text("Player").build());
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
  public int getHeldItemSlot() {
    int slot = Minecraft.getInstance().player.inventory.currentItem;
    return slot >= 0 && slot <= 8 ? slot : -1;
  }

  @Override
  public void setHeldItemSlot(int slot) {
    if (slot < 0 || slot > 8) {
      throw new IndexOutOfBoundsException("Slot cannot be smaller than 0 and larger than 8");
    }
    Minecraft.getInstance().player.inventory.currentItem = slot;
  }

  @Override
  public EquipmentSlotType getSlotType(int slot) {
    if (this.getHeldItemSlot() == slot) {
      return EquipmentSlotType.MAIN_HAND;
    }

    for (EquipmentSlotType slotType : SLOT_TYPES) {
      if (slotType.getSlotIndex() == slot) {
        return slotType;
      }
    }

    return null;
  }

  @Override
  public int getHandSlot(PlayerHand hand) {
    if (hand == PlayerHand.OFF_HAND) {
      return 45;
    }
    return 36 + this.getHeldItemSlot();
  }

  @Override
  public boolean hasHand(PlayerHand hand) {
    return true;
  }

  @Override
  public ItemStack getCursor() {
    Object item = Minecraft.getInstance().player.inventory.getItemStack();
    return item == null
        ? super.registry.getAirType().createStack()
        : this.itemMapper.fromMinecraft(item);
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
