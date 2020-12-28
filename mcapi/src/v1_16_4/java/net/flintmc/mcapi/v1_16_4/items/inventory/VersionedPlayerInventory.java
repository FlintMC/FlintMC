/*
 * FlintMC
 * Copyright (C) 2020-2021 LabyMedia GmbH and contributors
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package net.flintmc.mcapi.v1_16_4.items.inventory;

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

  /** {@inheritDoc} */
  @Override
  public void setItem(int slot, ItemStack item) throws IndexOutOfBoundsException {
    Minecraft.getInstance().player.inventory.mainInventory.set(slot, super.mapToVanilla(item));
  }

  /** {@inheritDoc} */
  @Override
  public ItemStack getArmorPart(PlayerArmorPart part) {
    return this.getItem(Minecraft.getInstance().player.inventory.armorInventory, part.getIndex());
  }

  /** {@inheritDoc} */
  @Override
  public ItemStack getItemInHand(PlayerHand hand) {
    int slot = this.getHandSlot(hand);
    return slot == -1 ? super.registry.getAirType().createStack() : this.getItem(slot);
  }

  /** {@inheritDoc} */
  @Override
  public int getHeldItemSlot() {
    int slot = Minecraft.getInstance().player.inventory.currentItem;
    return slot >= 0 && slot <= 8 ? slot : -1;
  }

  /** {@inheritDoc} */
  @Override
  public void setHeldItemSlot(int slot) {
    if (slot < 0 || slot > 8) {
      throw new IndexOutOfBoundsException("Slot cannot be smaller than 0 and larger than 8");
    }
    Minecraft.getInstance().player.inventory.currentItem = slot;
  }

  /** {@inheritDoc} */
  @Override
  public EquipmentSlotType getSlotType(int slot) {
    if (this.getHandSlot(PlayerHand.MAIN_HAND) == slot) {
      return EquipmentSlotType.MAIN_HAND;
    }

    switch (slot) {
      case 5:
        return EquipmentSlotType.HEAD;
      case 6:
        return EquipmentSlotType.CHEST;
      case 7:
        return EquipmentSlotType.LEGS;
      case 8:
        return EquipmentSlotType.FEET;
      case 45:
        return EquipmentSlotType.OFF_HAND;
      default:
        return null;
    }
  }

  /** {@inheritDoc} */
  @Override
  public ItemStack getItem(EquipmentSlotType slotType) {
    if (slotType == EquipmentSlotType.MAIN_HAND) {
      return this.getItem(this.getHandSlot(PlayerHand.MAIN_HAND));
    }

    int slot;

    switch (slotType) {
      case HEAD:
        slot = 5;
        break;
      case CHEST:
        slot = 6;
        break;
      case LEGS:
        slot = 7;
        break;
      case FEET:
        slot = 8;
        break;
      case OFF_HAND:
        slot = 45;
        break;
      default:
        throw new IllegalStateException("Unexpected value: " + slotType);
    }

    return this.getItem(slot);
  }

  /** {@inheritDoc} */
  @Override
  public int getHandSlot(PlayerHand hand) {
    if (hand == PlayerHand.OFF_HAND) {
      return 45;
    }
    return 36 + this.getHeldItemSlot();
  }

  /** {@inheritDoc} */
  @Override
  public boolean hasHand(PlayerHand hand) {
    return true;
  }

  /** {@inheritDoc} */
  @Override
  public ItemStack getCursor() {
    Object item = Minecraft.getInstance().player.inventory.getItemStack();
    return item == null
        ? super.registry.getAirType().createStack()
        : this.itemMapper.fromMinecraft(item);
  }

  /** {@inheritDoc} */
  @Override
  public void closeInventory() {
    if (Minecraft.getInstance().currentScreen instanceof ContainerScreen) {
      Minecraft.getInstance().player.closeScreen();
    }
  }

  /** {@inheritDoc} */
  @Override
  public ItemStack[] getContents() {
    return this.map(Minecraft.getInstance().player.inventory.mainInventory);
  }

  /** {@inheritDoc} */
  @Override
  public void setContents(ItemStack[] contents) throws IllegalArgumentException {
    super.validateContents(contents);

    for (int i = 0; i < contents.length; i++) {
      this.setItem(i, contents[i]);
    }
  }
}
