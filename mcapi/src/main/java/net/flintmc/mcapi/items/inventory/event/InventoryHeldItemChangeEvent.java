package net.flintmc.mcapi.items.inventory.event;

import net.flintmc.framework.eventbus.event.Cancellable;
import net.flintmc.framework.eventbus.event.Event;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;

public interface InventoryHeldItemChangeEvent extends Event, Cancellable {

  int getSlot();

  @AssistedFactory(InventoryHeldItemChangeEvent.class)
  interface Factory {

    InventoryHeldItemChangeEvent create(@Assisted int slot);
  }
}
