package net.labyfy.internal.component.items.meta.enchantment;

import net.labyfy.chat.component.ChatComponent;
import net.labyfy.component.items.meta.enchantment.Enchantment;
import net.labyfy.component.items.meta.enchantment.EnchantmentRarity;
import net.labyfy.component.items.meta.enchantment.EnchantmentType;
import net.labyfy.component.stereotype.NameSpacedKey;

public class DefaultEnchantmentType implements EnchantmentType {

  private final Enchantment.Factory enchantmentFactory;
  private final NameSpacedKey registryName;
  private final int highestLevel;
  private final ChatComponent displayName;
  private final EnchantmentRarity rarity;

  public DefaultEnchantmentType(Enchantment.Factory enchantmentFactory, NameSpacedKey registryName, int highestLevel, ChatComponent displayName, EnchantmentRarity rarity) {
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
