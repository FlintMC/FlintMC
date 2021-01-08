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

package net.flintmc.mcapi.render.image.util;

import net.flintmc.mcapi.items.ItemStack;
import net.flintmc.mcapi.items.mapper.MinecraftItemMapper;

/**
 * Renderer for item stacks from Minecraft.
 */
public interface ItemRenderer {

  /**
   * Draws the given item stack with its amount on the screen. The display name, lore, ... won't be
   * rendered when hovering over the item. This may create a copy of the item to get the Item for
   * Minecraft, for better performance you should use {@link #drawRawItemStack(float, float, float,
   * Object)}.
   *
   * <p>The item will be rendered in 16x16 pixel and scaled if set.
   *
   * @param x     The x coordinate on the screen
   * @param y     The y coordinate on the screen
   * @param scale The scale at which to scale the rendered item, 1 to disable scaling
   * @param item  The non-null item to be rendered
   * @see #drawRawItemStack(float, float, float, Object)
   */
  void drawItemStack(float x, float y, float scale, ItemStack item);

  /**
   * Draws the given item stack with its amount on the screen. The display name, lore, ... won't be
   * rendered when hovering over the item. To create the item, {@link
   * MinecraftItemMapper#toMinecraft(ItemStack)} should be used only once and not every render
   * tick.
   *
   * <p>The item will be rendered in 16x16 pixel and scaled if set.
   *
   * @param x             The x coordinate on the screen
   * @param y             The y coordinate on the screen
   * @param scale         The scale at which to scale the rendered item, 1 to disable scaling
   * @param minecraftItem The non-null minecraft item to be rendered
   * @throws IllegalArgumentException If {@code minecraftItem} is not an instance of the Minecraft
   *                                  ItemStack
   * @see MinecraftItemMapper#toMinecraft(ItemStack)
   * @see #drawItemStack(float, float, float, ItemStack)
   */
  void drawRawItemStack(float x, float y, float scale, Object minecraftItem);
}
