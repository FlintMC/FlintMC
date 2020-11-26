package net.flintmc.mcapi.internal.items.inventory.event;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.items.inventory.Inventory;
import net.flintmc.mcapi.items.inventory.event.InventoryCloseEvent;

@Implement(InventoryCloseEvent.class)
public class DefaultInventoryCloseEvent extends DefaultInventoryEvent
    implements InventoryCloseEvent {

  private final Direction direction;
  private boolean cancelled;

  @AssistedInject
  public DefaultInventoryCloseEvent(
      @Assisted("inventory") Inventory inventory, @Assisted("direction") Direction direction) {
    super(inventory);
    this.direction = direction;
  }

  @Override
  public Direction getDirection() {
    return this.direction;
  }

  @Override
  public boolean isCancelled() {
    return this.cancelled;
  }

  @Override
  public void setCancelled(boolean cancelled) {
    this.cancelled = cancelled;
  }
}
