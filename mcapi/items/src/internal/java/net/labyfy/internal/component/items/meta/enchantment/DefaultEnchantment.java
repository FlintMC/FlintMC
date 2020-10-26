package net.labyfy.internal.component.items.meta.enchantment;

import com.google.common.base.Preconditions;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.labyfy.chat.builder.ComponentBuilder;
import net.labyfy.chat.component.ChatComponent;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.items.meta.enchantment.Enchantment;
import net.labyfy.component.items.meta.enchantment.EnchantmentType;

@Implement(Enchantment.class)
public class DefaultEnchantment implements Enchantment {

  private final ComponentBuilder.Factory componentFactory;
  private final EnchantmentType type;
  private final int level;
  private ChatComponent displayName;

  @AssistedInject
  public DefaultEnchantment(ComponentBuilder.Factory componentFactory, @Assisted("type") EnchantmentType type, @Assisted("level") int level) {
    Preconditions.checkArgument(level > 0, "Level needs to be at least 1");
    this.componentFactory = componentFactory;
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

  @Override
  public ChatComponent getDisplayName() {
    if (this.displayName != null)
      return this.displayName;

    ChatComponent displayName = this.type.getDisplayName().copy();

    if (this.level != 1 || this.type.getHighestLevel() != 1) {
      displayName.append(this.componentFactory.text().text(" ").build());
      displayName.append(this.componentFactory.translation().translationKey("enchantment.level." + this.level).build());
    }

    return this.displayName = displayName;
  }
}
