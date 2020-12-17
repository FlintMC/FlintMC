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
