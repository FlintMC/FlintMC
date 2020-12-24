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

package net.flintmc.mcapi.v1_15_2.items.inventory.event.slotupdate;

import com.google.common.collect.ImmutableList;
import java.util.List;
import net.flintmc.transform.shadow.FieldGetter;
import net.flintmc.transform.shadow.FieldSetter;
import net.flintmc.transform.shadow.Shadow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

@Shadow("net.minecraft.entity.player.PlayerInventory")
public interface AccessiblePlayerInventory {

  @FieldSetter("mainInventory")
  void setMainInventory(NonNullList<ItemStack> list);

  @FieldSetter("armorInventory")
  void setArmorInventory(NonNullList<ItemStack> list);

  @FieldSetter("offHandInventory")
  void setOffHandInventory(NonNullList<ItemStack> list);

  @FieldGetter("mainInventory")
  NonNullList<ItemStack> getMainInventory();

  @FieldGetter("armorInventory")
  NonNullList<ItemStack> getArmorInventory();

  @FieldGetter("offHandInventory")
  NonNullList<ItemStack> getOffHandInventory();

  default void updateAllInventories() {
    this.setAllInventories(
        ImmutableList.of(
            this.getMainInventory(), this.getArmorInventory(), this.getOffHandInventory()));
  }

  @FieldSetter("allInventories")
  void setAllInventories(List<NonNullList<ItemStack>> inventories);
}
