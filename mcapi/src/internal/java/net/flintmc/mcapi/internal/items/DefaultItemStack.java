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

package net.flintmc.mcapi.internal.items;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.items.ItemStack;
import net.flintmc.mcapi.items.meta.ItemMeta;
import net.flintmc.mcapi.items.type.ItemType;

@Implement(ItemStack.class)
public class DefaultItemStack implements ItemStack {

  private final ItemMeta.Factory metaFactory;
  private final ItemType type;
  private final int stackSize;
  private ItemMeta meta;

  @AssistedInject
  public DefaultItemStack(
      ItemMeta.Factory metaFactory,
      @Assisted("type") ItemType type,
      @Assisted("stackSize") int stackSize) {
    this.metaFactory = metaFactory;
    this.type = type;
    this.stackSize = stackSize;
  }

  @AssistedInject
  public DefaultItemStack(
      ItemMeta.Factory metaFactory,
      @Assisted("type") ItemType type,
      @Assisted("stackSize") int stackSize,
      @Assisted("meta") ItemMeta meta) {
    this.metaFactory = metaFactory;
    this.type = type;
    this.stackSize = stackSize;
    this.meta = meta;
  }

  @Override
  public boolean hasItemMeta() {
    return this.meta != null;
  }

  @Override
  public ItemMeta getItemMeta(boolean create) {
    if (create && this.meta == null) {
      this.meta = this.metaFactory.createMeta(this.type);
    }
    return this.meta;
  }

  @Override
  public ItemMeta getItemMeta() {
    return this.getItemMeta(true);
  }

  @Override
  public int getStackSize() {
    return this.stackSize;
  }

  @Override
  public ItemType getType() {
    return this.type;
  }
}
