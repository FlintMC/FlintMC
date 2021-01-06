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

package net.flintmc.mcapi.internal.items.inventory.event;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.items.ItemStack;
import net.flintmc.mcapi.items.inventory.Inventory;
import net.flintmc.mcapi.items.inventory.InventoryClick;
import net.flintmc.mcapi.items.inventory.event.InventoryClickEvent;

/**
 * {@inheritDoc}
 */
@Implement(InventoryClickEvent.class)
public class DefaultInventoryClickEvent extends DefaultInventoryEvent
    implements InventoryClickEvent {

  private final ItemStack clickedItem;
  private final InventoryClick clickType;
  private final int slot;
  private boolean cancelled;

  @AssistedInject
  public DefaultInventoryClickEvent(
      @Assisted("inventory") Inventory inventory,
      @Assisted("slot") int slot,
      @Assisted("clickType") InventoryClick clickType) {
    super(inventory);
    this.clickedItem = slot < 0 /* outside of any slot */ ? null : inventory.getItem(slot);
    this.clickType = clickType;
    this.slot = slot;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ItemStack getClickedItem() {
    return this.clickedItem;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public InventoryClick getClickType() {
    return this.clickType;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getSlot() {
    return this.slot;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isCancelled() {
    return this.cancelled;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setCancelled(boolean cancelled) {
    this.cancelled = cancelled;
  }
}
