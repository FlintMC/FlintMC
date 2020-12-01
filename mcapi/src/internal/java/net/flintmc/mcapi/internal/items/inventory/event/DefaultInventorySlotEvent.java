package net.flintmc.mcapi.internal.items.inventory.event;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.items.inventory.Inventory;
import net.flintmc.mcapi.items.inventory.event.InventorySlotEvent;

/** {@inheritDoc} */
@Implement(InventorySlotEvent.class)
public class DefaultInventorySlotEvent extends DefaultInventoryEvent implements InventorySlotEvent {

  private final int slot;

  @AssistedInject
  public DefaultInventorySlotEvent(
      @Assisted("inventory") Inventory inventory, @Assisted("slot") int slot) {
    super(inventory);
    this.slot = slot;
  }

  /** {@inheritDoc} */
  @Override
  public int getSlot() {
    return this.slot;
  }
}
