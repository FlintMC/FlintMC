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

package net.flintmc.mcapi.items;

import com.google.gson.JsonObject;

/**
 * Serializer for the (de-)serialization of an {@link ItemStack} into a json that can be read be
 * vanilla minecraft.
 */
public interface ItemStackSerializer {

  /**
   * Parses an {@link ItemStack} out of the given json object.
   *
   * @param object The non-null object to parse the item out
   * @return The parsed item or {@code null} if an invalid json has been provided
   * @throws NullPointerException If no item with the id in the given json exists
   */
  ItemStack fromJson(JsonObject object);

  /**
   * Serializes the given {@link ItemStack} into a new json object to be read by vanilla minecraft.
   *
   * @param itemStack The non-null item to create the json from
   * @return The new non-null json object out of the given item
   */
  JsonObject toJson(ItemStack itemStack);
}
