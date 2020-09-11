package net.labyfy.internal.component.items.meta.enchantment;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.items.meta.enchantment.Enchantment;
import net.labyfy.component.items.meta.enchantment.EnchantmentRarity;
import net.labyfy.component.items.meta.enchantment.EnchantmentType;
import net.labyfy.component.stereotype.NameSpacedKey;

@Implement(EnchantmentType.Builder.class)
public class DefaultEnchantmentTypeBuilder implements EnchantmentType.Builder {

  private final Enchantment.Factory enchantmentFactory;
  private NameSpacedKey registryName;
  private int highestLevel = 5;
  private EnchantmentRarity rarity = EnchantmentRarity.UNCOMMON;

  @Inject
  public DefaultEnchantmentTypeBuilder(Enchantment.Factory enchantmentFactory) {
    this.enchantmentFactory = enchantmentFactory;
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
  public EnchantmentType.Builder rarity(EnchantmentRarity rarity) {
    this.rarity = rarity;
    return this;
  }

  @Override
  public EnchantmentType build() {
    Preconditions.checkNotNull(this.registryName, "Missing registryName");
    Preconditions.checkNotNull(this.rarity, "Missing rarity");
    Preconditions.checkArgument(this.highestLevel > 0, "HighestLevel needs to be at least 1, got %d", this.highestLevel);

    return new DefaultEnchantmentType(this.enchantmentFactory, this.registryName, this.highestLevel, this.rarity);
  }
}
