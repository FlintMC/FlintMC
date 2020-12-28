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

package net.flintmc.mcapi.internal.items.type;

import net.flintmc.framework.stereotype.NameSpacedKey;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.internal.items.DefaultItemStack;
import net.flintmc.mcapi.items.ItemStack;
import net.flintmc.mcapi.items.meta.ItemMeta;
import net.flintmc.mcapi.items.type.ItemCategory;
import net.flintmc.mcapi.items.type.ItemType;
import net.flintmc.mcapi.resources.ResourceLocation;

public class DefaultItemType implements ItemType {

  private final ItemMeta.Factory metaFactory;
  private final ItemCategory category;
  private final NameSpacedKey registryName;
  private final ChatComponent defaultDisplayName;
  private final int maxStackSize;
  private final int maxDamage;
  private final Class<? extends ItemMeta> metaClass;
  private final ResourceLocation resourceLocation;

  public DefaultItemType(
      ItemMeta.Factory metaFactory,
      ItemCategory category,
      NameSpacedKey registryName,
      ChatComponent defaultDisplayName,
      int maxStackSize,
      int maxDamage,
      Class<? extends ItemMeta> metaClass,
      ResourceLocation resourceLocation) {
    this.metaFactory = metaFactory;
    this.category = category;
    this.registryName = registryName;
    this.defaultDisplayName = defaultDisplayName;
    this.maxStackSize = maxStackSize;
    this.maxDamage = maxDamage;
    this.metaClass = metaClass != null ? metaClass : ItemMeta.class;
    this.resourceLocation = resourceLocation;
  }

  @Override
  public ItemCategory getCategory() {
    return this.category;
  }

  @Override
  public NameSpacedKey getRegistryName() {
    return this.registryName;
  }

  @Override
  public ChatComponent getDefaultDisplayName() {
    return this.defaultDisplayName;
  }

  @Override
  public int getMaxStackSize() {
    return this.maxStackSize;
  }

  @Override
  public int getMaxDamage() {
    return this.maxDamage;
  }

  @Override
  public boolean isDamageable() {
    return this.maxDamage > 0;
  }

  @Override
  public Class<? extends ItemMeta> getMetaClass() {
    return this.metaClass;
  }

  @Override
  public ItemStack createStack() {
    return this.createStack(1);
  }

  @Override
  public ItemStack createStack(int stackSize) {
    return new DefaultItemStack(this.metaFactory, this, stackSize);
  }

  @Override
  public ResourceLocation getResourceLocation() {
    return this.resourceLocation;
  }
}
