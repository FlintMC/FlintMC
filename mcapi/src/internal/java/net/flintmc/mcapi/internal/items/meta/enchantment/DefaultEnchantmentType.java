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

import net.flintmc.framework.stereotype.NameSpacedKey;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.items.meta.enchantment.Enchantment;
import net.flintmc.mcapi.items.meta.enchantment.EnchantmentRarity;
import net.flintmc.mcapi.items.meta.enchantment.EnchantmentType;

public class DefaultEnchantmentType implements EnchantmentType {

  private final Enchantment.Factory enchantmentFactory;
  private final NameSpacedKey registryName;
  private final int highestLevel;
  private final ChatComponent displayName;
  private final EnchantmentRarity rarity;

  public DefaultEnchantmentType(
      Enchantment.Factory enchantmentFactory,
      NameSpacedKey registryName,
      int highestLevel,
      ChatComponent displayName,
      EnchantmentRarity rarity) {
    this.enchantmentFactory = enchantmentFactory;
    this.registryName = registryName;
    this.highestLevel = highestLevel;
    this.displayName = displayName;
    this.rarity = rarity;
  }

  @Override
  public NameSpacedKey getRegistryName() {
    return this.registryName;
  }

  @Override
  public int getHighestLevel() {
    return this.highestLevel;
  }

  @Override
  public EnchantmentRarity getRarity() {
    return this.rarity;
  }

  @Override
  public ChatComponent getDisplayName() {
    return this.displayName;
  }

  @Override
  public Enchantment createEnchantment(int level) {
    return this.enchantmentFactory.createEnchantment(this, level);
  }
}
