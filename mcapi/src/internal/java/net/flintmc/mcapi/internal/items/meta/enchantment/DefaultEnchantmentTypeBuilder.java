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

package net.flintmc.mcapi.internal.items.meta.enchantment;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.framework.stereotype.NameSpacedKey;
import net.flintmc.mcapi.chat.builder.ComponentBuilder;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.items.meta.enchantment.Enchantment;
import net.flintmc.mcapi.items.meta.enchantment.EnchantmentRarity;
import net.flintmc.mcapi.items.meta.enchantment.EnchantmentType;

@Implement(EnchantmentType.Builder.class)
public class DefaultEnchantmentTypeBuilder implements EnchantmentType.Builder {

  private final Enchantment.Factory enchantmentFactory;
  private final ComponentBuilder.Factory componentFactory;

  private NameSpacedKey registryName;
  private int highestLevel = 5;
  private ChatComponent displayName;
  private EnchantmentRarity rarity = EnchantmentRarity.UNCOMMON;

  @Inject
  public DefaultEnchantmentTypeBuilder(
      Enchantment.Factory enchantmentFactory, ComponentBuilder.Factory componentFactory) {
    this.enchantmentFactory = enchantmentFactory;
    this.componentFactory = componentFactory;
  }

  @Override
  public EnchantmentType.Builder registryName(NameSpacedKey registryName) {
    this.registryName = registryName;
    return this;
  }

  @Override
  public EnchantmentType.Builder highestLevel(int highestLevel) {
    this.highestLevel = highestLevel;
    return this;
  }

  @Override
  public EnchantmentType.Builder displayName(ChatComponent displayName) {
    this.displayName = displayName;
    return this;
  }

  @Override
  public EnchantmentType.Builder rarity(EnchantmentRarity rarity) {
    this.rarity = rarity;
    return this;
  }

  @Override
  public EnchantmentType build() {
    Preconditions.checkNotNull(this.registryName, "Missing registryName");
    Preconditions.checkNotNull(this.rarity, "Missing rarity");
    Preconditions.checkArgument(
        this.highestLevel > 0, "HighestLevel needs to be at least 1, got %d", this.highestLevel);

    if (this.displayName == null) {
      this.displayName = this.componentFactory.text().text(this.registryName.getKey()).build();
    }

    return new DefaultEnchantmentType(
        this.enchantmentFactory,
        this.registryName,
        this.highestLevel,
        this.displayName,
        this.rarity);
  }
}
