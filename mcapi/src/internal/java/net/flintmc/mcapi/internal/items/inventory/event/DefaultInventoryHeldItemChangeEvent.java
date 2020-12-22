package net.flintmc.mcapi.internal.items.inventory.event;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.items.ItemStack;
import net.flintmc.mcapi.items.inventory.event.InventoryHeldItemChangeEvent;

/** {@inheritDoc} */
@Implement(InventoryHeldItemChangeEvent.class)
public class DefaultInventoryHeldItemChangeEvent implements InventoryHeldItemChangeEvent {

  private final int slot;
  private final ItemStack item;

  private boolean cancelled;

  @AssistedInject
  public DefaultInventoryHeldItemChangeEvent(@Assisted int slot, @Assisted ItemStack item) {
    this.slot = slot;
    this.item = item;
  }

  /** {@inheritDoc} */
  @Override
  public int getSlot() {
    return this.slot;
  }

  /** {@inheritDoc} */
  @Override
  public ItemStack getItem() {
    return this.item;
  }

  /** {@inheritDoc} */
  @Override
  public boolean isCancelled() {
    return this.cancelled;
  }

  /** {@inheritDoc} */
  @Override
  public void setCancelled(boolean cancelled) {
    this.cancelled = cancelled;
  }
}
