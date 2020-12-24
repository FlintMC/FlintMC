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

package net.flintmc.mcapi.items.component;

import net.flintmc.mcapi.chat.component.event.HoverEvent;
import net.flintmc.mcapi.chat.component.event.content.HoverContent;
import net.flintmc.mcapi.items.ItemStack;

/**
 * The content of a {@link HoverEvent} which displays an item.
 */
public class HoverItem extends HoverContent {

  private final ItemStack itemStack;

  /**
   * Creates a new content for a {@link HoverEvent} which displays an item.
   *
   * @param itemStack The non-null item to be displayed
   */
  public HoverItem(ItemStack itemStack) {
    this.itemStack = itemStack;
  }

  /**
   * Retrieves the item that will be used when displaying the item.
   *
   * @return The non-null item
   */
  public ItemStack getItemStack() {
    return this.itemStack;
  }

  @Override
  public HoverEvent.Action getAction() {
    return HoverEvent.Action.SHOW_ITEM;
  }
}
