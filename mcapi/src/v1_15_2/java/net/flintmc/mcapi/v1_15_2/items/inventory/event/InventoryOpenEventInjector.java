package net.flintmc.mcapi.v1_15_2.items.inventory.event;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.eventbus.EventBus;
import net.flintmc.mcapi.items.inventory.event.InventoryOpenEvent;

@Singleton
public class InventoryOpenEventInjector {

  private final EventBus eventBus;
  private final InventoryOpenEvent.Factory eventFactory;

  @Inject
  public InventoryOpenEventInjector(EventBus eventBus, InventoryOpenEvent.Factory eventFactory) {
    this.eventBus = eventBus;
    this.eventFactory = eventFactory;
  }
}
