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

package net.flintmc.mcapi.items.mapper;

import net.flintmc.mcapi.items.ItemRegistry;
import net.flintmc.mcapi.items.ItemStack;
import net.flintmc.mcapi.items.type.ItemType;

/**
 * A mapper between the Flint {@link ItemStack} and the Minecraft ItemStack.
 */
public interface MinecraftItemMapper {

  /**
   * Maps the given Minecraft ItemStack to a Flint {@link ItemStack}.
   *
   * @param handle The non-null minecraft ItemStack
   * @return The new non-null Flint ItemStack
   * @throws ItemMappingException If the given object is not an instance of the minecraft ItemStack
   * @throws ItemMappingException If no item matching the given stack exists in the {@link
   *                              ItemRegistry}.
   */
  ItemStack fromMinecraft(Object handle) throws ItemMappingException;

  /**
   * Maps the given Flint {@link ItemStack} to a minecraft ItemStack.
   *
   * @param stack The non-null Flint {@link ItemStack}
   * @return The new non-null minecraft ItemStack
   * @throws ItemMappingException If no item matching the given stack exists in the Item registry in
   *                              minecraft.
   */
  Object toMinecraft(ItemStack stack) throws ItemMappingException;

  ItemType fromMinecraftType(Object handle) throws ItemMappingException;

  Object toMinecraftType(ItemType type) throws ItemMappingException;

}
