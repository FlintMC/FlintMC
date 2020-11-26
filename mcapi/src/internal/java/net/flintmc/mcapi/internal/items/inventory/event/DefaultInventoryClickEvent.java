package net.flintmc.mcapi.internal.items.inventory.event;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.items.ItemStack;
import net.flintmc.mcapi.items.inventory.Inventory;
import net.flintmc.mcapi.items.inventory.InventoryClick;
import net.flintmc.mcapi.items.inventory.event.InventoryClickEvent;

@Implement(InventoryClickEvent.class)
public class DefaultInventoryClickEvent extends DefaultInventoryEvent
    implements InventoryClickEvent {

  private final ItemStack clickedItem;
  private final InventoryClick clickType;
  private final int slot;
  private boolean cancelled;

  @AssistedInject
  public DefaultInventoryClickEvent(
      @Assisted("inventory") Inventory inventory,
      @Assisted("slot") int slot,
      @Assisted("clickType") InventoryClick clickType) {
    super(inventory);
    this.clickedItem = slot < 0 /* outside of any slot */ ? null : inventory.getItem(slot);
    ;
    this.clickType = clickType;
    this.slot = slot;
  }

  @Override
  public ItemStack getClickedItem() {
    return this.clickedItem;
  }

  @Override
  public InventoryClick getClickType() {
    return this.clickType;
  }

  @Override
  public int getSlot() {
    return this.slot;
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
