package net.flintmc.mcapi.internal.items.inventory.event;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.items.inventory.Inventory;
import net.flintmc.mcapi.items.inventory.event.InventoryOpenEvent;

/** {@inheritDoc} */
@Implement(InventoryOpenEvent.class)
public class DefaultInventoryOpenEvent extends DefaultInventoryEvent implements InventoryOpenEvent {

  @AssistedInject
  public DefaultInventoryOpenEvent(@Assisted("inventory") Inventory inventory) {
    super(inventory);
  }
}
