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

package net.flintmc.mcapi.internal.items.meta;

import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.Collection;
import net.flintmc.mcapi.items.meta.ItemHideFlag;
import net.flintmc.mcapi.items.meta.ItemMeta;
import net.flintmc.mcapi.items.type.ItemType;

public abstract class DefaultItemMeta implements ItemMeta {

  private static final ItemHideFlag[] HIDE_FLAGS = ItemHideFlag.values();

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

  protected abstract int getHideFlagsBase();

  protected abstract void setHideFlagsBase(int base);

  @Override
  public ItemHideFlag[] getHideFlags() {
    int base = this.getHideFlagsBase();

    Collection<ItemHideFlag> flags = new ArrayList<>();
    for (ItemHideFlag flag : HIDE_FLAGS) {
      if (this.isFlagSet(base, flag)) {
        flags.add(flag);
      }
    }

    return flags.toArray(new ItemHideFlag[0]);
  }

  @Override
  public boolean hasHideFlag(ItemHideFlag flag) {
    return this.isFlagSet(this.getHideFlagsBase(), flag);
  }

  @Override
  public void setHideFlag(ItemHideFlag flag, boolean value) {
    int base = this.getHideFlagsBase();

    if (this.isFlagSet(base, flag) == value) {
      return;
    }

    if (value) {
      base |= flag.getModifier();
    } else {
      base &= ~flag.getModifier();
    }

    this.setHideFlagsBase(base);
  }

  @Override
  public void setAllHideFlags(boolean value) {
    this.setHideFlagsBase(value ? 63 : 0);
  }

  protected boolean isFlagSet(int base, ItemHideFlag flag) {
    return (base & flag.getModifier()) == flag.getModifier();
  }

  protected void validateDamage(int damage) {
    if (!this.type.isDamageable()) {
      throw new IllegalStateException(
          "The type " + this.type.getRegistryName() + " is not damagable");
    }

    if (damage < 0 || damage > this.type.getMaxDamage()) {
      throw new IllegalArgumentException("Invalid damage provided: " + damage);
    }
  }
}
