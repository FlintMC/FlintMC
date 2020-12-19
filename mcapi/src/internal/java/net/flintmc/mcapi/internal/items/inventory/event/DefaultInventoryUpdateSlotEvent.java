package net.flintmc.mcapi.internal.items.inventory.event;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.items.ItemStack;
import net.flintmc.mcapi.items.inventory.Inventory;
import net.flintmc.mcapi.items.inventory.event.InventoryUpdateSlotEvent;

/** {@inheritDoc} */
@Implement(InventoryUpdateSlotEvent.class)
public class DefaultInventoryUpdateSlotEvent extends DefaultInventorySlotEvent
    implements InventoryUpdateSlotEvent {

  private final ItemStack newItem;

  @AssistedInject
  public DefaultInventoryUpdateSlotEvent(
      @Assisted("inventory") Inventory inventory,
      @Assisted("slot") int slot,
      @Assisted("newItem") ItemStack newItem) {
    super(inventory, slot);
    this.newItem = newItem;
  }

  /** {@inheritDoc} */
  @Override
  public ItemStack getNewItem() {
    return this.newItem;
  }
}
