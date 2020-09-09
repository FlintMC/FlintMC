package net.labyfy.internal.component.items.meta;

import com.google.common.base.Preconditions;
import net.labyfy.component.items.meta.ItemMeta;
import net.labyfy.component.items.type.ItemType;

public abstract class DefaultItemMeta implements ItemMeta {

  protected ItemType type;

  public void setType(ItemType type) {
    Preconditions.checkState(this.type == null, "Cannot set the type twice");
    this.type = type;
  }

  @Override
  public int getRemainingDurability() {
    return this.type.isDamageable() ? this.type.getMaxDamage() - this.getDamage() : -1;
  }

  @Override
  public void setRemainingDurability(int remainingDurability) {
    this.setDamage(this.type.getMaxDamage() - remainingDurability);
  }

  protected void validateDamage(int damage) {
    if (!this.type.isDamageable()) {
      throw new IllegalStateException("The type " + this.type.getRegistryName() + " is not damagable");
    }

    if (damage < 0 || damage > this.type.getMaxDamage()) {
      throw new IllegalArgumentException("Invalid damage provided: " + damage);
    }
  }

}
