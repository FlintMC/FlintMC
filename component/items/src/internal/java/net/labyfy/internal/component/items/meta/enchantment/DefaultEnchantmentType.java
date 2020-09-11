package net.labyfy.internal.component.items.meta.enchantment;

import net.labyfy.component.items.meta.enchantment.Enchantment;
import net.labyfy.component.items.meta.enchantment.EnchantmentRarity;
import net.labyfy.component.items.meta.enchantment.EnchantmentType;
import net.labyfy.component.stereotype.NameSpacedKey;

public class DefaultEnchantmentType implements EnchantmentType {

  private final Enchantment.Factory enchantmentFactory;
  private final NameSpacedKey registryName;
  private final int highestLevel;
  private final EnchantmentRarity rarity;

  public DefaultEnchantmentType(Enchantment.Factory enchantmentFactory, NameSpacedKey registryName, int highestLevel, EnchantmentRarity rarity) {
    this.enchantmentFactory = enchantmentFactory;
    this.registryName = registryName;
    this.highestLevel = highestLevel;
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
  public Enchantment createEnchantment(int level) {
    return this.enchantmentFactory.createEnchantment(this, level);
  }
}
