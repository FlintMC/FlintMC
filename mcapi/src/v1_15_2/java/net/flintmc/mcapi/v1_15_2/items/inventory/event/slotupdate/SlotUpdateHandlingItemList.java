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

package net.flintmc.mcapi.v1_15_2.items.inventory.event.slotupdate;

import com.google.common.base.Preconditions;
import java.util.Arrays;
import net.flintmc.framework.eventbus.EventBus;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe.Phase;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.mcapi.items.inventory.InventoryController;
import net.flintmc.mcapi.items.inventory.event.InventoryUpdateSlotEvent;
import net.flintmc.mcapi.items.mapper.MinecraftItemMapper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class SlotUpdateHandlingItemList extends NonNullList<ItemStack> {

  private final InventoryController controller;
  private final MinecraftItemMapper itemMapper;
  private final EventBus eventBus;
  private final InventoryUpdateSlotEvent.Factory eventFactory;
  private final int offset;

  @AssistedInject
  private SlotUpdateHandlingItemList(
      InventoryController controller,
      MinecraftItemMapper itemMapper,
      EventBus eventBus,
      InventoryUpdateSlotEvent.Factory eventFactory,
      @Assisted("offset") int offset,
      @Assisted("size") int size,
      @Assisted NonNullList<ItemStack> copy) {
    super(Arrays.asList(create(size)), ItemStack.EMPTY);
    this.controller = controller;
    this.itemMapper = itemMapper;
    this.eventBus = eventBus;
    this.eventFactory = eventFactory;
    this.offset = offset;

    for (int i = 0; i < copy.size(); i++) {
      super.set(i, copy.get(i));
    }
  }

  private static ItemStack[] create(int size) {
    ItemStack[] items = new ItemStack[size];
    Arrays.fill(items, ItemStack.EMPTY);
    return items;
  }

  @Override
  public ItemStack set(int index, ItemStack item) {
    Preconditions.checkNotNull(item);

    int slot = this.offset + index;
    if (slot >= 9 && slot <= 17) {
      slot += 36 - 9; // hotbar starts at 36, but somehow not everywhere in Minecraft
    } else if (slot >= 18 && slot <= 44) {
      slot -= 9;
    }

    InventoryUpdateSlotEvent event =
        this.eventFactory.create(
            this.controller.getPlayerInventory(), slot, this.itemMapper.fromMinecraft(item));

    this.eventBus.fireEvent(event, Phase.PRE);
    ItemStack result = super.set(index, item);
    this.eventBus.fireEvent(event, Phase.POST);

    return result;
  }

  @AssistedFactory(SlotUpdateHandlingItemList.class)
  public interface Factory {

    SlotUpdateHandlingItemList create(
        @Assisted("offset") int offset,
        @Assisted("size") int size,
        @Assisted NonNullList<ItemStack> copy);
  }
}
