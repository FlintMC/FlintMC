package net.labyfy.internal.component.items.meta.enchantment;

import com.google.common.base.Preconditions;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.items.meta.enchantment.Enchantment;
import net.labyfy.component.items.meta.enchantment.EnchantmentType;

@Implement(Enchantment.class)
public class DefaultEnchantment implements Enchantment {

  private final EnchantmentType type;
  private final int level;

  @AssistedInject
  public DefaultEnchantment(@Assisted("type") EnchantmentType type, @Assisted("level") int level) {
    Preconditions.checkArgument(level > 0, "Level needs to be at least 1");
    this.type = type;
    this.level = level;
  }

  @Override
  public EnchantmentType getType() {
    return this.type;
  }

  @Override
  public int getLevel() {
    return this.level;
  }
}
