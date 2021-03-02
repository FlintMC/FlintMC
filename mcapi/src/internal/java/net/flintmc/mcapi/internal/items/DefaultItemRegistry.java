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

import com.google.common.base.Preconditions;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import net.flintmc.framework.stereotype.NameSpacedKey;
import net.flintmc.mcapi.chat.builder.ComponentBuilder;
import net.flintmc.mcapi.chat.component.event.HoverEvent;
import net.flintmc.mcapi.chat.serializer.ComponentSerializer;
import net.flintmc.mcapi.internal.items.component.HoverItemSerializer;
import net.flintmc.mcapi.items.ItemRegistry;
import net.flintmc.mcapi.items.ItemStackSerializer;
import net.flintmc.mcapi.items.meta.enchantment.EnchantmentType;
import net.flintmc.mcapi.items.type.ItemCategory;
import net.flintmc.mcapi.items.type.ItemType;

public abstract class DefaultItemRegistry implements ItemRegistry {

  protected final ItemType.Builder.Factory itemFactory;
  protected final EnchantmentType.Factory enchantmentFactory;
  protected final ComponentBuilder.Factory componentFactory;

  private final ItemType airType;

  private final Map<NameSpacedKey, ItemCategory> itemCategories = new HashMap<>();
  private final Map<NameSpacedKey, ItemType> itemTypes = new HashMap<>();
  private final Map<NameSpacedKey, EnchantmentType> enchantmentTypes = new HashMap<>();

  public DefaultItemRegistry(
      ItemType.Builder.Factory itemFactory,
      EnchantmentType.Factory enchantmentFactory,
      ComponentBuilder.Factory componentFactory,
      ComponentSerializer.Factory componentSerializerFactory,
      ItemStackSerializer itemStackSerializer) {
    this.itemFactory = itemFactory;
    this.enchantmentFactory = enchantmentFactory;
    this.componentFactory = componentFactory;

    this.airType =
        itemFactory
            .newBuilder()
            .category(null)
            .registryName(NameSpacedKey.minecraft("air"))
            .build();
    this.registerItems();

    componentSerializerFactory
        .gson()
        .registerHoverContentSerializer(
            HoverEvent.Action.SHOW_ITEM, new HoverItemSerializer(itemStackSerializer));
  }

  protected abstract void registerItems();

  @Override
  public void registerType(ItemType type) {
    Preconditions.checkArgument(
        !this.itemTypes.containsKey(type.getRegistryName()),
        "A type with the name %s is already registered",
        type.getRegistryName());
    this.itemTypes.put(type.getRegistryName(), type);
  }

  @Override
  public ItemType getType(NameSpacedKey registryName) {
    if (registryName.equals(this.airType.getRegistryName())) {
      return this.airType;
    }
    return this.itemTypes.get(registryName);
  }

  @Override
  public ItemType[] getTypes() {
    return this.itemTypes.values().toArray(new ItemType[0]);
  }

  @Override
  public ItemType getAirType() {
    return this.airType;
  }

  @Override
  public ItemType[] getTypesInCategory(ItemCategory category) {
    Collection<ItemType> types = new HashSet<>();

    for (ItemType type : this.itemTypes.values()) {
      //   no category                                     or  the exact category
      if ((category == null && type.getCategory() == null)
          || (type.getCategory() != null && type.getCategory().equals(category))) {
        types.add(type);
      }
    }

    return types.toArray(new ItemType[0]);
  }

  @Override
  public void registerCategory(ItemCategory category) {
    Preconditions.checkArgument(
        !this.itemCategories.containsKey(category.getRegistryName()),
        "A category with the name %s is already registered",
        category.getRegistryName());
    this.itemCategories.put(category.getRegistryName(), category);
  }

  @Override
  public ItemCategory[] getCategories() {
    return this.itemCategories.values().toArray(new ItemCategory[0]);
  }

  @Override
  public ItemCategory getCategory(int index) {
    for (ItemCategory category : this.itemCategories.values()) {
      if (category.getIndex() == index) {
        return category;
      }
    }
    return null;
  }

  @Override
  public ItemCategory getCategory(NameSpacedKey registryName) {
    return this.itemCategories.get(registryName);
  }

  @Override
  public void registerEnchantmentType(EnchantmentType type) {
    Preconditions.checkArgument(
        !this.enchantmentTypes.containsKey(type.getRegistryName()),
        "An enchantment type with the name %s is already registered",
        type.getRegistryName());
    this.enchantmentTypes.put(type.getRegistryName(), type);
  }

  @Override
  public EnchantmentType[] getEnchantmentTypes() {
    return this.enchantmentTypes.values().toArray(new EnchantmentType[0]);
  }

  @Override
  public EnchantmentType getEnchantmentType(NameSpacedKey registryName) {
    return this.enchantmentTypes.get(registryName);
  }
}
