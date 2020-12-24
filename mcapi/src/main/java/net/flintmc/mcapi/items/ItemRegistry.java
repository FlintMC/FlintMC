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

import net.flintmc.framework.stereotype.NameSpacedKey;
import net.flintmc.mcapi.items.meta.enchantment.EnchantmentType;
import net.flintmc.mcapi.items.type.ItemCategory;
import net.flintmc.mcapi.items.type.ItemType;

/**
 * A registry containing all available item types and categories.
 */
public interface ItemRegistry {

  /**
   * Registers a new item type in this registry.
   *
   * @param type The new non-null type
   */
  void registerType(ItemType type);

  /**
   * Retrieves an item type by the name out of this registry.
   *
   * @param registryName The non-null key for the item
   * @return The item type out of this registry matching the given key or {@code null} if no type
   * with the given key could be found
   */
  ItemType getType(NameSpacedKey registryName);

  /**
   * Retrieves all available types of items in this registry.
   *
   * @return The non-null array of items, modification to the array will have no effect
   */
  ItemType[] getTypes();

  /**
   * Retrieves the constant item type for air.
   *
   * @return The non-null item type for air
   */
  ItemType getAirType();

  /**
   * Retrieves all types in the given category.
   *
   * @param category The non-null category to get all item types from
   * @return A new non-null array of all types that were found in this category, modification to
   * this array will have no effect
   */
  ItemType[] getTypesInCategory(ItemCategory category);

  /**
   * Registers a new category in this registry. Currently, this is only intended to be used
   * internally as new categories aren't displayed by the client.
   *
   * @param category The new non-null category
   */
  void registerCategory(ItemCategory category);

  /**
   * Retrieves all available categories in this registry.
   *
   * @return A non-null array of all categories in this registry, modifcation to this array will
   * have no effect
   */
  ItemCategory[] getCategories();

  /**
   * Retrieves a category matching the given index.
   *
   * @param index The index of the category to search for
   * @return The category with the given index or {@code null} if no category with the given index
   * exists in this registry
   */
  ItemCategory getCategory(int index);

  /**
   * Retrieves a category matching the given name.
   *
   * @param registryName The non-null name of the category to search for
   * @return The category with the given name or {@code null} if no category with the given name
   * exists in this registry
   */
  ItemCategory getCategory(NameSpacedKey registryName);

  /**
   * Registers a new enchantment type in this registry.
   *
   * @param type The new non-null type
   */
  void registerEnchantmentType(EnchantmentType type);

  /**
   * Retrieves all available types of enchantments in this registry.
   *
   * @return The non-null array of enchantment types, modification to the array will have no effect
   */
  EnchantmentType[] getEnchantmentTypes();

  /**
   * Retrieves an enchantment type by the name out of this registry.
   *
   * @param registryName The non-null key for the enchantment type
   * @return The enchantment type out of this registry matching the given key or {@code null} if no
   * type with the given key could be found
   */
  EnchantmentType getEnchantmentType(NameSpacedKey registryName);
}
