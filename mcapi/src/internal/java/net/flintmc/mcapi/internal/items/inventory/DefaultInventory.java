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

package net.flintmc.mcapi.internal.items.inventory;

import com.google.common.primitives.Ints;
import java.util.ArrayList;
import java.util.Collection;
import net.flintmc.mcapi.items.ItemRegistry;
import net.flintmc.mcapi.items.ItemStack;
import net.flintmc.mcapi.items.inventory.Inventory;
import net.flintmc.mcapi.items.inventory.InventoryDimension;
import net.flintmc.mcapi.items.inventory.InventoryType;
import net.flintmc.mcapi.items.type.ItemType;

public abstract class DefaultInventory implements Inventory {

  protected final ItemRegistry registry;
  private final int windowId;
  private final InventoryType type;
  private final InventoryDimension dimension;

  public DefaultInventory(
      ItemRegistry registry, int windowId, InventoryType type, InventoryDimension dimension) {
    this.registry = registry;
    this.windowId = windowId;
    this.type = type;
    this.dimension = dimension;
  }

  @Override
  public int getWindowId() {
    return this.windowId;
  }

  @Override
  public InventoryType getType() {
    return this.type;
  }

  @Override
  public InventoryDimension getDimension() {
    return this.dimension;
  }

  @Override
  public int countItems(ItemType type) {
    int count = 0;
    for (ItemStack content : this.getContents()) {
      if (content != null && content.getType().equals(type)) {
        count += content.getStackSize();
      }
    }

    return count;
  }

  @Override
  public int[] findSlots(ItemType type) {
    Collection<Integer> slots = new ArrayList<>();

    ItemStack[] contents = this.getContents();
    for (int i = 0; i < contents.length; i++) {
      ItemStack content = contents[i];
      if (content != null && content.getType().equals(type)) {
        slots.add(i);
      }
    }

    return Ints.toArray(slots);
  }
}
