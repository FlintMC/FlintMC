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

package net.flintmc.mcapi.v1_15_2.items.inventory;

import java.util.List;
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

public class VersionedOpenedInventory extends VersionedInventory {

  private ItemStack[] contents;

  public VersionedOpenedInventory(
      ItemRegistry registry,
      InventoryType type,
      InventoryDimension dimension,
      MinecraftItemMapper mapper,
      Container container,
      ChatComponent title) {
    super(registry, container.windowId, type, dimension, mapper, () -> container, title);
    container.addListener(new ContainerListener());
  }

  @Override
  public ItemStack[] getContents() {
    if (this.contents != null) {
      return this.contents;
    }

    List<Slot> slots = this.getContainer().inventorySlots;
    // the size of the slots list doesn't match the size of this inventory, the player inventory is
    // included in this list
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
    public void sendAllContents(
        Container containerToSend, NonNullList<net.minecraft.item.ItemStack> itemsList) {
      invalidate();
    }

    @Override
    public void sendSlotContents(
        Container containerToSend, int slotInd, net.minecraft.item.ItemStack stack) {
      invalidate();
    }

    @Override
    public void sendWindowProperty(Container containerIn, int varToUpdate, int newValue) {
    }
  }
}
