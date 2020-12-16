package net.flintmc.mcapi.internal.items.inventory.event;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.items.inventory.Inventory;
import net.flintmc.mcapi.items.inventory.event.InventoryEvent;

/** {@inheritDoc} */
@Implement(InventoryEvent.class)
public class DefaultInventoryEvent implements InventoryEvent {

  private final Inventory inventory;

  @AssistedInject
  public DefaultInventoryEvent(@Assisted("inventory") Inventory inventory) {
    this.inventory = inventory;
  }

  /** {@inheritDoc} */
  @Override
  public Inventory getInventory() {
    return this.inventory;
  }
}
