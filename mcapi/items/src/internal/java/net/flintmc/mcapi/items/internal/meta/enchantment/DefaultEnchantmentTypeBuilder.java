package net.flintmc.mcapi.items.internal.meta.enchantment;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import net.flintmc.mcapi.chat.builder.ComponentBuilder;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.items.meta.enchantment.Enchantment;
import net.flintmc.mcapi.items.meta.enchantment.EnchantmentRarity;
import net.flintmc.mcapi.items.meta.enchantment.EnchantmentType;
import net.flintmc.framework.stereotype.NameSpacedKey;

@Implement(EnchantmentType.Builder.class)
public class DefaultEnchantmentTypeBuilder implements EnchantmentType.Builder {

  private final Enchantment.Factory enchantmentFactory;
  private final ComponentBuilder.Factory componentFactory;

  private NameSpacedKey registryName;
  private int highestLevel = 5;
  private ChatComponent displayName;
  private EnchantmentRarity rarity = EnchantmentRarity.UNCOMMON;

  @Inject
  public DefaultEnchantmentTypeBuilder(Enchantment.Factory enchantmentFactory, ComponentBuilder.Factory componentFactory) {
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
    Preconditions.checkArgument(this.highestLevel > 0, "HighestLevel needs to be at least 1, got %d", this.highestLevel);

    if (this.displayName == null) {
      this.displayName = this.componentFactory.text().text(this.registryName.getKey()).build();
    }

    return new DefaultEnchantmentType(this.enchantmentFactory, this.registryName, this.highestLevel, this.displayName, this.rarity);
  }
}
