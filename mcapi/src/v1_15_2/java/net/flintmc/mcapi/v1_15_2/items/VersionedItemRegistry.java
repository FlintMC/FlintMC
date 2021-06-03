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

package net.flintmc.mcapi.v1_15_2.items;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.util.HashMap;
import java.util.Map;
import net.flintmc.framework.eventbus.event.subscribe.PreSubscribe;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.framework.stereotype.NameSpacedKey;
import net.flintmc.mcapi.chat.builder.ComponentBuilder;
import net.flintmc.mcapi.internal.items.DefaultItemRegistry;
import net.flintmc.mcapi.items.ItemRegistry;
import net.flintmc.mcapi.items.meta.ItemMeta;
import net.flintmc.mcapi.items.meta.enchantment.EnchantmentRarity;
import net.flintmc.mcapi.items.meta.enchantment.EnchantmentType;
import net.flintmc.mcapi.items.type.ItemCategory;
import net.flintmc.mcapi.items.type.ItemType;
import net.flintmc.mcapi.registry.RegistryRegisterEvent;
import net.flintmc.mcapi.resources.ResourceLocation;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

@Singleton
@Implement(ItemRegistry.class)
public class VersionedItemRegistry extends DefaultItemRegistry {

  @Inject
  private VersionedItemRegistry(
      ItemType.Builder.Factory itemFactory,
      EnchantmentType.Factory enchantmentFactory,
      ComponentBuilder.Factory componentFactory) {
    super(
        itemFactory,
        enchantmentFactory,
        componentFactory
    );
  }

  @PreSubscribe
  public void registerItemsAndEnchantments(final RegistryRegisterEvent event) {
    ResourceLocation registryKey = event.getRegistryKeyLocation();
    String path = registryKey.getPath();

    if (path.equalsIgnoreCase("item")) {
      this.registerItem((Item) event.getRegistryObject(), event.getRegistryValueLocation());
    } else if (path.equalsIgnoreCase("enchantment")) {
      this.registerEnchantment((Enchantment) event.getRegistryObject(),
          event.getRegistryValueLocation());
    }

  }

  private void registerItem(final Item item, final ResourceLocation resourceLocation) {
    Map<NameSpacedKey, Class<? extends ItemMeta>> specialMetaClasses =
        this.getSpecialItemMetaClasses();

    // create one builder for every item
    ItemType.Builder builder = super.itemFactory.newBuilder();

    NameSpacedKey registryName =
        NameSpacedKey.of(resourceLocation.getNamespace(), resourceLocation.getPath());

    // creative mode categories
    ItemCategory category = this.findCategory(registryName, item);

    // copy all values of the minecraft item to our builder
    builder
        .category(category)
        .registryName(registryName)
        .defaultDisplayName(
            super.componentFactory.translation().translationKey(item.getTranslationKey()).build())
        .maxDamage(item.getMaxDamage())
        .maxStackSize(item.getMaxStackSize());

    // exceptions for the ItemMeta like leather armor
    if (specialMetaClasses.containsKey(registryName)) {
      builder.metaClass(specialMetaClasses.get(registryName));
    }

    super.registerType(builder.build());

    // re-use the builder for every other item in the registry
    builder.reset();
  }

  private void registerEnchantment(final Enchantment enchantment,
      final ResourceLocation resourceLocation) {
    NameSpacedKey registryName =
        NameSpacedKey.of(resourceLocation.getNamespace(), resourceLocation.getPath());
    Enchantment.Rarity rarity = enchantment.getRarity();

    super.registerEnchantmentType(
        super.enchantmentFactory
            .newBuilder()
            .registryName(registryName)
            .highestLevel(enchantment.getMaxLevel())
            .rarity(
                rarity == Enchantment.Rarity.UNCOMMON
                    ? EnchantmentRarity.UNCOMMON
                    : rarity == Enchantment.Rarity.COMMON
                        ? EnchantmentRarity.COMMON
                        : rarity == Enchantment.Rarity.RARE
                            ? EnchantmentRarity.RARE
                            : rarity == Enchantment.Rarity.VERY_RARE
                                ? EnchantmentRarity.EPIC
                                : null)
            .build());
  }

  private Map<NameSpacedKey, Class<? extends ItemMeta>> getSpecialItemMetaClasses() {
    Map<NameSpacedKey, Class<? extends ItemMeta>> exceptions = new HashMap<>();

    // TODO add metas like the leather armor with the configurable color

    return exceptions;
  }

  private ItemCategory findCategory(NameSpacedKey itemRegistryName, Item item) {
    if (item.getGroup() == null || item.getGroup() == ItemGroup.SEARCH) {
      // items like the barrier don't have a category in the creative menu
      return null;
    }

    NameSpacedKey registryName = NameSpacedKey.minecraft(item.getGroup().getPath());

    ItemCategory category = super.getCategory(registryName);
    if (category != null) {
      return category;
    }

    category =
        ItemCategory.create(
            registryName,
            this.componentFactory
                .translation()
                .translationKey(item.getGroup().getTranslationKey())
                .build(),
            () -> super.getType(itemRegistryName),
            item.getGroup().drawInForegroundOfTab(),
            item.getGroup().getIndex());

    super.registerCategory(category);
    return category;
  }
}
