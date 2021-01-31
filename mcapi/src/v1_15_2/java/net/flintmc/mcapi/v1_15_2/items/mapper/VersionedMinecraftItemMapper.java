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

package net.flintmc.mcapi.v1_15_2.items.mapper;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.framework.stereotype.NameSpacedKey;
import net.flintmc.mcapi.items.ItemRegistry;
import net.flintmc.mcapi.items.ItemStack;
import net.flintmc.mcapi.items.mapper.ItemMappingException;
import net.flintmc.mcapi.items.mapper.MinecraftItemMapper;
import net.flintmc.mcapi.items.meta.ItemMeta;
import net.flintmc.mcapi.items.type.ItemType;
import net.minecraft.item.Item;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

@Singleton
@Implement(value = MinecraftItemMapper.class, version = "1.15.2")
public class VersionedMinecraftItemMapper implements MinecraftItemMapper {

  private final ItemRegistry registry;
  private final ItemStack.Factory itemFactory;
  private final ItemMeta.Factory metaFactory;

  @Inject
  public VersionedMinecraftItemMapper(
      ItemRegistry registry, ItemStack.Factory itemFactory, ItemMeta.Factory metaFactory) {
    this.registry = registry;
    this.itemFactory = itemFactory;
    this.metaFactory = metaFactory;
  }

  @Override
  public ItemStack fromMinecraft(Object handle) throws ItemMappingException {
    if (!(handle instanceof net.minecraft.item.ItemStack)) {
      throw new ItemMappingException(
          handle.getClass().getName()
              + " is not an instance of "
              + net.minecraft.item.ItemStack.class.getName());
    }
    net.minecraft.item.ItemStack stack = (net.minecraft.item.ItemStack) handle;

    ResourceLocation resourceLocation = Registry.ITEM.getKey(stack.getItem());
    NameSpacedKey registryName =
        NameSpacedKey.of(resourceLocation.getNamespace(), resourceLocation.getPath());

    ItemType type = this.registry.getType(registryName);
    if (type == null) {
      throw new ItemMappingException("No item type with the name " + registryName + " found");
    }

    ItemMeta meta = this.metaFactory.createMeta(type);

    if (stack.getTag() != null) {
      meta.applyNBTFrom(stack.getTag());
    }

    return this.itemFactory.createItemStack(type, stack.getCount(), meta);
  }

  @Override
  public Object toMinecraft(ItemStack stack) throws ItemMappingException {
    Item item =
        Registry.ITEM
            .getValue(stack.getType().getResourceLocation().getHandle())
            .orElseThrow(
                () ->
                    new ItemMappingException(
                        "Unknown item " + stack.getType().getResourceLocation()));

    net.minecraft.item.ItemStack result =
        new net.minecraft.item.ItemStack(new IItemProvider() {
          @Override
          public Item asItem() {
            return item;
          }
        }, stack.getStackSize());

    if (stack.hasItemMeta()) {
      result.setTag(new CompoundNBT());
      stack.getItemMeta(false).copyNBTTo(result.getTag());
    }

    return result;
  }
}
