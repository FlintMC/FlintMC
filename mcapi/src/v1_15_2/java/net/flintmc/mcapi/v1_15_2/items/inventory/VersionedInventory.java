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

import java.util.function.Supplier;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.internal.items.inventory.DefaultInventory;
import net.flintmc.mcapi.items.ItemRegistry;
import net.flintmc.mcapi.items.ItemStack;
import net.flintmc.mcapi.items.inventory.InventoryDimension;
import net.flintmc.mcapi.items.inventory.InventoryType;
import net.flintmc.mcapi.items.mapper.MinecraftItemMapper;
import net.minecraft.inventory.container.Container;

public abstract class VersionedInventory extends DefaultInventory {

  protected final MinecraftItemMapper mapper;
  private final Supplier<Container> containerSupplier;
  private final ChatComponent title;

  public VersionedInventory(
      ItemRegistry registry,
      int windowId,
      InventoryType type,
      InventoryDimension dimension,
      MinecraftItemMapper mapper,
      Supplier<Container> containerSupplier,
      ChatComponent title) {
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
      throw new IllegalArgumentException(
          contents.length
              + " are too many contents for an inventory with a size of "
              + super.getDimension().getSlotCount());
    }
  }

  @Override
  public ItemStack getItem(int slot) throws IndexOutOfBoundsException {
    return this.mapper.fromMinecraft(this.getContainer().getInventory().get(slot));
  }

  protected net.minecraft.item.ItemStack mapToVanilla(ItemStack item) {
    return (net.minecraft.item.ItemStack)
        this.mapper.toMinecraft(item == null ? this.registry.getAirType().createStack() : item);
  }
}
