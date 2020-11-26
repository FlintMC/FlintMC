package net.flintmc.mcapi.internal.items.inventory.event;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.items.ItemStack;
import net.flintmc.mcapi.items.inventory.Inventory;
import net.flintmc.mcapi.items.inventory.event.InventoryHotkeyPressEvent;

@Implement(InventoryHotkeyPressEvent.class)
public class DefaultInventoryHotkeyPressEvent extends DefaultInventorySlotEvent
    implements InventoryHotkeyPressEvent {

  private final int hotkey;
  private final ItemStack clickedItem;
  private boolean cancelled;

  @AssistedInject
  public DefaultInventoryHotkeyPressEvent(
      @Assisted("inventory") Inventory inventory,
      @Assisted("slot") int slot,
      @Assisted("hotkey") int hotkey) {
    super(inventory, slot);
    this.hotkey = hotkey;
    this.clickedItem =
        slot < 0 /* outside of any slot */
            ? null
            : inventory.getItem(
                slot); // TODO not working, always the item after the click instead of the clicked
    // item
  }

  @Override
  public int getHotkey() {
    return this.hotkey;
  }

  @Override
  public ItemStack getClickedItem() {
    return this.clickedItem;
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
