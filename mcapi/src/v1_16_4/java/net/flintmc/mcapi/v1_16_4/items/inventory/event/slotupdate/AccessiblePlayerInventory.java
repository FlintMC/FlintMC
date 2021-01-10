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

package net.flintmc.mcapi.v1_16_4.items.inventory.event.slotupdate;

import com.google.common.collect.ImmutableList;
import java.util.List;
import net.flintmc.transform.shadow.FieldGetter;
import net.flintmc.transform.shadow.FieldSetter;
import net.flintmc.transform.shadow.Shadow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

@Shadow(value = "net.minecraft.entity.player.PlayerInventory", version = "1.16.4")
public interface AccessiblePlayerInventory {

  @FieldGetter("mainInventory")
  NonNullList<ItemStack> getMainInventory();

  @FieldSetter("mainInventory")
  void setMainInventory(NonNullList<ItemStack> list);

  @FieldGetter("armorInventory")
  NonNullList<ItemStack> getArmorInventory();

  @FieldSetter("armorInventory")
  void setArmorInventory(NonNullList<ItemStack> list);

  @FieldGetter("offHandInventory")
  NonNullList<ItemStack> getOffHandInventory();

  @FieldSetter("offHandInventory")
  void setOffHandInventory(NonNullList<ItemStack> list);

  default void updateAllInventories() {
    this.setAllInventories(
        ImmutableList.of(
            this.getMainInventory(), this.getArmorInventory(), this.getOffHandInventory()));
  }

  @FieldSetter("allInventories")
  void setAllInventories(List<NonNullList<ItemStack>> inventories);
}
