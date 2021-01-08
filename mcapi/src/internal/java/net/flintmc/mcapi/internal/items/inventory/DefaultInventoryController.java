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

import com.google.common.base.Preconditions;
import java.util.HashMap;
import java.util.Map;
import net.flintmc.framework.stereotype.NameSpacedKey;
import net.flintmc.mcapi.items.inventory.Inventory;
import net.flintmc.mcapi.items.inventory.InventoryController;
import net.flintmc.mcapi.items.inventory.InventoryType;
import net.flintmc.mcapi.items.inventory.player.PlayerInventory;

public abstract class DefaultInventoryController implements InventoryController {

  private final PlayerInventory playerInventory;

  private final Map<NameSpacedKey, InventoryType> inventoryTypes = new HashMap<>();

  private Inventory openInventory;

  public DefaultInventoryController(PlayerInventory playerInventory) {
    this.registerType(playerInventory.getType());
    this.playerInventory = playerInventory;
  }

  @Override
  public InventoryType[] getTypes() {
    return this.inventoryTypes.values().toArray(new InventoryType[0]);
  }

  @Override
  public InventoryType getType(NameSpacedKey registryName) {
    return this.inventoryTypes.get(registryName);
  }

  @Override
  public void registerType(InventoryType type) {
    Preconditions.checkArgument(
        !this.inventoryTypes.containsKey(type.getRegistryName()),
        "A type with the name %s is already registered",
        type.getRegistryName());
    this.inventoryTypes.put(type.getRegistryName(), type);
  }

  @Override
  public PlayerInventory getPlayerInventory() {
    return this.canOpenInventories() ? this.playerInventory : null;
  }

  @Override
  public Inventory getOpenInventory() {
    if (this.openInventory == null || !this.isOpened(this.openInventory)) {
      this.openInventory = this.defineOpenInventory();
    }
    return this.openInventory;
  }

  protected abstract boolean isOpened(Inventory inventory);

  protected abstract Inventory defineOpenInventory();
}
