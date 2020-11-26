package net.flintmc.mcapi.items.inventory.event;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.items.ItemStack;
import net.flintmc.mcapi.items.inventory.Inventory;

// only pre phase
public interface InventoryUpdateSlotEvent extends InventorySlotEvent, InventoryEvent {

  ItemStack getPreviousItem();

  ItemStack getNewItem();

  @AssistedFactory(InventoryUpdateSlotEvent.class)
  interface Factory {

    InventoryUpdateSlotEvent create(
        @Assisted("inventory") Inventory inventory,
        @Assisted("slot") int slot,
        @Assisted("previousItem") ItemStack previousItem,
        @Assisted("newItem") ItemStack newItem);
  }
}
